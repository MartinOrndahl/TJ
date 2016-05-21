package com.beatem.tj.MyTrips;

/**
 * Created by JoelBuhrman on 16-05-06.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
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
    private ArrayList<Integer> amountExtras;


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
        amountExtras = new ArrayList<Integer>();



        /*
        Hämta alla komponenter
         */

        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);


        new AsyncTaskLoadFiles(holder.img).execute(position);

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


    public class AsyncTaskLoadFiles extends AsyncTask<Integer, Intent, String> {
        ImageView IMG;
        int pos;

        public AsyncTaskLoadFiles(ImageView IMG) {
            this.IMG = IMG;

        }

        @Override
        protected String doInBackground(Integer... integers) {
            pos = integers[0];
            return "some string";

        }

        private void handleImages(int pos) {
            IMG.setImageBitmap(getThumbNail(result[pos]));

        }

        private Bitmap getThumbNail(String s) {
            final int THUMBSIZE = 256;

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(s),
                    THUMBSIZE,
                    THUMBSIZE);

            ThumbImage = RotateBitmap(ThumbImage, 90);
            Bitmap.createScaledBitmap(ThumbImage, THUMBSIZE, THUMBSIZE, false);
            return ThumbImage;


        }

        public Bitmap RotateBitmap(Bitmap source, float angle) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }


        @Override
        protected void onProgressUpdate(Intent... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            handleImages(pos);

            super.onPostExecute(s);
        }
    }


}