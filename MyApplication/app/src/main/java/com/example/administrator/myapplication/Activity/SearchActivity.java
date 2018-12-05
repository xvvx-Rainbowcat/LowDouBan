package com.example.administrator.myapplication.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search,viewGroup,false);
            SearchViewHolder holder = new SearchViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder (@NonNull SearchViewHolder searchViewHolder, int i) {

        }

        @Override
        public int getItemCount () {
            return 0;
        }

        class SearchViewHolder extends RecyclerView.ViewHolder{
            private ImageView search_item_imgv;
            private TextView search_item_title;
            private TextView search_item_doc;
            public SearchViewHolder (@NonNull View itemView) {
                super(itemView);
                this.search_item_imgv = itemView.findViewById(R.id.search_item_imgv);
                this.search_item_title = itemView.findViewById(R.id.search_item_title);
                search_item_doc = itemView.findViewById(R.id.search_item_doc);
            }
        }
    }
}
