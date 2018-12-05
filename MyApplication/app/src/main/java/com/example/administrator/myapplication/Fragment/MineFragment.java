package com.example.administrator.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;

public class MineFragment extends Fragment {

    public static final String[] TAGS = {"讨论", "想看", "在看", "看过", "影评", "影人"};
    private ViewPager fragment_mine_pager;
    private TabLayout fragment_mine_tab;
    ArrayList<Fragment> fragmentList;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        fragment_mine_pager = view.findViewById(R.id.fragment_mine_pager);
        fragment_mine_tab = view.findViewById(R.id.fragment_mine_tab);
        fragmentList = new ArrayList<>();
        for (int i = 0; i < TAGS.length; i++) {
            fragmentList.add(new MineItemFragment());
        }
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
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
                return TAGS[position];
            }
        };
        fragment_mine_pager.setAdapter(pagerAdapter);
        fragment_mine_tab.setupWithViewPager(fragment_mine_pager);
        return view;
    }
}
