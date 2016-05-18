package com.beatem.tj.MyTrips;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.beatem.tj.MyLocation;
import com.beatem.tj.MySqLite;
import com.beatem.tj.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private static GridView gridView;

    private MySqLite mySqlLite;
    ArrayList<MyLocation> locations;
    private ArrayList<String> trips ;
    private ArrayList<String> picPaths ;
    private ArrayList<String> picPathsTrips ;
    HashMap<String, ArrayList<String>> hmap ;
    public boolean galleryStarted;
    public ChosenTripFragment newFragment;



    public GalleryFragment() {
        // Required empty public constructor
    }

    public void getImages() {
        for (MyLocation myLocation : locations) {
            ArrayList<String> temp= new ArrayList<String>();

            if (hmap.get(myLocation.getTrip()) != null) {
                temp = hmap.get(myLocation.getTrip());
            }

            temp.add(myLocation.getPicpath());
            hmap.put(myLocation.getTrip(), temp);
            picPaths.add(myLocation.getPicpath());

        }

    }

    public void getTitles() {
        for (MyLocation myLocation : locations) {
            if (!trips.contains(myLocation.getTrip())) {
                trips.add(myLocation.getTrip());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mySqlLite = new MySqLite(getActivity().getApplicationContext());
        locations = mySqlLite.getLocations();
        hmap= new HashMap<String, ArrayList<String>>();
        picPathsTrips= new ArrayList<String>();
        picPaths= new ArrayList<String>();
        trips= new ArrayList<String>();

        //Läser in alla bilder från internminnet till en vektor
        //Bitmap[] images = {loadImageFromStorage("/storage/emulated/0/Pictures/GPUImage/1461422081611.jpg"), loadImageFromStorage("/storage/emulated/0/Snapchat/Snapchat-8253945721256595138.jpg")};

        /**
         MySqLite db = new MySqLite(getContext());
         ArrayList<MyLocation> locations = db.getLocations();
         Bitmap[] images = new Bitmap[locations.size()];
         int i = 0;
         for(MyLocation temp : locations){
         Bitmap bitmap = loadImageFromStorage(temp.getPicpath());
         if(bitmap != null){
         images[i] = bitmap;
         i++;
         }
         }
         **/


        getTitles();
        getImages();


        gridView = (GridView) view.findViewById(R.id.view_gallery);
        //gridView.setAdapter(new CustomAdapter(getActivity().getApplicationContext(), (String[]) trips.toArray(new String[0]), (String[]) picPaths.toArray(new String[0])));
        gridView.setAdapter(new CustomAdapter(getActivity().getApplicationContext(), (String[]) trips.toArray(new String[0]), hmap));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {


                galleryStarted = true;
                newFragment = new ChosenTripFragment();
                Bundle b = new Bundle();
                b.putString("trip",trips.get(position));
                newFragment.setArguments(b);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//hej
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.add(R.id.fragment_container, newFragment,"gallery");

                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();
            }
        });



        /*


        //Hanterar gridViewn
        gridView = (GridView) view.findViewById(R.id.view_gallery);
        gridView.setAdapter(new ImageAdapter(getContext(), images));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id)
            {
                Toast.makeText(getContext(),
                        "pic" + (position + 1) + " selected",
                        Toast.LENGTH_SHORT).show();
            }
        });
*/

        // Inflate the layout for this fragment
        return view;
    }



    private Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;
        try {
            File f = new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

}
