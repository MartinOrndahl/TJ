package com.beatem.tj.Intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JoelBuhrman on 16-04-20.
 */

public class IntroSlide extends android.support.v4.app.Fragment {
    private int resource;


    public IntroSlide(int resource) {
        this.resource=resource;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(resource, container, false);
        return v;
    }
}