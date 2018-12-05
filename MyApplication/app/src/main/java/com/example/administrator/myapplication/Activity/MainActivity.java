package com.example.administrator.myapplication.Activity;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.myapplication.Fragment.FindFragment;
import com.example.administrator.myapplication.Fragment.HotFragment;
import com.example.administrator.myapplication.Fragment.MineFragment;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RESPONSE_GET = 0;
    public static final int RESPONSE_FAIL = 1;
    public static final String[] titleList = {"热映", "找片", "我的"};
    private ViewPager main_pager;
    private TabLayout main_tab;
    private List<Fragment> fragmentList;
    View statusBarView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_pager = findViewById(R.id.main_pager);
        main_tab = findViewById(R.id.main_tab);
        statusBarView = new View(getWindow().getContext());
        Fragment hotFragment = new HotFragment();
        Fragment findFragment = new FindFragment();
        Fragment mineFragment = new MineFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(hotFragment);
        fragmentList.add(findFragment);
        fragmentList.add(mineFragment);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        };
        main_pager.setAdapter(pagerAdapter);
        main_tab.setupWithViewPager(main_pager);
    }
}
