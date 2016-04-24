package com.beatem.tj;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mliljenberg on 12/04/16.
 */
public class MyLocation {
    private float longditude,latitude;
    private String text,picpath,trip;
    private LatLng latlng;

    public MyLocation(float londitude ,float latitude,String trip,String text, String picpath){
        this.latitude = latitude;
        this.longditude = londitude;
        this.text =text;
        this.picpath = picpath;
        this.trip = trip;
        latlng = new LatLng(latitude,londitude);

    }
    public float getLongditude() {
        return longditude;
    }

    public String getPicpath() {
        return picpath;
    }

    public String getText() {
        return text;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getTrip() {
        return trip;
    }

    public LatLng getLatlng() {
        return latlng;
    }
}
