package com.example.administrator.myapplication.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.myapplication.HTML.HotHtml;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Activity.SearchActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FindFragment extends Fragment {
    public static final int GRABE_SUCCESS = 1;
    public static final int GRABE_FAIL = 2;
    private ConstraintLayout fragment_find_search;
    private RecyclerView fragment_find_recycler;
    private SwipeRefreshLayout fragment_find_swipe;
    private ArrayList<HotHtml> hotHtmls;
    private FindAdapter findAdapter;
    private Handler findHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage (Message message) {
            switch (message.what) {
                case GRABE_SUCCESS: {
                    fragment_find_swipe.setRefreshing(false);
                    findAdapter.setHotHtmls(hotHtmls);
                    findAdapter.notifyDataSetChanged();
                    findAdapter.setClickble(true);
                    break;
                }
                case GRABE_FAIL: {
                    break;
                }
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        fragment_find_recycler = view.findViewById(R.id.fragment_find_recycler);
        fragment_find_search = view.findViewById(R.id.fragment_find_search);
        fragment_find_swipe = view.findViewById(R.id.fragment_find_swipe);
        fragment_find_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        fragment_find_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                grabeDataFromWeb();
            }
        });
        findAdapter = new FindAdapter();
        fragment_find_recycler.setAdapter(findAdapter);
        fragment_find_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        hotHtmls = new ArrayList<>();
        grabeDataFromWeb();
        return view;
    }

    private class FindAdapter extends RecyclerView.Adapter<FindAdapter.FindViewHolder> {
        private boolean clickble = false;
        private ArrayList<HotHtml> hotHtmls;

        public void setClickble (boolean clickble) {
            this.clickble = clickble;
        }

        public void setHotHtmls (ArrayList<HotHtml> hotHtmls) {
            this.hotHtmls = hotHtmls;
        }

        @NonNull
        @Override
        public FindViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_find, viewGroup, false);
            FindViewHolder holder = new FindViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder (@NonNull FindViewHolder findViewHolder, int i) {
            final HotHtml hotHtml = hotHtmls.get(i);
            findViewHolder.item_find_title.setText(hotHtml.title);
            findViewHolder.item_find_doc.setText(hotHtml.doc);
            Glide.with(getContext()).load(hotHtml.src).apply(RequestOptions.centerCropTransform())
                    .into(findViewHolder.item_find_imgv);
            findViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if(clickble){
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(hotHtml.href);
                        intent.setData(uri);
                        startActivity(Intent.createChooser(intent, "请选择浏览器"));
                    }
                }
            });
        }

        @Override
        public int getItemCount () {
            if (hotHtmls == null) {
                return 0;
            }
            return hotHtmls.size();
        }

        class FindViewHolder extends RecyclerView.ViewHolder {
            private ImageView item_find_imgv;
            private TextView item_find_title;
            private TextView item_find_doc;

            public FindViewHolder (@NonNull View itemView) {
                super(itemView);
                this.item_find_imgv = itemView.findViewById(R.id.item_find_imgv);
                this.item_find_title = itemView.findViewById(R.id.item_find_title);
                this.item_find_doc = itemView.findViewById(R.id.item_find_doc);
            }
        }
    }

    private void grabeDataFromWeb () {
        fragment_find_swipe.setRefreshing(true);
        findAdapter.setClickble(false);
        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    Document document = Jsoup.connect("https://movie.douban.com").get();
                    Elements gallery = document.select("#hot-gallery");
                    Elements list = gallery.select("li.ui-slide-item");
                    for (Element temp : list) {
                        HotHtml hotHtml = new HotHtml();
                        Element a = temp.select("a").get(0);
                        Element img = a.select("img").get(0);
                        hotHtml.href = a.attr("href");
                        hotHtml.src = img.attr("src");
                        hotHtml.title = img.attr("alt");
                        hotHtml.doc = temp.select("div.gallery-bd").select("p").text();
                        hotHtmls.add(hotHtml);
                    }
                    findHandler.sendEmptyMessage(GRABE_SUCCESS);
                } catch (IOException e) {
                    findHandler.sendEmptyMessage(GRABE_FAIL);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
