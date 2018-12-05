package com.example.administrator.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.Activity.AreaSelectActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends Fragment {
    public static final int RESULT_OK = 1;
    public static final String[] titleList = {"正在热映", "即将上映"};
    private ViewPager hot_pager;
    private TabLayout hot_tab;
    private TextView hot_city;
    private ConstraintLayout hot_area_select;
    private ConstraintLayout hot_search;
    private List<Fragment> fragmentList;
    private HotOnFragment hotOnfragment;
    private HotLaterFragment hotLaterFragment;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hot, container, false);
        hot_pager = view.findViewById(R.id.hot_pager);
        hot_tab = view.findViewById(R.id.hot_tab);
        hot_city = view.findViewById(R.id.hot_city);
        hot_area_select = view.findViewById(R.id.hot_area_select);
        hot_search = view.findViewById(R.id.fragment_find_search);
        hot_area_select = view.findViewById(R.id.hot_area_select);

        hot_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        fragmentList = new ArrayList<>();
        hotOnfragment = new HotOnFragment();
        hotLaterFragment = new HotLaterFragment();
        fragmentList.add(hotOnfragment);
        fragmentList.add(hotLaterFragment);
        hot_area_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getContext(), AreaSelectActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount () {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem (int i) {
                return fragmentList.get(i);
            }


            @Nullable
            @Override
            public CharSequence getPageTitle (int position) {
                return titleList[position];
            }
        };
        hot_pager.setAdapter(fragmentPagerAdapter);
        hot_tab.setupWithViewPager(hot_pager);
        return view;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String city;
            city = data.getStringExtra("City");
            hot_city.setText(city);
            hotOnfragment.onCityChange(city);
        }
    }
}
