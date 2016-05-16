package com.beatem.tj.MyTrips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.beatem.tj.MyLocation;
import com.beatem.tj.MySqLite;
import com.beatem.tj.R;

import java.util.ArrayList;

/**
 * Created by JoelBuhrman on 16-05-07.
 */
public class ChosenTripFragment extends Fragment {
    private static GridView gridView2;
    private MySqLite mySqlLite;
    private ArrayList<MyLocation> locations;
    private ArrayList<String> paths;
    private String trip;

    public ChosenTripFragment(String trip) {
        this.trip=trip;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.my_trips_chosen_trip, container, false);
        gridView2= (GridView)view.findViewById(R.id.my_trip_chosen_trip_grid);
        paths = new ArrayList<String>();
        mySqlLite = new MySqLite(getActivity().getApplicationContext());
        locations = mySqlLite.getLocations();

        for(MyLocation location: locations){
            if(location.getTrip().equals(trip)){
                paths.add(location.getPicpath());
            }
        }




        gridView2.setAdapter(new CustomChosenTripAdapter(getActivity().getApplicationContext(), paths.toArray(new String[0])));




        return view;
    }


}
