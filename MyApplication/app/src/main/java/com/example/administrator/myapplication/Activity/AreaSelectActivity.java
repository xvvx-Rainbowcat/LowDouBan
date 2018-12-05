package com.example.administrator.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Util.AreaBean;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AreaSelectActivity extends AppCompatActivity {
    public static final int RESULT_OK = 1;
    private StickyListHeadersListView activity_city_sticky;
    private ImageButton activity_city_back;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        activity_city_sticky = findViewById(R.id.activity_city_sticky);
        activity_city_back = findViewById(R.id.activity_city_back);

        activity_city_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                finish();
            }
        });
        String[] city = {
                "安庆|anqing", "阿泰勒|ataile", "安康|ankang",
                "阿克苏|akesu", "北京|beijing", "包头|baotou", "北海|beihai", "百色|baise", "保山|baoshan",
                "成都|chengdu", "长治|changzhi", "长春|changchun", "常州|changzhou", "昌都|changdu",
                "朝阳|chaoyang", "常德|changde", "长白山|changbaishan", "赤峰|chifeng", "长沙|changsha", "重庆|chongqing",
                "大同|datong", "大连|dalian", "达县|daxian", "东营|dongying", "大庆|daqing", "丹东|dandong",
                "大理|dali", "敦煌|dunhuang", "鄂尔多斯|eerduosi", "恩施|enshi",
                "福州|fuzhou", "阜阳|fuyang", "广州|guangzhou", "贵阳|guiyang",
                "合肥|hefei", "杭州|hangzhou", "青岛|qingdao", "南京|nanjing", "南昌|nanchang", "内蒙古|neimenggu",
                "上海|shanghai", "深圳|shenzhen", "苏州|shuzhou", "三亚|sanya", "天津|tianjin", "厦门|xiamen", "西安|xian", "西藏|xizang"};
        List<AreaBean> areaBeanList = new ArrayList<>();
        for (String temp : city) {
            String[] temparr = temp.split("\\|");
            AreaBean areaBean = new AreaBean(temparr[0], temparr[1]);
            areaBeanList.add(areaBean);
        }
        AreaAdapter areaAdapter = new AreaAdapter(this, AreaSelectActivity.this, areaBeanList);
        activity_city_sticky.setAdapter(areaAdapter);
    }

    private class AreaAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private AppCompatActivity activity;
        private List<AreaBean> areas;
        private LayoutInflater inflater;

        public AreaAdapter (AppCompatActivity activity, Context context, List<AreaBean> areas) {
            this.activity = activity;
            inflater = LayoutInflater.from(context);
            this.areas = areas;
        }

        class ItemHolder {
            TextView item_txv;
            public ItemHolder(View view){
                this.item_txv = view.findViewById(R.id.item_city_txv);
            }
        }

        class HeaderHolder {
            TextView header_txv;
            public HeaderHolder(View view){
                this.header_txv = view.findViewById(R.id.item_header_txv);
            }
        }

        @Override
        public View getHeaderView (int position, View convertView, ViewGroup parent) {
            HeaderHolder headerHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_header, parent, false);
                headerHolder = new HeaderHolder(convertView);
                convertView.setTag(headerHolder);
            } else {
                headerHolder = (HeaderHolder) convertView.getTag();
            }
            String headerText = areas.get(position).name_eng.charAt(0) + "";
            headerHolder.header_txv.setText(headerText);
            return convertView;
        }

        @Override
        public long getHeaderId (int position) {
            return areas.get(position).name_eng.charAt(0);
        }

        @Override
        public int getCount () {
            return areas.size();
        }

        @Override
        public Object getItem (int i) {
            return areas.get(i);
        }

        @Override
        public long getItemId (int i) {
            return i;
        }

        @Override
        public View getView (final int i, View view, ViewGroup viewGroup) {
            ItemHolder itemHolder;
            if (view == null) {
                view = inflater.inflate(R.layout.item_city, viewGroup, false);
                itemHolder = new ItemHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        Intent intent = new Intent();
                        intent.putExtra("City", areas.get(i).name_chi);
                        activity.setResult(1, intent);
                        activity.finish();
                    }
                });
                view.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) view.getTag();
            }
            String itemText = areas.get(i).name_chi + "";
            itemHolder.item_txv.setText(itemText);
            return view;
        }
    }
}
