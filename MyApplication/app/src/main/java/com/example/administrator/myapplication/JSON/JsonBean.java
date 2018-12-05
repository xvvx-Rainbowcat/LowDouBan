package com.example.administrator.myapplication.JSON;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonBean {
    public int count;
    public int start;
    public int total;
    @SerializedName("subjects")
    public List<MovieBean> movies;
    public String title;
}
