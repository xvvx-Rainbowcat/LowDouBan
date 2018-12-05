package com.example.administrator.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.JSON.DetailBean;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAreaFragment extends Fragment {

    public static final String[] titleList = {"评论区", "短评区"};
    private TabLayout fragment_comment_area_tab;
    private ViewPager fragment_comment_area_viewpager;
    private FragmentManager fragmentManager;
    private CommentFragment fragment_comment;
    private ReviewFragment fragment_review;
    private List<Fragment> fragmentList;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_comment_area, container, false);
        fragmentManager = getChildFragmentManager();
        fragmentList = new ArrayList<>();
        fragment_comment_area_tab = view.findViewById(R.id.fragment_comment_area_tab);
        fragment_comment_area_viewpager = view.findViewById(R.id.fragment_comment_area_viewpager);
        fragment_comment = new CommentFragment();
        fragment_review = new ReviewFragment();
        fragmentList.add(fragment_comment);
        fragmentList.add(fragment_review);
        fragment_comment_area_viewpager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem (int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount () {
                return fragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle (int position) {
                return titleList[position];
            }
        });
        fragment_comment_area_tab.setupWithViewPager(fragment_comment_area_viewpager);
        return view;
    }

    public void prepareComment (DetailBean detailBean) {
        fragment_comment.prepareComment(detailBean.popular_comments);
        fragment_review.prePareReview(detailBean.popular_reviews);
    }
}
