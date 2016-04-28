package com.beatem.tj.OldTripsViewer;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beatem.tj.R;

import java.util.ArrayList;

/**
 * Created by JoelBuhrman on 16-04-27.
 */

public class SlideImageAdapter extends PagerAdapter {


    private ArrayList<Integer> IMAGES;
    private ArrayList<String> DATES, CITIES, DIRECTIONS;
    private LayoutInflater inflater;
    private Context context;


    public SlideImageAdapter(Context context,ArrayList<Integer> IMAGES, ArrayList<String> DATES,ArrayList<String> CITIES, ArrayList<String> DIRECTIONS) {
        this.context = context;
        this.IMAGES=IMAGES;
        this.DATES=DATES;
        this.CITIES=CITIES;
        this.DIRECTIONS=DIRECTIONS;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        final TextView date = (TextView) imageLayout
                .findViewById(R.id.slidingimages_layout_date);
        final TextView city = (TextView) imageLayout
                .findViewById(R.id.slidingimages_layout_city);
        final TextView direction = (TextView) imageLayout
                .findViewById(R.id.slidingimages_layout_direction);


        imageView.setImageResource(IMAGES.get(position));
        date.setText(DATES.get(position));
        city.setText(CITIES.get(position));
        direction.setText(DIRECTIONS.get(position));


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}