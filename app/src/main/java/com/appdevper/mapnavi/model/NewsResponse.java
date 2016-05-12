package com.appdevper.mapnavi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by worawit on 3/6/16.
 */
public class NewsResponse {
    @SerializedName("news")
    public List<News> news = null;
}
