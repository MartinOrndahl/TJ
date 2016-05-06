package com.beatem.tj;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.beatem.tj.Camera.CameraActivity;
import com.beatem.tj.OldTripsViewer.SlideImageActivity;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener, NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    private GoogleMap mMap;
    private LocationManager locationManager;
    public static LatLng currentlocation;
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
    private android.support.v4.app.FragmentTransaction fragmentTransactionCat;
    private CatLoadingView cat;
    private MySqLite db;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location currentloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        zoomEnabled = false;
        permission = false;

        polygon = new PolygonOptions().geodesic(true);
        cat = new CatLoadingView();
        setContentView(R.layout.activity_nav_drawer);
        fragmentTransactionCat = getSupportFragmentManager().beginTransaction();
        cat.show(fragmentTransactionCat, "");

        if (!SaveSharedPreferences.getFirstStart(getApplicationContext())) {


            db = new MySqLite(getApplicationContext());
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bondibeachselfie);
            bm=RotateBitmap(bm, 270);
            Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.sydneyoperahouse);
            bm1=RotateBitmap(bm1, 270);
            Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.universityofsydney);
            bm2=RotateBitmap(bm2, 270);
            Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.sydneyharbourbridge);
            bm3=RotateBitmap(bm3, 270);

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "TJ");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {

                if (!mediaStorageDir.mkdirs()) {
                    Log.e("MyCameraApp", "failed to create directory");
                }
            }

            int i=0;
            boolean doIt = false;
            //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + i + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(mediaFile);
                Log.e("marcusärcpbra", "Filen borde ha skrivits rätt här");
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                bm.recycle();
                doIt = true;


            } catch (FileNotFoundException e) {
                Log.e("ASDF", "File not found: " + e.getMessage());
                doIt = false;
            } catch (IOException e) {
                Log.e("ASDF", "Error accessing file: " + e.getMessage());
                doIt = false;
            }
            i++;
            File mediaFile1 = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + i + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(mediaFile1);
                Log.e("marcusärcpbra", "Filen borde ha skrivits rätt här");
                bm1.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                bm1.recycle();
                doIt = true;


            } catch (FileNotFoundException e) {
                Log.e("ASDF", "File not found: " + e.getMessage());
                doIt = false;
            } catch (IOException e) {
                Log.e("ASDF", "Error accessing file: " + e.getMessage());
                doIt = false;
            }
            i++;

            File mediaFile2 = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + i + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(mediaFile2);
                Log.e("marcusärcpbra", "Filen borde ha skrivits rätt här");
                bm2.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                bm2.recycle();
                doIt = true;


            } catch (FileNotFoundException e) {
                Log.e("ASDF", "File not found: " + e.getMessage());
                doIt = false;
            } catch (IOException e) {
                Log.e("ASDF", "Error accessing file: " + e.getMessage());
                doIt = false;
            }
            i++;
            File mediaFile3 = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + i + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(mediaFile3);
                Log.e("marcusärcpbra", "Filen borde ha skrivits rätt här");
                bm3.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                bm3.recycle();
                doIt = true;


            } catch (FileNotFoundException e) {
                Log.e("ASDF", "File not found: " + e.getMessage());
                doIt = false;
            } catch (IOException e) {
                Log.e("ASDF", "Error accessing file: " + e.getMessage());
                doIt = false;
            }
            i++;

            if (doIt) {

                db.addLocation(new MyLocation((float)151.276613, (float)-33.892208, "Australien", "Selfie at Bondi!", mediaFile.getAbsolutePath(), "N", "filter_early_bird", "5 Jan 2016"));
                db.addLocation(new MyLocation((float)151.215855,(float)-33.855893, "Australien", "Caught this awesome photo with the drone", mediaFile1.getAbsolutePath(), "S", "filter_early_bird", "9 Jan 2016"));
                db.addLocation(new MyLocation((float) 151.18661811, (float)-33.888655, "Australien", "This is where my best friend in the entire world, Joel, studies :) He's the best.", mediaFile2.getAbsolutePath(), "W", "filter_rise", "10 Jan 2016"));
                db.addLocation(new MyLocation((float)151.210701, (float)-33.852271, "Australien", "I made it to the top of the Sydney Harbout bridge!! :D", mediaFile3.getAbsolutePath(), "S", "filter_walden", "16 Jan 2016"));
                SaveSharedPreferences.setStartBefore(getApplicationContext(), true);
            }
        }
        getTrips();



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
                if(isBetterLocation(location,new Location(currentloc))){
                    currentloc = location;
                }

                currentlocation = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
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
        try {
            currentlocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        } catch (Exception e) {

        }
        GpsListniner(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        galleryFragment = new GalleryFragment();
        removeCat();


       /* File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TJ");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.adventure);
        try {
            FileOutputStream out = new FileOutputStream(mediaFile);
            Log.e("marcusärcpkass", "Filen borde ha skrivits rätt här");
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bm.recycle();

        } catch (FileNotFoundException e) {
            Log.e("ASDF", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("ASDF", "Error accessing file: " + e.getMessage());
        }


        MySqLite sqLite = new MySqLite(getApplicationContext());
        sqLite.addLocation(new MyLocation((float) -33.8922, (float)  151.277, "Sydney", "zup", "E", "filter_normal","1 May 2016"));
*/
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void getTrips() {
        MySqLite db = new MySqLite(getApplicationContext());
        locations = db.getLocations();
    }

    public void removeCat() {
        fragmentTransactionCat.remove(cat);

    }



    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    public synchronized void GpsListniner(boolean onoff) {
        try {
            if (onoff && locationManager != null) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            } else {
                //TODO:sluta lyssna efter gps signal.

            }
        } catch (SecurityException e) {

        }
    }

    public synchronized void uppdateCurrentLocation() {
        Log.i("test1", "vi kom hit");
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
            uppdateCurrentLocation();
            mMap.clear();
            ArrayList<LatLng> polylist = new ArrayList<LatLng>();
            int i = 0;

            String lastKnownTrip;
            lastKnownTrip = locations.get(0).getTrip();
            for (MyLocation location : locations) {

                if (location.getTrip().equals(lastKnownTrip)) {
                    mMap.addMarker(new MarkerOptions().position(location.getLatlng()));
                    polylist.add(location.getLatlng());
                } else {
                    mMap.addPolyline(new PolylineOptions().addAll(polylist));
                    mMap.addMarker(new MarkerOptions().position(location.getLatlng()));
                    polylist.clear();
                    polylist.add(location.getLatlng());
                    lastKnownTrip = location.getTrip();
                }

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

    public void worldMapmode() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        zoomEnabled = true;
        //Todo: hämta alla markeringar från SQLLite databas och sätt ut på karta
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));


    }

    public void currentTripMode() {
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        zoomEnabled = false;
        moveToCurrentTrip();
    }

    public void moveToCurrentTrip() {
        int lat = 0;
        double largestLat = 0;
        double smalestLat = 0;
        double largestLong = 0;
        double smalestLong = 0;
        int log = 0;
        double largestlog;
        ArrayList<MyLocation> locations = new ArrayList<MyLocation>();
        for(MyLocation location:this.locations){
            if(location.getTrip().equals(SaveSharedPreferences.getCurrentTrip(getApplicationContext()))){
                locations.add(location);
            }
        }
        if (locations != null && locations.size() > 0) {
            largestLat = locations.get(0).getLatitude();
            smalestLat = locations.get(0).getLatitude();
            smalestLong = locations.get(0).getLongditude();
            largestLong = locations.get(0).getLongditude();

            for (int i = 0; i < locations.size(); i++) {
                if (largestLat < locations.get(i).getLatitude()) {
                    largestLat = locations.get(i).getLatitude();
                }
                if (smalestLat > locations.get(i).getLatitude()) {
                    smalestLat = locations.get(i).getLatitude();
                }
                if (largestLong < locations.get(i).getLongditude()) {
                    largestLong = locations.get(i).getLongditude();
                }
                if (smalestLong > locations.get(i).getLongditude()) {
                    smalestLong = locations.get(i).getLongditude();
                }

                lat += locations.get(i).getLatitude();
                log += locations.get(i).getLongditude();
            }

            LatLngBounds bounds = new LatLngBounds(new LatLng(smalestLat, smalestLong), new LatLng(largestLat, largestLong));

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(bounds.getCenter()));
        } else {
            if (currentlocation == null) {
                currentlocation = new LatLng(0, 0);
            }
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
                for (MyLocation location : locations) {
                    if (marker.getPosition().equals(location.getLatlng())) {


                        Intent i = new Intent(getApplicationContext(), SlideImageActivity.class);
                        //TODO: Fixa så man kan kolla genom hela resan

                        i.putExtra("location", location);
                        startActivity(i);

                        return true;
                    }

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
                currentTripMode();
                removeCat();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        if (zoomEnabled) {
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

            Intent dbmanager = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
            startActivity(dbmanager);

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
            Toast.makeText(getApplicationContext(),mMap.getMyLocation().getLatitude()+":"+mMap.getMyLocation().getLongitude() + "jämnfört med current: " + currentlocation.latitude+":"+currentlocation.longitude,Toast.LENGTH_LONG).show();
            if(SaveSharedPreferences.getCurrentTrip(getApplicationContext()).equals("none")){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));
            }else {


                if (galleryCreated) {

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(galleryFragment);
                    fragmentTransaction.commit();
                    galleryCreated = false;
                }
            }
            currentTripMode();


        } else if (id == R.id.my_trips_button) {

            galleryCreated = true;
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, galleryFragment);
            fragmentTransaction.commit();


        } else if (id == R.id.map_button) {

            worldMapmode();
            if (galleryCreated) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(galleryFragment);
                fragmentTransaction.commit();
                galleryCreated = false;
            }


        } else if (id == R.id.end_trip_button) {

            if (navigationView.getMenu().findItem(R.id.end_trip_button).getTitle().toString().equals("Start new trip")) {
                MenuItem i = navigationView.getMenu().findItem(R.id.end_trip_button);
                i.setTitle("End trip");
                AlertDialog.Builder startNewTrip = new AlertDialog.Builder(this);
                startNewTrip.setTitle("Tripname");
                startNewTrip.setIcon(getDrawable(R.drawable.worldmap));
                final EditText input = new EditText(this);
                input.setHint("Name of your trip");
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.setSingleLine(true);
                startNewTrip.setView(input, 64, 0, 0, 0);
                startNewTrip.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        SaveSharedPreferences.setCurrentTrip(getApplicationContext(), input.getText().toString());
                        MenuItem i = navigationView.getMenu().findItem(R.id.current_trip_button);
                        i.setTitle("Current trip");
                        MenuItem j = navigationView.getMenu().findItem(R.id.end_trip_button);

                        dialog.dismiss();
                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                        fab.setImageResource(R.drawable.ic_menu_camera);

                        j.setIcon(R.drawable.vector_drawable_ic_cancel_black___px);
                        dialog.dismiss();
                    }

                });
                startNewTrip.show();


                currentTripMode();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Confirm");
                builder.setIcon(getDrawable(R.drawable.worldmap));
                builder.setMessage("Are you sure you want to end this trip?");


                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing but close the dialog
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        MenuItem i = navigationView.getMenu().findItem(R.id.end_trip_button);
                        i.setTitle("Start new trip");
                        MenuItem j = navigationView.getMenu().findItem(R.id.current_trip_button);
                        j.setTitle("Current location");
                        SaveSharedPreferences.setCurrentTrip(getApplicationContext(), "none");
                        worldMapmode();

                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                        fab.setImageResource(R.drawable.vector_drawable_ic_add_black___px);

                        i.setIcon(R.drawable.vector_drawable_ic_add_black___px);
                        dialog.dismiss();
                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void startCameraActivity(View view) {
        if (!SaveSharedPreferences.getCurrentTrip(getApplicationContext()).equals("none")) {
            startActivity(new Intent(this, CameraActivity.class).putExtra("camType", "back"));
        }
        else{
            AlertDialog.Builder startNewTrip = new AlertDialog.Builder(this);
            startNewTrip.setTitle("Tripname");
            startNewTrip.setIcon(getDrawable(R.drawable.worldmap));
            final EditText input = new EditText(this);
            input.setHint("Name of your trip");
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            input.setSingleLine(true);
            startNewTrip.setView(input, 64,0,0,0);
            startNewTrip.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    SaveSharedPreferences.setCurrentTrip(getApplicationContext(),input.getText().toString());

                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                    fab.setImageResource(R.drawable.ic_menu_camera);
                    MenuItem i = navigationView.getMenu().findItem(R.id.end_trip_button);
                    i.setTitle("End trip");
                    i.setIcon(R.drawable.vector_drawable_ic_cancel_black___px);
                    MenuItem j = navigationView.getMenu().findItem(R.id.current_trip_button);
                    j.setTitle("Current trip");
                    dialog.dismiss();
                }

            });
            startNewTrip.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            startNewTrip.show();


        }


    }
}

