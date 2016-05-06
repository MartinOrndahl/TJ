package com.beatem.tj.MyTrips;

/**
 * Created by JoelBuhrman on 16-05-06.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beatem.tj.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomChosenTripAdapter extends BaseAdapter {
    Holder holder;
    String[] result;
    Context context;
    String[] imageId;
    int amountOfPicturesExtra = 0;
    private static LayoutInflater inflater = null;

    HashMap<String, ArrayList<String>> picpaths;
    private ArrayList<String> acceptedPicPaths;
    private ArrayList<Integer> amountExtras = new ArrayList<Integer>();


    public CustomChosenTripAdapter(Context context, String[] paths) {
        // TODO Auto-generated constructor stub
        result = paths;
        this.context = context;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.my_trips_chosen_trip_custom_component, null);



        /*
        Hämta alla komponenter
         */

        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
        handleImages(position);


        /*rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();


            }
        });
*/
        return rowView;
    }

    private void handleImages(int position) {
        holder.img.setImageBitmap( RotateBitmap(BitmapFactory.decodeFile(result[position]),90));

    }

    private Bitmap getThumbNail(String s) {
        final int THUMBSIZE = 128;

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(s),
                THUMBSIZE,
                THUMBSIZE);

        ThumbImage = RotateBitmap(ThumbImage, 90);
        Bitmap.createScaledBitmap(ThumbImage, 500, 500, false);
        return ThumbImage;
    }


    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}