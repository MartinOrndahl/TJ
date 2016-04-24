package com.beatem.tj;

/**
 * Created by Ludvig on 2016-04-12.
 */
public class Images {
    private int img;
    private String name;

    public Images(int img, String name){
        this.img=img;
        this.name=name;
    }
    public int getImage(){
        return img;
    }
    public void setImg(int img){
        this.img = img;
    }
    public String getName(){
        return name;
    }
}
