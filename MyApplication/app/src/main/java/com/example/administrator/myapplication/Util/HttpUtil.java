package com.example.administrator.myapplication.Util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public static String getResponseFromServer(String url) throws IOException {
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        response = client.newCall(request).execute();
        if(response!=null){
            return response.body().string();
        }
        return null;
    }

}
