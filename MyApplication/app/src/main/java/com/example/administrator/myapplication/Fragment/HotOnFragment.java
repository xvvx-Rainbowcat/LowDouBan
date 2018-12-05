package com.example.administrator.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.myapplication.JSON.CastBean;
import com.example.administrator.myapplication.JSON.JsonBean;
import com.example.administrator.myapplication.JSON.MovieBean;
import com.example.administrator.myapplication.Activity.MovieActicity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class HotOnFragment extends Fragment {
    public static final int RESULT_OK = 1;
    public static final int RESPONSE_GET = 0;
    public static final int RESPONSE_FAIL = 1;
    private RecyclerView hot_on_recyler;
    private SwipeRefreshLayout hot_on_swiperefreshlayout;
    private JsonBean jsonBean;
    private MovieAdapter movieAdapter;
    private String city;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage (Message msg) {
            switch (msg.what) {
                case RESPONSE_GET: {
                    jsonBean = (JsonBean) msg.obj;
                    movieAdapter = new MovieAdapter(jsonBean.movies);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    hot_on_recyler.setAdapter(movieAdapter);
                    hot_on_recyler.setLayoutManager(manager);
                    movieAdapter.setClickble(true);
                    hot_on_swiperefreshlayout.setRefreshing(false);
                    break;
                }
                case RESPONSE_FAIL: {
                    Toast.makeText(getContext(), "获取电影信息失败", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hot_on, container, false);
        hot_on_recyler = view.findViewById(R.id.hot_on_recycler);
        hot_on_swiperefreshlayout = view.findViewById(R.id.hot_on_swiperefreshlayout);
        if (city == null) {
            city = "西安";
        }
        hot_on_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                getServerData(city);
            }
        });
        getServerData(city);
        return view;
    }

    public void onCityChange (String city) {
        this.city = city;
        movieAdapter.setClickble(false);
        getServerData(city);
    }

    private void getServerData (final String city) {
        hot_on_swiperefreshlayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    Gson gson = new Gson();
                    String url = "https://api.douban.com/v2/movie/in_theaters?apikey=0b2bdeda43b5688921839c8ecb20399b&" + city;
                    String jsonstring = HttpUtil.getResponseFromServer(url);
                    JsonBean jsonBean = gson.fromJson(jsonstring, JsonBean.class);
                    sendMsg(RESPONSE_GET, jsonBean);
                } catch (IOException e) {
                    sendMsg(RESPONSE_FAIL, null);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
        private List<MovieBean> movieBeanList;
        private boolean clickble = false;

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_hot_on, viewGroup, false);
            MovieViewHolder viewHolder = new MovieViewHolder(view);
            return viewHolder;
        }

        public MovieAdapter (List<MovieBean> movieBeanList) {
            this.movieBeanList = movieBeanList;
        }

        public void setMovieBeanList (List<MovieBean> movieBeanList) {
            this.movieBeanList = movieBeanList;
        }

        public void setClickble (boolean clickble) {
            this.clickble = clickble;
        }

        @Override
        public void onBindViewHolder (@NonNull MovieViewHolder holder, int i) {
            final MovieBean movieBean = movieBeanList.get(i);
            RequestOptions requestOptions = new RequestOptions().centerCrop();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (clickble == true) {
                        Intent intent = new Intent(getContext(), MovieActicity.class);
                        intent.putExtra("MovieId", movieBean.id);
                        startActivity(intent);
                    }
                }
            });
            Glide.with(HotOnFragment.this).load(movieBean.images.medium).apply(requestOptions)
                    .into(holder.on_recycler_item_cover);
            holder.on_recycler_item_title.setText(movieBean.title);
            if(movieBean.rating.average <= 0.01){
                holder.on_recycler_item_rating.setText("暂无评分");
                holder.on_recyler_item_rating_bar.setVisibility(View.GONE);
            }else{
                holder.on_recyler_item_rating_bar.setVisibility(View.VISIBLE);
                holder.on_recycler_item_rating.setText(movieBean.rating.average + "");
                holder.on_recyler_item_rating_bar.setRating((float) movieBean.rating.average / 2);
            }
            StringBuilder builder = new StringBuilder();
            builder.append("导演: ");
            for (CastBean temp : movieBean.directors) {
                builder.append(temp.name + " / ");
            }
            builder.delete(builder.length() - 3, builder.length() - 1);
            String directorString = builder.toString();
            holder.on_recycler_item_directors.setText(directorString);
            builder = new StringBuilder();
            builder.append("演员: ");
            for (CastBean temp : movieBean.casts) {
                builder.append(temp.name + " / ");
            }
            builder.delete(builder.length() - 3, builder.length() - 1);
            String castsString = builder.toString();
            holder.on_recycler_item_casts.setText(castsString);
            builder = new StringBuilder();
            Double viewer = new Double(movieBean.collect_count / 10000);
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            builder.append(decimalFormat.format(viewer)).append(" 万人看过");
            holder.on_recycler_item_viewer.setText(builder.toString());
            holder.on_recycler_item_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    Intent intent = new Intent(getContext(), MovieActicity.class);
                    intent.putExtra("MovieId", movieBean.id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount () {
            return movieBeanList.size();
        }

        class MovieViewHolder extends RecyclerView.ViewHolder {
            private ImageView on_recycler_item_cover;
            private TextView on_recycler_item_title;
            private TextView on_recycler_item_rating;
            private RatingBar on_recyler_item_rating_bar;
            private TextView on_recycler_item_directors;
            private TextView on_recycler_item_casts;
            private TextView on_recycler_item_viewer;
            private Button on_recycler_item_ticket;

            public MovieViewHolder (@NonNull View itemView) {
                super(itemView);
                on_recycler_item_cover = itemView.findViewById(R.id.item_hot_on_cover);
                on_recycler_item_title = itemView.findViewById(R.id.item_hot_on_title);
                on_recycler_item_rating = itemView.findViewById(R.id.item_hot_on_rating);
                on_recyler_item_rating_bar = itemView.findViewById(R.id.item_hot_on_rating_bar);
                on_recycler_item_directors = itemView.findViewById(R.id.item_hot_on_directors);
                on_recycler_item_casts = itemView.findViewById(R.id.item_hot_on_casts);
                on_recycler_item_viewer = itemView.findViewById(R.id.item_hot_on_viewer);
                on_recycler_item_ticket = itemView.findViewById(R.id.item_hot_on_ticket);
            }
        }
    }

    private void sendMsg (int what, Object object) {
        Message message = new Message();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);
    }
}
