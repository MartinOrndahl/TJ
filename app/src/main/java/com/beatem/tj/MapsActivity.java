package com.beatem.tj;

import android.Manifest;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.beatem.tj.Camera.CameraActivity;
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
    private boolean mMapready, permission, zoomEnabled;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 36;
    private PolygonOptions polygon;
    private ArrayList<LatLng> locations;
    private LocationListener locationListener;
    private SensorManager mSensorManager;
    private boolean moveWithSensor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zoomEnabled = false;
        permission = false;
        locations = new ArrayList<LatLng>();
        polygon = new PolygonOptions().geodesic(true);

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


        //setContentView(R.layout.activity_nav_drawer);

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


        locations.add(new LatLng(-34, 151));
        locations.add(new LatLng(-34, 147));
        locations.add(new LatLng(-37, 130));
        locations.add(new LatLng(-20, 120));

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



// Register the listener with the Location Manager to receive location updates

        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        currentlocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        GpsListniner(true);

        //Initierar main fragment
        /**
        MapFragment mapFragment1 = new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment1);
        fragmentTransaction.commit();
         **/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        /**
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        }
        });
         **/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




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
            mMap.addPolyline(new PolylineOptions().addAll(locations));
            for (LatLng l : locations) {

                mMap.addMarker(new MarkerOptions().position(l));

            }

        }
    }

    public synchronized void addLocationMarker(LatLng pos) {
    locations.add(pos);
    mMap.addMarker(new MarkerOptions().position(pos));


    }

    public synchronized void uppdateMap() {
        if (mMapready) {

            mMap.clear();
            mMap.addPolyline(new PolylineOptions().addAll(locations));
            for (LatLng l : locations) {
                mMap.addMarker(new MarkerOptions().position(l));
            }


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
        if (locations.size()>0) {
            largestLat = locations.get(0).latitude;
            smalestLat = locations.get(0).latitude;
            smalestLong = locations.get(0).longitude;
            largestLong = locations.get(0).longitude;

            for (int i = 0; i < locations.size(); i++) {
                if (largestLat<locations.get(i).latitude){largestLat = locations.get(i).latitude;}
                if (smalestLat>locations.get(i).latitude){ smalestLat = locations.get(i).latitude;}
                if (largestLong<locations.get(i).longitude){ largestLong = locations.get(i).longitude;}
                if (smalestLong>locations.get(i).longitude){ smalestLong = locations.get(i).longitude;}

                lat += locations.get(i).latitude;
                log += locations.get(i).longitude;
            }

            LatLngBounds bounds = new LatLngBounds(new LatLng(smalestLat,smalestLong),new LatLng(largestLat,largestLong));

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));
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

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Toast.makeText(getApplicationContext(), "Yey detta fungerade", Toast.LENGTH_SHORT).show();
                //TODO:Implementera vad som händer när man klickar på en marker
                if (marker.getPosition().equals("kolla igenom alla longlat")) {
                    //handle click here
                }
                return true;
            }

        });

        mMapready = true;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

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
            super.onBackPressed();
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
            currentTripMode();
            /**
            MapFragment mapFragment = new MapFragment();
            FragmentManager m = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mapFragment);
            fragmentTransaction.commit();
             **/

        } else if (id == R.id.my_trips_button) {

            GalleryFragment galleryFragment = new GalleryFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.map, galleryFragment);
            fragmentTransaction.commit();


        } else if (id == R.id.map_button) {
            worldMapmode();

        } else if (id == R.id.end_trip_button) {
            //Implementera end tripp
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void startCameraActivity(View view){
        startActivity(new Intent(this, CameraActivity.class));
    }
}

