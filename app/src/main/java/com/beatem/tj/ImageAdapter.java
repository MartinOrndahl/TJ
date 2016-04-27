package com.beatem.tj;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ludvig on 2016-04-12.
 */
public class ImageAdapter extends BaseAdapter
{
    private Context context;
    private Bitmap[] images;

    public ImageAdapter(Context c, Bitmap[] images)
    {
        context = c;
        this.images = images;
    }

    //---returns the number of images---
    public int getCount() {
        return images.length;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(16, 16, 16, 16);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(images[position]);
        return imageView;
    }
}