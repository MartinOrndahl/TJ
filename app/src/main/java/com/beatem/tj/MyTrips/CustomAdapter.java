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
    private ArrayList<Integer> amountExtras;


    public CustomAdapter(Context context, String[] prgmNameList, HashMap<String, ArrayList<String>> prgmImages) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        this.context = context;
        picpaths = prgmImages;
        acceptedPicPaths = new ArrayList<String>();
        amountExtras = new ArrayList<Integer>();

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
                amountExtras.add(i, pics.size() - 3);
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




        /*
        Tillsätter komponenterna sina värden
         */
        //holder.title.setText(result[position]);
        //holder.number.setText("+" + amountExtras.get(position));

        //handleImages(position);
        //holder.blankImage.setImageBitmap(blankImage);

        new AsyncTaskLoadFiles(holder.img1, holder.img2, holder.img3,holder.blankImage, holder.title, holder.number, amountExtras, (String[]) acceptedPicPaths.toArray(new String[0])).execute(position);

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
        int pos;
        ImageView IMG1, IMG2, IMG3, BLANK;
        String[] ImageId;
        TextView hTitle, hNumber;
        ArrayList<Integer> AmountExtras;

        public AsyncTaskLoadFiles(ImageView IMG1, ImageView IMG2, ImageView IMG3, ImageView BLANK, TextView hTitle, TextView hNumber,ArrayList<Integer> amount, String[] ImageId) {
            this.IMG1 = IMG1;
            this.IMG2 = IMG2;
            this.IMG3 = IMG3;
            this.BLANK=BLANK;
            this.hTitle=hTitle;
            this.hNumber=hNumber;
            this.AmountExtras= amount;

            this.ImageId = ImageId;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            pos = integers[0];

            return "some string";

        }

        private void handleImages(int pos) {
            //imageId = (String[]) acceptedPicPaths.toArray(new String[0]);
            IMG1.setImageBitmap(getThumbNail(ImageId[pos * 3]));
            IMG2.setImageBitmap(getThumbNail(ImageId[pos * 3 + 1]));
            IMG3.setImageBitmap(getThumbNail(ImageId[pos * 3 + 2]));
        }

        private Bitmap getThumbNail(String s) {
            final int THUMBSIZE = 128;

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
            Bitmap blankImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent);
            blankImage = Bitmap.createScaledBitmap(blankImage, 128, 128, false);
            BLANK.setImageBitmap(blankImage);
            hTitle.setText(result[pos]);
            hNumber.setText("+" + AmountExtras.get(pos));
            handleImages(pos);

            super.onPostExecute(s);
        }
    }


}