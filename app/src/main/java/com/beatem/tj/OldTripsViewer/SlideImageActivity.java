package com.beatem.tj.OldTripsViewer;

/**
 * Created by JoelBuhrman on 16-04-27.
 */

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.beatem.tj.MyLocation;
import com.beatem.tj.MySqLite;
import com.beatem.tj.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SlideImageActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private static final Integer[] IMAGES = {R.drawable.adventure, R.drawable.worldmap, R.drawable.adventure, R.drawable.worldmap, R.drawable.adventure, R.drawable.worldmap, R.drawable.adventure, R.drawable.worldmap};
    private static final String[] DATES = {"24 Apr 2011", "4 Aug 2013", "1 Nov 2015", "1 Mar 2016", "24 Apr 2011", "4 Aug 2013", "1 Nov 2015", "1 Mar 2016"};
    private static final String[] CITIES = {"Malmö", "Lund", "Ängelholm", "Smygehuk", "Malmö", "Lund", "Ängelholm", "Smygehuk"};
    private static final String[] DIRECTIONS = {"N", "NO", "SW", "W", "N", "NO", "SW", "W"};
    private static final String[] DESCRIPTIONS = {"Martin suger", "Ludde suger", "Marcus suger", "Joel är cool", "Martin suger", "Ludde suger", "Marcus suger", "Joel är cool"};

    private ArrayList<String> ImagesArray = new ArrayList<>();
    private ArrayList<String> DatesArray = new ArrayList<String>();
    private ArrayList<String> CitiesArray = new ArrayList<String>();
    private ArrayList<String> DirectionsArray = new ArrayList<String>();
    private ArrayList<String> DescriptionsArray = new ArrayList<String>();

    private MyLocation location;
    private MySqLite mySqLite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.old_trip_viewer);
        init();
    }

    private void init() {
//push
        mySqLite = new MySqLite(this);
/*

Data från kartan
 */
        Bundle data = getIntent().getExtras();
        location = (MyLocation) data.getParcelable("location");

        ImagesArray.add(location.getPicpath());
        CitiesArray.add(getCity(location.getLatitude(), location.getLongditude()));
        DirectionsArray.add(location.getDirection());
        DatesArray.add(location.getDate());
        DescriptionsArray.add(location.getText());



        /*
        ArrayList<MyLocation> locations = mySqLite.getLocations();
        for (int i = 4; i < locations.size(); i++) {
            ImagesArray.add(locations.get(i).getPicpath());
            CitiesArray.add(getCity(locations.get(i).getLatitude(), locations.get(i).getLongditude()));
            DirectionsArray.add(locations.get(i).getDirection());
            DatesArray.add(locations.get(i).getDate());
            DescriptionsArray.add(locations.get(i).getText());
            FiltersArray.add(locations.get(i).getFilter());
        }*/





        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlideImageAdapter2(SlideImageActivity.this, ImagesArray, DatesArray, CitiesArray, DirectionsArray, DescriptionsArray, mPager, false, true, 0));
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(IMAGES.length);
        final float density = getResources().getDisplayMetrics().density;
        NUM_PAGES = ImagesArray.size();
    }

    public String getCity(float latitude, float longitude) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String[] total = new String[]{};
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString(); //This is the complete address.
            total = fnialAddress.split(" ");

        } catch (IOException e) {
        } catch (NullPointerException e) {
        }

        return total[total.length - 1];
    }

    public ArrayList<String> getImagesArray() {
        return ImagesArray;
    }

    public ArrayList<String> getCitiesArray() {
        return CitiesArray;
    }

    public ArrayList<String> getDirectionsArray() {
        return DirectionsArray;
    }

    public ArrayList<String> getDatesArray() {
        return DatesArray;
    }

    public ArrayList<String> getDescriptionsArray() {
        return DescriptionsArray;
    }



}