package com.beatem.tj.OldTripsViewer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beatem.tj.CapturedImage.filter.IF1977Filter;
import com.beatem.tj.CapturedImage.filter.IFAmaroFilter;
import com.beatem.tj.CapturedImage.filter.IFEarlybirdFilter;
import com.beatem.tj.CapturedImage.filter.IFHefeFilter;
import com.beatem.tj.CapturedImage.filter.IFHudsonFilter;
import com.beatem.tj.CapturedImage.filter.IFInkwellFilter;
import com.beatem.tj.CapturedImage.filter.IFLomoFilter;
import com.beatem.tj.CapturedImage.filter.IFLordKelvinFilter;
import com.beatem.tj.CapturedImage.filter.IFNashvilleFilter;
import com.beatem.tj.CapturedImage.filter.IFRiseFilter;
import com.beatem.tj.CapturedImage.filter.IFSierraFilter;
import com.beatem.tj.CapturedImage.filter.IFSutroFilter;
import com.beatem.tj.CapturedImage.filter.IFToasterFilter;
import com.beatem.tj.CapturedImage.filter.IFValenciaFilter;
import com.beatem.tj.CapturedImage.filter.IFWaldenFilter;
import com.beatem.tj.CapturedImage.filter.IFXprollFilter;
import com.beatem.tj.R;

import java.io.File;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by JoelBuhrman on 16-04-27.
 */

public class SlideImageAdapter extends PagerAdapter implements View.OnClickListener {

 //Test
    private ArrayList<String> IMAGES;
    private ArrayList<String> DATES, CITIES, DIRECTIONS, DESCRIPTIONS, FILTERS;

    private LayoutInflater inflater;
    private Context context;
    private SlideImageActivity slideImageActivity;

    private ImageView upArrow;
    private TextView descriptionText, descriptionTextContent;
    private RelativeLayout descriptionLayout;
    ResizeAnimation expandAnimation, collapsAnimation;
    private boolean expandDirection;
    private View imageLayout;
    private ViewPager mPager;
    private int page;
    private GPUImageView imageView;

    private boolean firstLaunch;


    public SlideImageAdapter(SlideImageActivity slideImageActivity, ArrayList<String> IMAGES,
                             ArrayList<String> DATES, ArrayList<String> CITIES, ArrayList<String> DIRECTIONS, ArrayList<String> DESCRIPTIONS,ArrayList<String> FILTERS,
                             ViewPager pager, boolean expandDirection, boolean firstLaunch, int page) {
        this.slideImageActivity = slideImageActivity;
        this.expandDirection = expandDirection;
        //this.context = context;
        this.IMAGES = IMAGES;
        this.DATES = DATES;
        this.CITIES = CITIES;
        this.DIRECTIONS = DIRECTIONS;
        this.DESCRIPTIONS=DESCRIPTIONS;
        this.FILTERS= FILTERS;
        this.mPager = pager;
        this.firstLaunch=firstLaunch;
        this.page=page;

        inflater = LayoutInflater.from(slideImageActivity.getApplicationContext());




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


        if(!expandDirection && !firstLaunch){
            imageLayout = inflater.inflate(R.layout.slidingimages_layout_expanded, view, false);
        }
        else{
            imageLayout = inflater.inflate(R.layout.slidingimages_layout_collapsed, view, false);
        }



        assert imageLayout != null;
        imageView = (GPUImageView) imageLayout
                .findViewById(R.id.image);
        final TextView date = (TextView) imageLayout
                .findViewById(R.id.slidingimages_layout_date);
        final TextView city = (TextView) imageLayout
                .findViewById(R.id.slidingimages_layout_city);
        final TextView direction = (TextView) imageLayout
                .findViewById(R.id.slidingimages_layout_direction);

        upArrow = (ImageView) imageLayout.findViewById(R.id.slidingimages_layout_arrow);
        descriptionText = (TextView) imageLayout.findViewById(R.id.slidingimages_layout_description_text);
        descriptionTextContent = (TextView) imageLayout.findViewById(R.id.slidingimages_layout_descriptiontext_textView);
        descriptionLayout = (RelativeLayout) imageLayout.findViewById(R.id.slidingimages_layout_description_layout);
        descriptionLayout.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        expandAnimation = new ResizeAnimation(descriptionLayout, 800, 0);
        expandAnimation.setDuration(500);
        collapsAnimation = new ResizeAnimation(descriptionLayout, 0, 800);
        collapsAnimation.setDuration(500);
        upArrow.setOnClickListener(this);


        setFilter(FILTERS.get(position));


        if (expandDirection) {
            descriptionLayout.startAnimation(expandAnimation);
            upArrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_down_white___px);

        } else {
            if(!firstLaunch){
            descriptionLayout.startAnimation(collapsAnimation);
            upArrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_up_white___px);}
        }

        Toast.makeText(slideImageActivity.getApplicationContext(), IMAGES.get(position), Toast.LENGTH_LONG).show();
        File file = new File(IMAGES.get(position));
        imageView.setImage(BitmapFactory.decodeFile(file.getAbsolutePath()));
        date.setText(DATES.get(position));
        city.setText(CITIES.get(position));
        direction.setText(DIRECTIONS.get(position));
        descriptionTextContent.setText(DESCRIPTIONS.get(position));


        view.addView(imageLayout, 0);


        return imageLayout;
    }

    private void setFilter(String s) {
        switch (s){
            case "filter_normal":
                imageView.setFilter(new GPUImageFilter());
                break;
            case "filter_1977":
                imageView.setFilter(new IF1977Filter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_amaro":
                imageView.setFilter(new IFAmaroFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_brannan":
                imageView.setFilter(new IF1977Filter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_early_bird":
                imageView.setFilter(new IFEarlybirdFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_hefe":
                imageView.setFilter(new IFHefeFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_hudson":
                imageView.setFilter(new IFHudsonFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_inkwell":
                imageView.setFilter(new IFInkwellFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_lomofi":
                imageView.setFilter(new IFLomoFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_lord_kelvin":
                imageView.setFilter(new IFLordKelvinFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_nashville":
                imageView.setFilter(new IFNashvilleFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_rise":
                imageView.setFilter(new IFRiseFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_sierra":
                imageView.setFilter(new IFSierraFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_sutro":
                imageView.setFilter(new IFSutroFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_toaster":
                imageView.setFilter(new IFToasterFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_valencia":
                imageView.setFilter(new IFValenciaFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_walden":
                imageView.setFilter(new IFWaldenFilter(slideImageActivity.getApplicationContext()));
                break;
            case "filter_xproii":
                imageView.setFilter(new IFXprollFilter(slideImageActivity.getApplicationContext()));
                break;
            default:
                break;
        }
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.slidingimages_layout_arrow:
                int page= mPager.getCurrentItem();
                if (expandDirection) {
                    expandDirection = false;
                } else {
                    expandDirection = true;
                }
                mPager.setAdapter(new SlideImageAdapter(slideImageActivity, slideImageActivity.getImagesArray(),
                        slideImageActivity.getDatesArray(),
                        slideImageActivity.getCitiesArray(),
                        slideImageActivity.getDirectionsArray(),
                        slideImageActivity.getDescriptionsArray(),
                        slideImageActivity.getFiltersArray(),
                        mPager, expandDirection, false, page));


                mPager.setCurrentItem(page);

                break;
        }

    }

    private class ResizeAnimation extends Animation {
        final int targetHeight;
        View view;
        int startHeight;

        public ResizeAnimation(View view, int targetHeight, int startHeight) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.startHeight = startHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) (startHeight + (targetHeight - startHeight) * interpolatedTime);
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}