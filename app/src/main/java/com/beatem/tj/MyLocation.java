package com.beatem.tj;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mliljenberg on 12/04/16.
 */
public class MyLocation implements Parcelable{
    private float longditude,latitude;
    private String text,picpath,trip, direction, filter;
    private LatLng latlng;

    public MyLocation(float londitude ,float latitude,String trip,String text, String picpath, String direction, String filter){
        this.latitude = latitude;
        this.longditude = londitude;
        this.text =text;
        this.picpath = picpath;
        this.trip = trip;
        this.direction=direction;
        this.filter=filter;
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

    public String getDirection() {
        return direction;
    }

    public String getFilter(){
        return filter;
    }

    //-------Parcable delen-----------

    public MyLocation(Parcel in){

        this.longditude = in.readFloat();
        this.latitude = in.readFloat();
        this.trip = in.readString();
        this.text = in.readString();
        this.picpath = in.readString();

        latlng = new LatLng(latitude,longditude);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.latitude+"",
                this.longditude+"",
                this.trip,this.text,this.picpath});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };


}

