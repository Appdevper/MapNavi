package com.appdevper.mapnavi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by worawit on 3/6/16.
 */
public class PlaceResponse {
    @SerializedName("placeList")
    public List <Place> placeList = null;
}
