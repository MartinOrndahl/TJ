package com.beatem.tj;

import java.util.ArrayList;

/**
 * Created by Ludvig on 2016-04-21.
 */
public class Album {
    private String name;
    private ArrayList<Integer> images;

    public Album(String name){
        this.name = name;
        images= new ArrayList();

    }

    public void addImages(String path) {
        int number = Integer.parseInt(path);
        images.add(number);
    }

    public int getImage(int pos){
        return images.get(pos);
    }

}
