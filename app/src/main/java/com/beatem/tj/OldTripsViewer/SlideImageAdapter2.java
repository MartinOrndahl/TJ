package com.beatem.tj.OldTripsViewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beatem.tj.CapturedImage.filter.IF1977Filter;
import com.beatem.tj.CapturedImage.filter.IFAmaroFilter;
import com.beatem.tj.CapturedImage.filter.IFBrannanFilter;
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

public class SlideImageAdapter2 extends PagerAdapter implements View.OnClickListener {


    private ArrayList<String> IMAGES;
    private ArrayList<String> DATES, CITIES, DIRECTIONS, DESCRIPTIONS, FILTERS;

    private LayoutInflater inflater;
    private Context context;
    private SlideImageActivity slideImageActivity;

    private TextView descriptionText, descriptionTextContent;
    private boolean expandDirection;
    private View imageLayout;
    private ViewPager mPager;
    private int page;
    private GPUImageView imageView;


    private boolean firstLaunch;


    public SlideImageAdapter2(SlideImageActivity slideImageActivity, ArrayList<String> IMAGES,
                              ArrayList<String> DATES, ArrayList<String> CITIES, ArrayList<String> DIRECTIONS, ArrayList<String> DESCRIPTIONS,ArrayList<String> FILTERS,
                              ViewPager pager, boolean expandDirection, boolean firstLaunch, int page) {
        this.slideImageActivity = slideImageActivity;
        this.expandDirection = expandDirection;
        this.IMAGES = IMAGES;
        this.DATES = DATES;
        this.CITIES = CITIES;
        this.DIRECTIONS = DIRECTIONS;
        this.DESCRIPTIONS = DESCRIPTIONS;
        this.FILTERS=FILTERS;
        this.mPager = pager;
        this.firstLaunch = firstLaunch;
        this.page = page;

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

        final boolean[] expanded = {false};

        if (!expandDirection && !firstLaunch) {
            imageLayout = inflater.inflate(R.layout.slidingimages_layout_expanded2, view, false);
        } else {
            imageLayout = inflater.inflate(R.layout.slidingimages_layout_collapsed2, view, false);
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

        final ImageView upArrow = (ImageView) imageLayout.findViewById(R.id.slidingimages_layout_arrow);
        descriptionText = (TextView) imageLayout.findViewById(R.id.slidingimages_layout_description_text);
        descriptionTextContent = (TextView) imageLayout.findViewById(R.id.slidingimages_layout_descriptiontext_textView);
        final RelativeLayout descriptionLayout = (RelativeLayout) imageLayout.findViewById(R.id.slidingimages_layout_description_layout);
        descriptionLayout.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        final ResizeAnimation expandAnimation = new ResizeAnimation(descriptionLayout, 800, 0);
        expandAnimation.setDuration(500);
        final ResizeAnimation collapsAnimation = new ResizeAnimation(descriptionLayout, 0, 800);
        collapsAnimation.setDuration(500);
        upArrow.setOnClickListener(this);


        if (expandDirection) {
            descriptionLayout.startAnimation(expandAnimation);
            upArrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_down_white___px);

        } else {
            if (!firstLaunch) {
                descriptionLayout.startAnimation(collapsAnimation);
                upArrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_up_white___px);
            }
        }


        //TODO: Fixa även här så att get endast roteras om width>height
        File file = new File(IMAGES.get(position));
        int rotation = 90;
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (IMAGES.get(position).endsWith("front.jpg")) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            slideImageActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            Matrix m = new Matrix();
            m.setScale(-1, 1);
            WindowManager wm = (WindowManager) slideImageActivity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.heightPixels;


            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), m, false);
            myBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
            myBitmap = RotateBitmap(myBitmap, 90);
            myBitmap = Bitmap.createBitmap(
                    myBitmap, 230
                    ,
                    0,
                    myBitmap.getWidth() - 460,
                    myBitmap.getHeight()
            );


        } else {
            myBitmap = RotateBitmap(myBitmap, 90);
        }

        imageView.setImage(myBitmap);
        setFilter(FILTERS.get(position));
        date.setText(DATES.get(position));
        city.setText(CITIES.get(position));
        direction.setText(DIRECTIONS.get(position));
        descriptionTextContent.setText(DESCRIPTIONS.get(position));

        view.addView(imageLayout, 0);

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded[0]) {
                    descriptionLayout.startAnimation(collapsAnimation);
                    upArrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_up_white___px);
                    expanded[0] = false;
                } else {
                    descriptionLayout.startAnimation(expandAnimation);
                    upArrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_down_white___px);
                    expanded[0] = true;

                }

            }
        });


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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            /*case R.id.slidingimages_layout_arrow:
                int page = mPager.getCurrentItem();

               // performExpandOrCollapseAction();

                break;*/
        }

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
                imageView.setFilter(new IFBrannanFilter(slideImageActivity.getApplicationContext()));
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



    private void performExpandOrCollapseAction() {


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

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}