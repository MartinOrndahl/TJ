package com.beatem.tj;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    public static String [] titles={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
    public static int [] images={
            R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
            };


    private static GridView gridView;
    private int THUMBNAIL_HEIGHT = 64;

    private MySqLite mySqlLite;
    ArrayList<MyLocation> locations;
    private ArrayList<String> trips=new ArrayList<String>();
    private ArrayList<String> picPaths=new ArrayList<String>();

    public GalleryFragment() {
        // Required empty public constructor
    }

    public void getImages() {
        for(MyLocation myLocation: locations){
            picPaths.add(myLocation.getPicpath());
        }

    }

    public void getTitles() {
        for(MyLocation myLocation: locations){
            if(!trips.contains(myLocation.getTrip())){
                trips.add(myLocation.getTrip());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mySqlLite= new MySqLite(getActivity().getApplicationContext());
        locations = mySqlLite.getLocations();

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
        //gridView.setAdapter(new CustomAdapter(getActivity().getApplicationContext(), titles,images));
        gridView.setAdapter(new CustomAdapter(getActivity().getApplicationContext(), (String[]) trips.toArray(new String[0]),(String[]) picPaths.toArray(new String[0])));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id)
            {
                /*Toast.makeText(getContext(),
                        "pic" + (position + 1) + " selected",
                        Toast.LENGTH_SHORT).show();*/

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

    private Bitmap loadImageFromStorage(String path){
        Bitmap b = null;
        try {
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

}
