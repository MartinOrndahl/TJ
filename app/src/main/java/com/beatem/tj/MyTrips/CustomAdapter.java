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
import android.widget.TextView;

import com.beatem.tj.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends BaseAdapter {
    Holder holder;
    String[] result;
    Context context;
    String[] imageId;
    int amountOfPicturesExtra = 0;
    private static LayoutInflater inflater = null;

    HashMap<String, ArrayList<String>> picpaths;
    private ArrayList<String> acceptedPicPaths;
    private ArrayList<Integer> amountExtras = new ArrayList<Integer>();


    public CustomAdapter(Context context, String[] prgmNameList, HashMap<String, ArrayList<String>> prgmImages) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        this.context = context;
        picpaths = prgmImages;
        acceptedPicPaths = new ArrayList<String>();

        addImages(picpaths);

        //imageId=prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /*
    Hämtar alla bilder, med resenamn som key
     */
    private void addImages(HashMap<String, ArrayList<String>> hmap) {


        for (int i = 0; i < result.length; i++) {
            ArrayList<String> pics = hmap.get(result[i]);
            if (pics.size() >= 3) {
                amountExtras.add(i, pics.size()-3);
                for (int j = 0; j < 3; j++) {
                    acceptedPicPaths.add(pics.get(j));
                }
            } else {
                for (int j = 0; j < 3; j++) {
                    acceptedPicPaths.add(pics.get(0));
                    amountExtras.add(i, 0);
                }

            }
        }

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
        TextView title, number;
        ImageView img1, img2, img3, blankImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.custom_grid_component, null);



        /*
        Hämta alla komponenter
         */
        holder.title = (TextView) rowView.findViewById(R.id.textView1);
        holder.number = (TextView) rowView.findViewById(R.id.custom_grid_plusNumber);

        holder.img1 = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.img2 = (ImageView) rowView.findViewById(R.id.imageView2);
        holder.img3 = (ImageView) rowView.findViewById(R.id.imageView3);
        holder.blankImage = (ImageView) rowView.findViewById(R.id.blankImage);


        /*
        Hanterar vår blankImage till rätt storlek
         */
        Bitmap blankImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent);
        blankImage = Bitmap.createScaledBitmap(blankImage, 128, 128, false);



        /*
        Tillsätter komponenterna sina värden
         */
        holder.title.setText(result[position]);
        holder.number.setText("+" + amountExtras.get(position));

        handleImages(position);
        holder.blankImage.setImageBitmap(blankImage);


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
        imageId = (String[]) acceptedPicPaths.toArray(new String[0]);
        holder.img1.setImageBitmap(getThumbNail(imageId[position * 3]));
        holder.img2.setImageBitmap(getThumbNail(imageId[position * 3 + 1]));
        holder.img3.setImageBitmap(getThumbNail(imageId[position * 3 + 2]));
    }

    private Bitmap getThumbNail(String s) {
        final int THUMBSIZE = 128;

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(s),
                THUMBSIZE,
                THUMBSIZE);

        ThumbImage = RotateBitmap(ThumbImage, 90);
        Bitmap.createScaledBitmap(ThumbImage, 128, 128, false);
        return ThumbImage;
    }


    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}