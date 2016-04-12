package com.beatem.tj;

/**
 * Created by mliljenberg on 12/04/16.
 */
public class Location {
    private float longditude,latitude;
    private String text,picpath;

    public Location(float londitude ,float latitude,String text,String picpath){
        this.latitude = latitude;
        this.longditude = londitude;
        this.text =text;
        this.picpath = picpath;

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
}
