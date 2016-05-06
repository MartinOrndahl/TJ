package com.beatem.tj;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {

    String[] result;
    Context context;
    String[] imageId = new String[3];
    int amountOfPicturesExtra;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, String[] prgmNameList, String[] prgmImages) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        this.context = context;
        amountOfPicturesExtra= prgmImages.length-3;
        addImages(prgmImages);

        //imageId=prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private void addImages(String[] prgmImages) {
        if (prgmImages.length >= 3) {
            for (int i = 0; i < 3; i++) {
                imageId[i] = prgmImages[i];
            }
        }
        else{
            //TODO: Fixa detta fallet
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
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.custom_grid_component, null);


        holder.title = (TextView) rowView.findViewById(R.id.textView1);
        holder.number = (TextView) rowView.findViewById(R.id.custom_grid_plusNumber);

        holder.img1 = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.img2 = (ImageView) rowView.findViewById(R.id.imageView2);
        holder.img3 = (ImageView) rowView.findViewById(R.id.imageView3);
        holder.blankImage= (ImageView)rowView.findViewById(R.id.blankImage);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;


        Bitmap blankImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent);
        blankImage= Bitmap.createScaledBitmap(blankImage, 128, 128, false);



        holder.title.setText(result[position]);
        holder.number.setText("+"+ amountOfPicturesExtra);

        holder.img1.setImageBitmap(getThumbNail(imageId[position * 3]));
        holder.img2.setImageBitmap(getThumbNail(imageId[position * 3 + 1]));
        holder.img3.setImageBitmap(getThumbNail(imageId[position * 3 + 2]));
        holder.blankImage.setImageBitmap(blankImage);


        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }

    private Bitmap getThumbNail(String s) {
        final int THUMBSIZE = 128;

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(s),
                THUMBSIZE,
                THUMBSIZE);

        ThumbImage= RotateBitmap(ThumbImage, 90);
        Bitmap.createScaledBitmap(ThumbImage, 128, 128, false);
        return ThumbImage;
    }


    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}