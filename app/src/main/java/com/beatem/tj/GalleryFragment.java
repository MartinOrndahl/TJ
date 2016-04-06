package com.beatem.tj;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    static final String MEDIA_DIRECTORY = "TravelJournal";
    private static final int READ_REQUEST_CODE = 42;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    private void readPictureDirectory(){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

}
