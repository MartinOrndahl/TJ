package com.beatem.tj.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.beatem.tj.CapturedImage.ImageViewingActivity;
import com.beatem.tj.MapsActivity;
import com.beatem.tj.R;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by JoelBuhrman on 16-04-20.
 */
public class MyIntro extends AppIntro {
    final String PREFS_NAME = "MyPrefsFile";


    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        //checkIfFirstTimeUsingApp();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new IntroSlide(R.layout.intro1));
        addSlide(new IntroSlide(R.layout.intro2));
        addSlide(new VideoSlide(R.layout.intro3));
        addSlide(new IntroSlide(R.layout.intro4));
        /*

        addSlide(third_fragment);
        addSlide(fourth_fragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("titel", "beskrivning", R.drawable.vector_drawable_ic_filter_black___px, Color.BLUE));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#698443"));
        setSeparatorColor(Color.parseColor("#FF43542A"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(false);
        setVibrateIntensity(30);



    }

    public void doneWithIntroClicked(View view){
        startActivity(new Intent(this, ImageViewingActivity.class));
        finish();
    }

    private void checkIfFirstTimeUsingApp() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do nothing
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        else{
            startActivity(new Intent(this, MapsActivity.class));
        }
    }

    @Override
    public void onSkipPressed() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}