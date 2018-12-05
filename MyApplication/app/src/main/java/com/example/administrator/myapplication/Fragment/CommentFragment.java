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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.myapplication.JSON.Commentbean;
import com.example.administrator.myapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentFragment extends Fragment {
    private NestedScrollView fragment_comment_scrollview;
    private LinearLayout fragment_comment_line;
    CommentAreaFragment fragment_comment_area;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_comment, container, false);
        fragment_comment_scrollview = view.findViewById(R.id.fragment_comment_scrollview);
        fragment_comment_line = view.findViewById(R.id.fragment_comment_line);
        return view;
    }

    public void prepareComment (List<Commentbean> commentbeanList) {
        RequestOptions options = new RequestOptions().centerCrop();
        for (Commentbean commentbean : commentbeanList) {
            View view = getLayoutInflater().inflate(R.layout.item_comment, fragment_comment_line, false);
            CircleImageView fragment_comment_line_imgv = view.findViewById(R.id.item_comment_imgv);
            TextView fragment_comment_line_author = view.findViewById(R.id.item_comment_author);
            TextView fragment_comment_line_content = view.findViewById(R.id.item_comment_content);
            TextView fragment_comment_line_time = view.findViewById(R.id.item_comment_time);
            Glide.with(getContext()).load(commentbean.author.avatar).apply(options).into(fragment_comment_line_imgv);
            fragment_comment_line_author.setText(commentbean.author.name);
            fragment_comment_line_content.setText(commentbean.content);
            fragment_comment_line_time.setText(commentbean.created_at);
            fragment_comment_line.addView(view);
        }
    }

}
