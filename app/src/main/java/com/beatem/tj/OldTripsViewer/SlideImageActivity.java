package com.beatem.tj.OldTripsViewer;

/**
 * Created by JoelBuhrman on 16-04-27.
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.beatem.tj.R;

import java.util.ArrayList;

public class SlideImageActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private static final Integer[] IMAGES= {R.drawable.adventure,R.drawable.worldmap,R.drawable.adventure,R.drawable.worldmap,R.drawable.adventure,R.drawable.worldmap,R.drawable.adventure,R.drawable.worldmap};
    private static final String[] DATES= {"24 Apr 2011","4 Aug 2013","1 Nov 2015","1 Mar 2016","24 Apr 2011","4 Aug 2013","1 Nov 2015","1 Mar 2016"};
    private static final String[] CITIES= {"Malmö","Lund","Ängelholm","Smygehuk","Malmö","Lund","Ängelholm","Smygehuk"};
    private static final String[] DIRECTIONS= {"N","NO","SW","W","N","NO","SW","W"};
    private static final String[] DESCRIPTIONS= {"Martin suger","Ludde suger","Marcus suger","Joel är cool","Martin suger","Ludde suger","Marcus suger","Joel är cool"};

    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<String> DatesArray = new ArrayList<String>();
    private ArrayList<String> CitiesArray = new ArrayList<String>();
    private ArrayList<String> DirectionsArray = new ArrayList<String>();
    private ArrayList<String> DescriptionsArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.old_trip_viewer);

        init();
    }
    private void init() {

        /*
        Adda här från sql
         */
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        for(int i=0;i<CITIES.length;i++)
            CitiesArray.add(CITIES[i]);

        for(int i=0;i<DIRECTIONS.length;i++)
            DirectionsArray.add(DIRECTIONS[i]);
        for(int i=0;i<DATES.length;i++)
            DatesArray.add(DATES[i]);
        for(int i=0;i<DESCRIPTIONS.length;i++)
            DescriptionsArray.add(DESCRIPTIONS[i]);



        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlideImageAdapter(SlideImageActivity.this,ImagesArray,DatesArray,CitiesArray, DirectionsArray,DescriptionsArray, mPager, false, true, 0));
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(IMAGES.length);
        final float density = getResources().getDisplayMetrics().density;
        NUM_PAGES =IMAGES.length;
    }


    public ArrayList<Integer> getImagesArray(){
        return ImagesArray;
    }

    public ArrayList<String> getCitiesArray(){
        return CitiesArray;
    }
    public ArrayList<String> getDirectionsArray(){
        return DirectionsArray;
    }
    public ArrayList<String> getDatesArray(){
        return DatesArray;
    }
    public ArrayList<String> getDescriptionsArray(){
        return DescriptionsArray;
    }
}