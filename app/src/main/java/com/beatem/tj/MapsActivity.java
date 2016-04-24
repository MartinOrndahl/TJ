package com.beatem.tj;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.beatem.tj.CameraAlt2.ActivityCamera;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener,NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng currentlocation;
    private boolean mMapready, permission, zoomEnabled, galleryCreated;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 36;
    private PolygonOptions polygon;
    private ArrayList<MyLocation> locations;
    private LocationListener locationListener;
    private SensorManager mSensorManager;
    private boolean moveWithSensor = true;
    private GalleryFragment galleryFragment;
    private SupportMapFragment mapFragment;
    private MapFragment mapFragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zoomEnabled = false;
        permission = false;

        polygon = new PolygonOptions().geodesic(true);
        if(!SaveSharedPreferences.getFirstStart(getApplicationContext())){
            //TODO:lägg till en trip med bilder i databasen.
            MySqLite db = new MySqLite(getApplicationContext());
            db.addLocation(new MyLocation(10,20,"Australien","test","bildpath här"));
            db.addLocation(new MyLocation(10,25,"Australien","test","bildpath här"));
            db.addLocation(new MyLocation(11,26,"Australien","test","bildpath här"));
            db.addLocation(new MyLocation(15,18,"Australien","test","bildpath här"));
            SaveSharedPreferences.setStartBefore(getApplicationContext(),true);
        }
        getTrips();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        setContentView(R.layout.activity_nav_drawer);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
            locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                currentlocation = new LatLng(location.getLatitude(), location.getLongitude());
                //uppdateCurrentLocation();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



// Register the listener with the Location Manager to receive location updates

        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        currentlocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        GpsListniner(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       galleryFragment = new GalleryFragment();



    }
    public void getTrips(){
        MySqLite db = new MySqLite(getApplicationContext());
        locations = db.getLocations();
    }

    public synchronized void GpsListniner(boolean onoff) {
        try {
        if (onoff && locationManager != null) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }else{
            //TODO:sluta lyssna efter gps signal.
        }
        }catch(SecurityException e){

        }
    }

    public synchronized void uppdateCurrentLocation() {
        Log.i("test1","vi kom hit");
        if (currentlocation != null && mMapready) {
            try {
                mMap.setMyLocationEnabled(true);




            } catch (SecurityException e) {
                e.printStackTrace();


            }
            //TODO:Fixa
           /**
            mMap.addPolyline(new PolylineOptions().addAll(locations));
            for (LatLng l : locations) {

                mMap.addMarker(new MarkerOptions().position(l));

            }
            **/

        }
    }

    public synchronized void addLocationMarker(LatLng pos) {
    //TODO:fixa, alternativt ta bort.
        getTrips();
    mMap.addMarker(new MarkerOptions().position(pos));


    }

    public synchronized void uppdateMap() {
        if (mMapready) {

            mMap.clear();
            ArrayList<LatLng> polylist = new ArrayList<LatLng>();
            for (MyLocation location : locations) {
                mMap.addMarker(new MarkerOptions().position(location.getLatlng()));
                polylist.add(location.getLatlng());
            }
            mMap.addPolyline(new PolylineOptions().addAll(polylist));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission = true;

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void worldMapmode(){
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        zoomEnabled = true;
        //Todo: hämta alla markeringar från SQLLite databas och sätt ut på karta
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0,0)));


    }
    public void currentTripMode(){
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        zoomEnabled = false;
        moveToCurrentTrip();
    }

    public void moveToCurrentTrip(){
        int lat = 0;
       double largestLat = 0;
        double smalestLat = 0;
        double largestLong = 0;
        double smalestLong = 0;
        int log = 0;
        double largestlog;
        //TODO: Få denna att faktist fungera....
        if (locations!=null&&locations.size()>0) {
            largestLat = locations.get(0).getLatitude();
            smalestLat = locations.get(0).getLatitude();
            smalestLong = locations.get(0).getLongditude();
            largestLong = locations.get(0).getLongditude();

            for (int i = 0; i < locations.size(); i++) {
                if (largestLat<locations.get(i).getLatitude()){largestLat = locations.get(i).getLatitude();}
                if (smalestLat>locations.get(i).getLatitude()){ smalestLat = locations.get(i).getLatitude();}
                if (largestLong<locations.get(i).getLongditude()){ largestLong = locations.get(i).getLongditude();}
                if (smalestLong>locations.get(i).getLongditude()){ smalestLong = locations.get(i).getLongditude();}

                lat += locations.get(i).getLatitude();
                log += locations.get(i).getLongditude();
            }

            LatLngBounds bounds = new LatLngBounds(new LatLng(smalestLat,smalestLong),new LatLng(largestLat,largestLong));

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,300));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(bounds.getCenter()));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Toast.makeText(getApplicationContext(), "Yey detta fungerade", Toast.LENGTH_SHORT).show();
                //TODO:Implementera vad som händer när man klickar på en marker
                for (MyLocation location : locations) {
                    if (marker.getPosition().equals(location.getLatlng())) {
                        //TODO: öppna bilden.


                    }
                    return true;
                }
                return false;
            }


        });

        mMapready = true;

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                uppdateMap();
                uppdateCurrentLocation();
                worldMapmode();

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        if(mSensorManager != null) {
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        if(mSensorManager!=null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        if(zoomEnabled) {
            float x = Math.round(event.values[0]);
            float y = Math.round(event.values[1]);
            double z = Math.round(event.values[2]);
            if (mMap != null) {
                //mMap.moveCamera(CameraUpdateFactory.scrollBy(-x*3, y*3));
                if (x > 2 || x < -2) {
                    mMap.moveCamera(CameraUpdateFactory.zoomBy(-x / 70));
                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.current_trip_button) {

            if(galleryCreated) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(galleryFragment);
                fragmentTransaction.commit();
                galleryCreated = false;
            }
            currentTripMode();


        } else if (id == R.id.my_trips_button) {

            galleryCreated = true;
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, galleryFragment);
            fragmentTransaction.commit();



        } else if (id == R.id.map_button) {

            worldMapmode();
            if(galleryCreated) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(galleryFragment);
                fragmentTransaction.commit();
                galleryCreated = false;
            }



        } else if (id == R.id.end_trip_button) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setIcon(getDrawable(R.drawable.worldmap));
            builder.setMessage("Are you sure you want to end this trip?");

            builder.setNegativeButton("END", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    dialog.dismiss();
                }

            });



            AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void startCameraActivity(View view){
        startActivity(new Intent(this, ActivityCamera.class).putExtra("camType", "back"));

    }
}

