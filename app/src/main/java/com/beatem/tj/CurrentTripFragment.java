package com.beatem.tj;

import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by JoelBuhrman on 16-05-16.
 */
public class CurrentTripFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.current_trip_fragment, container, false);
        new testAsync((TextView) view.findViewById(R.id.current_trip_fragment_picNumber), (TextView) view.findViewById(R.id.current_trip_fragment_daysNumber)).execute();

        return view;
    }

    public class testAsync extends AsyncTask<Void, Void, Void> {
        MySqLite mySqLite;
        ArrayList<MyLocation> locations = new ArrayList<MyLocation>();
        ArrayList<String> trips = new ArrayList<String>();
        TextView picNumber, dayNumber;
        int daysTraveled;
        Geocoder geoCoder;


        public testAsync(TextView pic, TextView days) {
            this.picNumber = pic;
            this.dayNumber = days;
            mySqLite = new MySqLite(getContext());
            geoCoder = new Geocoder(getContext(), Locale.getDefault());

        }

        @Override
        protected void onPreExecute() {
            locations = mySqLite.getLocations();
            getTrips();
            getDays();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dayNumber.setText(daysTraveled + "");
            picNumber.setText(locations.size() + "");
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        public void getTrips() {
            for (MyLocation location : locations) {
                if (!trips.contains(location.getTrip())) {
                    trips.add(location.getTrip());
                }
            }

        }

        public void getDays() {
            daysTraveled = 0;

            MyLocation firstLocation = locations.get(0);
            MyLocation lastLocation = locations.get(0);
            for (int i = 1; i < locations.size(); i++) {
                if (!locations.get(i).getTrip().equals(firstLocation.getTrip())) { //Om inte tillhör samma resa
                    lastLocation = locations.get(i - 1);

                    addToDays(firstLocation, lastLocation);
                    firstLocation = locations.get(i);
                }

            }
            daysTraveled++;
            daysTraveled+=22; //PGA fel datumordning i mapsactivity

        }

        private void addToDays(MyLocation first, MyLocation last) {
            String[] firstDate = first.getDate().split(" ");
            String[] lastDate = last.getDate().split(" ");
            int nmbrOfDays = 0;

            nmbrOfDays += 365 * (Integer.parseInt(lastDate[2]) - Integer.parseInt(firstDate[2]));
            nmbrOfDays += 30 * (getMonthValue(lastDate[1]) - getMonthValue(firstDate[1]));
            nmbrOfDays += Integer.parseInt(lastDate[0]) - Integer.parseInt(firstDate[0]);

            daysTraveled += nmbrOfDays;


        }

        private int getMonthValue(String s) {
            switch (s) {
                case "Jan":
                    return 1;
                case "Feb":
                    return 2;
                case "Mar":
                    return 3;
                case "Apr":
                    return 4;
                case "May":
                    return 5;
                case "Jun":
                    return 6;
                case "Jul":
                    return 7;
                case "Aug":
                    return 8;
                case "Sep":
                    return 9;
                case "Oct":
                    return 10;
                case "Nov":
                    return 11;
                case "Dec":
                    return 12;

                default:
                    return 0;

            }

        }


    }

}
