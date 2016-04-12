package com.beatem.tj;


import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static GridView gridView;
    private int[]image={R.drawable.vector_drawable_ic_map_black___px, R.drawable.vector_drawable_ic_cancel_black___px};

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridView = (GridView) view.findViewById(R.id.view_gallery);
        ImageAdapter adapter = new ImageAdapter(getContext(), getImages());

        gridView.setAdapter(adapter);


        // Inflate the layout for this fragment
        return view;
    }


    private ArrayList<Images> getImages(){
        ArrayList<Images> images = new ArrayList<>();
        images.add(new Images(image[0]));
        images.add(new Images(image[1]));


        return images;
    }

}
