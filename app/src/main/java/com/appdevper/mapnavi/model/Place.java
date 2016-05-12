package com.appdevper.mapnavi.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by worawit on 2/18/16.
 */
public class Place {
    @SerializedName("map_name")
    public String name = "";
    @SerializedName("map_Detail")
    public String detail = "";
    @SerializedName("map_LAT")
    public double latitude = 0;
    @SerializedName("map_LNG")
    public double longitude = 0;
    @SerializedName("map_Type")
    public String type = "";
    public boolean isCheck = false;

    public Place(String name, String detail, double latitude, double longitude, String type) {
        this.name = name;
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    public String getType() {
        return type;
    }
}
