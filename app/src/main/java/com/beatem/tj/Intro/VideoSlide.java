package com.beatem.tj.Intro;

/**
 * Created by JoelBuhrman on 16-04-20.
 */

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.beatem.tj.R;


/**
 * Created by JoelBuhrman on 16-04-20.
 */

public class VideoSlide extends android.support.v4.app.Fragment {
    private int resource;


    public VideoSlide(int resource) {
        this.resource = resource;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(resource, container, false);


        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
//displays a video file
        VideoView mVideoView2 = (VideoView) v.findViewById(R.id.videoView1);
        mVideoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(0, 0);
                mp.setLooping(true);
            }
        });

        String uriPath2 = "android.resource://com.beatem.tj/" + R.raw.video;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();


        return v;


    }
}