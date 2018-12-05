package com.example.administrator.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.myapplication.JSON.ReviewBean;
import com.example.administrator.myapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewFragment extends Fragment {
    private LinearLayout fragment_review_line;
    private NestedScrollView fragment_review_scrollview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_review, container, false);
        fragment_review_line = view.findViewById(R.id.fragment_review_line);
        fragment_review_scrollview = view.findViewById(R.id.fragment_review_scrollview);
        return view;
    }

    public boolean isScrollTop(){
        if(fragment_review_scrollview.getScrollY() == 0){
            return true;
        }
        return false;
    }
    public void prePareReview(List<ReviewBean> reviewBeanList) {
        RequestOptions options = new RequestOptions().centerCrop();
        for (ReviewBean reviewBean : reviewBeanList) {
            View view = getLayoutInflater().inflate(R.layout.item_review, fragment_review_line, false);
            TextView fragment_review_item_title = view.findViewById(R.id.fragment_review_item_title);
            CircleImageView fragment_review_item_avatar = view.findViewById(R.id.fragment_review_item_avatar);
            TextView fragment_review_item_name = view.findViewById(R.id.fragment_review_item_name);
            RatingBar fragment_review_item_ratingbar = view.findViewById(R.id.fragment_review_item_ratingbar);
            fragment_review_item_title.setText(reviewBean.title);
            Glide.with(this).load(reviewBean.author.avatar).apply(options).into(fragment_review_item_avatar);
            fragment_review_item_name.setText(reviewBean.author.name);
            fragment_review_item_ratingbar.setRating(reviewBean.rating.value);
            fragment_review_line.addView(view);
        }
    }
}
