package com.example.administrator.myapplication.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.administrator.myapplication.Fragment.CastFragment;
import com.example.administrator.myapplication.Fragment.CommentAreaFragment;
import com.example.administrator.myapplication.Fragment.PhotoFragment;
import com.example.administrator.myapplication.JSON.DetailBean;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Util.HttpUtil;
import com.example.administrator.myapplication.Util.MyScrollView;
import com.google.gson.Gson;

import java.io.IOException;

public class MovieActicity extends AppCompatActivity implements View.OnClickListener {

    public static final int RESPONSE_GET = 0;
    public static final int RESPONSE_FAIL = 1;

    private Info photoInfo;
    private FragmentManager fragmentManager;
    private DetailBean detailBean;
    private String movieId;
    private ConstraintLayout activity_movie_toolbar;
    private ImageButton activity_movie_btn_back;
    private ImageButton activity_movie_btn_share;
    private TextView activity_movie_appbar_title;
    private ImageView activity_movie_appbar_popcorn;
    private MyScrollView activity_movie_scrollview;
    private ConstraintLayout activity_movie_cons_cover;
    private PhotoView activity_movie_imgv_cover;
    private TextView activity_movie_title;
    private TextView activity_movie_genres;
    private TextView activity_movie_orininal_name;
    private TextView activity_movie_time;
    private TextView activity_movie_length;
    private TextView activity_movie_rating;
    private RatingBar activity_movie_ratingbar;
    private TextView activity_movie_sum;
    private ConstraintLayout activity_movie_btn_want;
    private ConstraintLayout activity_movie_btn_seen;
    private ConstraintLayout activity_movie_btn_seat;
    private TextView activity_movie_discription;
    private CastFragment fragment_casts;
    private PhotoFragment fragment_photo;
    private CommentAreaFragment fragment_comment_area;
    private ImageView activity_movie_curtain;
    private ProgressBar activity_movie_progressbar;
    public PhotoView activity_movie_enlarge;
    private PhotoView tempPhotoView;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage (Message msg) {
            switch (msg.what) {
                case RESPONSE_GET: {
                    detailBean = (DetailBean) msg.obj;
                    setView();
                    break;
                }
                case RESPONSE_FAIL: {
                    Toast.makeText(MovieActicity.this, "获取电影信息失败", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    });


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_sec);
        iniView();

        activity_movie_scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange (View view, int left, int top, int oldl, int oldt) {
                if(top <= 1000){
                    activity_movie_toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransMovie,null));
                }else if(top > 1000){
                    activity_movie_toolbar.setBackgroundColor(getResources().getColor(R.color.colorMovie,null));
                }
            }
        });
        activity_movie_enlarge.enable();
        activity_movie_enlarge.setScaleType(ImageView.ScaleType.FIT_CENTER);
        activity_movie_enlarge.setMaxScale(4);
        activity_movie_enlarge.setOnClickListener(this);
        activity_movie_imgv_cover.setOnClickListener(this);
        Intent intent = getIntent();
        movieId = intent.getStringExtra("MovieId");
        activity_movie_btn_back.setOnClickListener(this);
        activity_movie_btn_share.setOnClickListener(this);
        getDataFromServer();
    }

    public void prepareEnlarge (Info photoInfo,PhotoView tempPhotoView) {
        this.photoInfo = photoInfo;
        this.tempPhotoView = tempPhotoView;
    }

    @Override
    public void onBackPressed () {
        if(activity_movie_enlarge.getVisibility() == View.VISIBLE){
            activity_movie_enlarge.animaTo(photoInfo, new Runnable() {
                @Override
                public void run () {
                    activity_movie_enlarge.setVisibility(View.GONE);
                    tempPhotoView.setVisibility(View.VISIBLE);
                }
            });
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.activity_movie_btn_back: {
                finish();
                break;
            }
            case R.id.activity_movie_btn_share: {
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.activity_movie_enlarge: {
                activity_movie_enlarge.animaTo(photoInfo, new Runnable() {
                    @Override
                    public void run () {
                        activity_movie_enlarge.setVisibility(View.GONE);
                        tempPhotoView.setVisibility(View.VISIBLE);
                    }
                });
                break;
            }
            case R.id.activity_movie_imgv_cover: {
                tempPhotoView = activity_movie_imgv_cover;
                tempPhotoView.setVisibility(View.GONE);
                activity_movie_enlarge.setVisibility(View.VISIBLE);
                Glide.with(this).load(detailBean.images.large).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady (@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        activity_movie_enlarge.setImageDrawable(resource);
                        photoInfo = tempPhotoView.getInfo();
                        activity_movie_enlarge.animaFrom(photoInfo);
                    }
                });
                break;
            }
        }
    }

    private void getDataFromServer () {
        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    Gson gson = new Gson();
                    String url = "http://api.douban.com/v2/movie/subject/" + movieId + "?apikey=0b2bdeda43b5688921839c8ecb20399b";
                    String jsonstring = HttpUtil.getResponseFromServer(url);
                    DetailBean detailBean = gson.fromJson(jsonstring, DetailBean.class);
                    sendMsg(RESPONSE_GET, detailBean);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMsg(RESPONSE_FAIL, null);
                }
            }
        }).start();
    }

    private void setView(){
        activity_movie_appbar_title.setText(detailBean.title);
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(MovieActicity.this).load(detailBean.images.large).apply(options).into(activity_movie_imgv_cover);
        activity_movie_title.setText(detailBean.title);
        StringBuilder builder = new StringBuilder();
        builder.append(detailBean.year + " / ");
        for (String temp : detailBean.genres) {
            builder.append(temp + " / ");
        }
        builder.delete(builder.length() - 3, builder.length() - 1);
        activity_movie_genres.setText(builder.toString());
        activity_movie_orininal_name.setText("原名: " + detailBean.original_title);
        builder = new StringBuilder();
        builder.append("上映时间: ");
        for (String temp : detailBean.pubdates) {
            builder.append(temp + "\n");
            builder.append("                  ");
        }
        builder.delete(builder.length() - 19, builder.length() - 1);
        activity_movie_time.setText(builder.toString());

        builder = new StringBuilder();
        builder.append("片长: ");
        for (String temp : detailBean.durations) {
            builder.append(temp + " / ");
        }
        builder.delete(builder.length() - 3, builder.length() - 1);
        activity_movie_length.setText(builder.toString());
        activity_movie_rating.setText(detailBean.rating.average + "");
        activity_movie_ratingbar.setRating((float) detailBean.rating.average / 2);
        activity_movie_sum.setText(detailBean.collect_count + "人");
        activity_movie_discription.setText("    " + detailBean.summary);
        fragment_casts.prepareRecycler(detailBean.casts);
        fragment_photo.prepareRecycler(detailBean.photos);
        fragment_comment_area.prepareComment(detailBean);
        activity_movie_curtain.setVisibility(View.GONE);
        activity_movie_progressbar.setVisibility(View.GONE);
    }

    private void iniView () {
        fragmentManager = getSupportFragmentManager();
        activity_movie_toolbar = findViewById(R.id.activity_movie_toolbar);
        activity_movie_btn_back = findViewById(R.id.activity_movie_btn_back);
        activity_movie_btn_share = findViewById(R.id.activity_movie_btn_share);
        activity_movie_appbar_title = findViewById(R.id.activity_movie_appbar_title);
        activity_movie_appbar_popcorn = findViewById(R.id.activity_movie_appbar_popcorn);
        activity_movie_scrollview = findViewById(R.id.activity_movie_scrollview);
        activity_movie_cons_cover = findViewById(R.id.activity_cons_cover);
        activity_movie_imgv_cover = findViewById(R.id.activity_movie_imgv_cover);
        activity_movie_title = findViewById(R.id.activity_movie_title);
        activity_movie_genres = findViewById(R.id.activity_movie_genres);
        activity_movie_orininal_name = findViewById(R.id.activity_movie_original_name);
        activity_movie_time = findViewById(R.id.activity_movie_time);
        activity_movie_length = findViewById(R.id.activity_movie_length);
        activity_movie_rating = findViewById(R.id.activity_movie_rating);
        activity_movie_ratingbar = findViewById(R.id.activity_movie_ratingbar);
        activity_movie_sum = findViewById(R.id.activity_movie_sum);
        activity_movie_btn_want = findViewById(R.id.activity_movie_btn_want);
        activity_movie_btn_seen = findViewById(R.id.activity_movie_btn_seen);
        activity_movie_btn_seat = findViewById(R.id.activity_movie_btn_seat);
        activity_movie_discription = findViewById(R.id.activity_movie_discription);
        fragment_casts = (CastFragment) fragmentManager.findFragmentById(R.id.fragment_casts);
        fragment_photo = (PhotoFragment) fragmentManager.findFragmentById(R.id.fragment_photo);
        fragment_comment_area = (CommentAreaFragment) fragmentManager.findFragmentById(R.id.fragment_comment_area);
        activity_movie_curtain = findViewById(R.id.activity_movie_curtain);
        activity_movie_progressbar = findViewById(R.id.activity_movie_progressbar);
        activity_movie_enlarge = findViewById(R.id.activity_movie_enlarge);
    }

    private void sendMsg (int what, Object object) {
        Message message = new Message();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);
    }
}
