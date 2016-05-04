package com.beatem.tj.OldTripsViewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.beatem.tj.MyLocation;
import com.beatem.tj.MySqLite;
import com.beatem.tj.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by JoelBuhrman on 16-05-04.
 */
public class OnlyOneImageAcitivity extends Activity implements View.OnClickListener{
    private GPUImageView mGPUImage;
    private ImageView arrow;
    private RelativeLayout descriptionLayout;
    private ResizeAnimation expandAnimation, collapsAnimation;
    private boolean expanded;
    private MyLocation location;
    private TextView description, city, date, direction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.only_one_image_activity);
        init();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        arrow.setOnClickListener(this);

    }

    private void init() {
        mGPUImage= (GPUImageView)findViewById(R.id.image);
        arrow= (ImageView)findViewById(R.id.slidingimages_layout_arrow);
        descriptionLayout= (RelativeLayout)findViewById(R.id.onlyOneImageActivity_layout_description_layout);
        description= (TextView)findViewById(R.id.slidingimages_layout_descriptiontext_textView);
        city= (TextView)findViewById(R.id.slidingimages_layout_city);
        date=(TextView)findViewById(R.id.slidingimages_layout_date);
        direction=(TextView)findViewById(R.id.slidingimages_layout_direction);


        expanded = false;
        expandAnimation = new ResizeAnimation(descriptionLayout, 800, 0);
        expandAnimation.setDuration(500);
        collapsAnimation = new ResizeAnimation(descriptionLayout, 0, 800);
        collapsAnimation.setDuration(500);


        MySqLite mySqLite = new MySqLite(this);
/*

Data från kartan
 */
        Bundle data = getIntent().getExtras();
        location= (MyLocation) data.getParcelable("location");
        File file= new File(location.getPicpath());
        int rotation = 90;
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


        mGPUImage.setImage(handleBitmap(myBitmap));
        setFilter(location.getFilter());
        description.setText(location.getText());
        date.setText(location.getDate());
        city.setText(getCity(location.getLatitude(), location.getLongditude()));
        direction.setText(location.getDirection());



    }

    private void setFilter(String s) {
        switch (s){
            case "filter_normal":
                mGPUImage.setFilter(new GPUImageFilter());
                break;
            case "filter_1977":
                mGPUImage.setFilter(new IF1977Filter(getApplicationContext()));
                break;
            case "filter_amaro":
                mGPUImage.setFilter(new IFAmaroFilter(getApplicationContext()));
                break;
            case "filter_brannan":
                mGPUImage.setFilter(new IF1977Filter(getApplicationContext()));
                break;
            case "filter_early_bird":
                mGPUImage.setFilter(new IFEarlybirdFilter(getApplicationContext()));
                break;
            case "filter_hefe":
                mGPUImage.setFilter(new IFHefeFilter(getApplicationContext()));
                break;
            case "filter_hudson":
                mGPUImage.setFilter(new IFHudsonFilter(getApplicationContext()));
                break;
            case "filter_inkwell":
                mGPUImage.setFilter(new IFInkwellFilter(getApplicationContext()));
                break;
            case "filter_lomofi":
                mGPUImage.setFilter(new IFLomoFilter(getApplicationContext()));
                break;
            case "filter_lord_kelvin":
                mGPUImage.setFilter(new IFLordKelvinFilter(getApplicationContext()));
                break;
            case "filter_nashville":
                mGPUImage.setFilter(new IFNashvilleFilter(getApplicationContext()));
                break;
            case "filter_rise":
                mGPUImage.setFilter(new IFRiseFilter(getApplicationContext()));
                break;
            case "filter_sierra":
                mGPUImage.setFilter(new IFSierraFilter(getApplicationContext()));
                break;
            case "filter_sutro":
                mGPUImage.setFilter(new IFSutroFilter(getApplicationContext()));
                break;
            case "filter_toaster":
                mGPUImage.setFilter(new IFToasterFilter(getApplicationContext()));
                break;
            case "filter_valencia":
                mGPUImage.setFilter(new IFValenciaFilter(getApplicationContext()));
                break;
            case "filter_walden":
                mGPUImage.setFilter(new IFWaldenFilter(getApplicationContext()));
                break;
            case "filter_xproii":
                mGPUImage.setFilter(new IFXprollFilter(getApplicationContext()));
                break;
            default:
                break;
        }
    }

    private Bitmap handleBitmap(Bitmap myBitmap) {
        if (location.getPicpath().endsWith("front.jpg")) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            Matrix m = new Matrix();
            m.setScale(-1, 1);
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
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
        return myBitmap;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.slidingimages_layout_arrow:
                performExpandOrCollapseAction();
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void performExpandOrCollapseAction() {

        if (expanded) {
            descriptionLayout.startAnimation(collapsAnimation);
            arrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_up_white___px);
            expanded = false;
        } else {
            descriptionLayout.startAnimation(expandAnimation);
            arrow.setImageResource(R.drawable.vector_drawable_ic_keyboard_arrow_down_white___px);
            expanded = true;

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

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public String getCity(float latitude, float longitude) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String[] total = new String[]{};
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString(); //This is the complete address.
            total = fnialAddress.split(" ");

        } catch (IOException e) {
        } catch (NullPointerException e) {
        }

        return total[total.length - 1];
    }
}
