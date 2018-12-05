package com.example.administrator.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.myapplication.HTML.MovieHtml;
import com.example.administrator.myapplication.Activity.MovieActicity;
import com.example.administrator.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class HotLaterFragment extends Fragment {

    public static final int GRABE_SUCCESS = 1;
    public static final int GRABE_FAIL = 2;
    private SwipeRefreshLayout fragment_hot_later_swipe;
    private StickyListHeadersListView fragment_hot_later_sticky;
    private ArrayList<MovieHtml> movieHtmls;
    private MovieAdapter adapter;
    private Handler hotLaterHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage (Message message) {
            switch (message.what) {
                case GRABE_SUCCESS: {
                    adapter.setMovieHtmls(movieHtmls);
                    adapter.setClickble(true);
                    adapter.notifyDataSetChanged();
                    fragment_hot_later_swipe.setRefreshing(false);
                    break;
                }
                case GRABE_FAIL: {
                    Toast.makeText(getContext(), "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hot_later, container, false);
        fragment_hot_later_swipe = view.findViewById(R.id.fragment_hot_later_swipe);
        fragment_hot_later_sticky = view.findViewById(R.id.fragment_hot_later_sticky);
        fragment_hot_later_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                grabeDataFromWeb();
            }
        });
        movieHtmls = new ArrayList<>();
        adapter = new MovieAdapter(getContext());
        fragment_hot_later_sticky.setAdapter(adapter);
        grabeDataFromWeb();
        return view;
    }

    private void grabeDataFromWeb () {
        fragment_hot_later_swipe.setRefreshing(true);
        adapter.setClickble(false);
        new Thread(new Runnable() {
            @Override
            public void run () {
                Document document = null;
                int id = 0;
                String oldTime = null;
                try {
                    document = Jsoup.connect("https://movie.douban.com/cinema/later/xian").get();
                    Elements item = document.select("div.item.mod");
                    for (int i = 0; i < item.size(); i++) {
                        MovieHtml html = new MovieHtml();
                        Element temp = item.get(i);
                        Elements a = temp.select("a");
                        Elements li = temp.select("li");
                        html.thumb = a.get(0).attr("href");
                        html.src = temp.select("img").attr("src");
                        html.title = a.get(1).text();
                        html.time = li.get(0).text();
                        html.genres = li.get(1).text();
                        html.location = li.get(2).text();
                        html.want = li.get(3).text();
                        html.movieId = html.thumb.split("/")[4];
                        id = getId(id, oldTime, html.time);
                        html.headerId = id;
                        oldTime = html.time;
                        movieHtmls.add(html);
                    }
                    hotLaterHandler.sendEmptyMessage(GRABE_SUCCESS);
                } catch (IOException e) {
                    hotLaterHandler.sendEmptyMessage(GRABE_FAIL);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int getId (int id, String oldtime, String time) {
        if (oldtime == null) {
            return 1;
        } else if (oldtime.equals(time)) {
            return id;
        } else {
            return id + 1;
        }
    }

    private class MovieAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private ArrayList<MovieHtml> movieHtmls;
        private LayoutInflater inflater;
        private Context context;
        private boolean clickble = false;

        public MovieAdapter (Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void setMovieHtmls (ArrayList<MovieHtml> movieHtmls) {
            this.movieHtmls = movieHtmls;
        }

        public void setClickble (boolean clickble) {
            this.clickble = clickble;
        }

        private class HeaderHolder {
            TextView header_txtv;

            public HeaderHolder (View view) {
                this.header_txtv = view.findViewById(R.id.item_header_txv);
            }
        }

        private class ItemHolder {
            ImageView item_hot_later_cover;
            TextView item_hot_later_title;
            TextView item_hot_later_genres;
            TextView item_hot_later_location;
            TextView item_hot_later_viewer;
            Button item_hot_later_ticket;
            LinearLayout item_hot_later_rating_bar;

            public ItemHolder (View view) {
                this.item_hot_later_cover = view.findViewById(R.id.item_hot_on_cover);
                this.item_hot_later_title = view.findViewById(R.id.item_hot_on_title);
                this.item_hot_later_genres = view.findViewById(R.id.item_hot_on_directors);
                this.item_hot_later_location = view.findViewById(R.id.item_hot_on_casts);
                this.item_hot_later_viewer = view.findViewById(R.id.item_hot_on_viewer);
                this.item_hot_later_rating_bar = view.findViewById(R.id.item_hot_on_rating_item);
                this.item_hot_later_ticket = view.findViewById(R.id.item_hot_on_ticket);
                this.item_hot_later_rating_bar.setVisibility(View.GONE);
            }
        }

        @Override
        public View getHeaderView (int position, View convertView, ViewGroup parent) {
            HeaderHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_header, parent, false);
                holder = new HeaderHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (HeaderHolder) convertView.getTag();
            }
            holder.header_txtv.setText(movieHtmls.get(position).time);
            return convertView;
        }

        @Override
        public long getHeaderId (int position) {
            return movieHtmls.get(position).headerId;
        }

        @Override
        public int getCount () {
            if(movieHtmls == null){
                return 0;
            }
            return movieHtmls.size();
        }

        @Override
        public Object getItem (int position) {
            return movieHtmls.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (final int position, View convertView, ViewGroup viewGroup) {
            ItemHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_hot_on, viewGroup, false);
                holder = new ItemHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolder) convertView.getTag();
            }
            MovieHtml movieHtml = movieHtmls.get(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if(clickble){
                        Intent intent = new Intent(getContext(), MovieActicity.class);
                        intent.putExtra("MovieId", movieHtmls.get(position).movieId);
                        startActivity(intent);
                    }
                }
            });
            Glide.with(context).load(movieHtml.src).apply(RequestOptions.centerCropTransform())
                    .into(holder.item_hot_later_cover);
            holder.item_hot_later_title.setText(movieHtml.title);
            holder.item_hot_later_genres.setText(movieHtml.genres);
            holder.item_hot_later_location.setText(movieHtml.location);
            holder.item_hot_later_viewer.setText(movieHtml.want);
            holder.item_hot_later_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if(clickble){
                        Intent intent = new Intent(getContext(), MovieActicity.class);
                        intent.putExtra("MovieId", movieHtmls.get(position).movieId);
                        startActivity(intent);
                    }
                }
            });
            return convertView;
        }
    }

}
