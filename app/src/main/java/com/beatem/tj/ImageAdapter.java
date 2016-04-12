package com.beatem.tj;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Ludvig on 2016-04-12.
 */
public class ImageAdapter extends BaseAdapter {
    Context c;
    ArrayList<Images>images;

    public ImageAdapter(Context c, ArrayList<Images> images){

        this.c =c;
        this.images=images;

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater infaltor = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView=infaltor.inflate(R.layout.gallery_model, parent, false);

        }

        ImageView img = (ImageView)convertView.findViewById(R.id.image_model);
        img.setImageResource(images.get(position).getImage());


        return convertView;
    }
}