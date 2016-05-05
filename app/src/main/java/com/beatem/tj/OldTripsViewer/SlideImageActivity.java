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
    private ArrayList<String> FiltersArray = new ArrayList<String>();

    private MyLocation location;
    private MySqLite mySqLite;
    private int startIndex;


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

        String chosenTrip = location.getTrip();
        ArrayList<MyLocation> locations = mySqLite.getLocations();
        ArrayList<MyLocation> chosenTripLocations = new ArrayList<MyLocation>();

        for (MyLocation location : locations) {
            if (location.getTrip().equals(chosenTrip)) {
                chosenTripLocations.add(location);
            }
        }
        int i = 0;
        for (MyLocation loc : chosenTripLocations) {
            if (loc.getLongditude() == location.getLongditude() && loc.getLatitude() == location.getLatitude()) {
                startIndex = i;
            }
            ImagesArray.add(loc.getPicpath());
            CitiesArray.add(getCity(loc.getLatitude(), loc.getLongditude()));
            DirectionsArray.add(loc.getDirection());
            DatesArray.add(loc.getDate());
            DescriptionsArray.add(loc.getText());
            FiltersArray.add(loc.getFilter());
            i++;
        }


        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlideImageAdapter2(SlideImageActivity.this, ImagesArray, DatesArray, CitiesArray, DirectionsArray, DescriptionsArray, FiltersArray, mPager, false, true, 0));
        mPager.setCurrentItem(startIndex);
        NUM_PAGES = ImagesArray.size();
    }

    public String getCity(float latitude, float longitude) {


        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            return addresses.get(0).getLocality();
        }
        return "";
    }


}