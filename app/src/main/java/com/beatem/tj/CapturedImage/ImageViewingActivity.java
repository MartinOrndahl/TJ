/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.beatem.tj.CapturedImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beatem.tj.Camera.CameraActivity;
import com.beatem.tj.CapturedImage.GPUImageFilterTools.FilterType;
import com.beatem.tj.CustomDialogCommandsClass;
import com.beatem.tj.MapsActivity;
import com.beatem.tj.MyLocation;
import com.beatem.tj.MySqLite;
import com.beatem.tj.R;
import com.beatem.tj.SaveSharedPreferences;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class ImageViewingActivity extends Activity implements OnClickListener, OnPictureSavedListener {
    private static int mode;
    private static final int FILTER_MODE = 0;
    private static final int REGULAR_MODE = 1;
    protected static final int RESULT_SPEECH = 1;
    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    private static final int REQUEST_PICK_IMAGE = 1;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private GPUImageView mGPUImageView;
    private GPUImage mGPUImage;
    private LinearLayout imageLL;
    private Bitmap myBitmap;
    private String currentFilter = "filter_normal";

    private boolean expanded;
    private RelativeLayout descriptionLayout, filterLayout, topRelLayout;
    ResizeAnimation expandAnimation, collapsAnimation, expandFilterAnimation, collapsFilterAnimation;
    CustomDialogCommandsClass cdcc;


    private ImageView image;
    private String dateValue, cityNameValue, directionValue;
    private TextView date, cityName, hide, direction;
    File imgFile;
    private String path, cameraType = "back";

    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton penAction, filterAction, saveAction, deleteAction;
    private MyLocation location;
    private boolean fromMain;
    private ImageButton mic, info;
    private EditText editText;


    private GPUImageFilterTools.FilterList filters = new GPUImageFilterTools.FilterList();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        activateClickListeners();



    }

    private void activateClickListeners() {
        findViewById(R.id.filter_normal).setOnClickListener(this);
        findViewById(R.id.filter_amaro).setOnClickListener(this);
        findViewById(R.id.filter_brannan).setOnClickListener(this);
        findViewById(R.id.filter_early_bird).setOnClickListener(this);
        findViewById(R.id.filter_hefe).setOnClickListener(this);
        findViewById(R.id.filter_hudson).setOnClickListener(this);
        findViewById(R.id.filter_in1977).setOnClickListener(this);
        findViewById(R.id.filter_inkwell).setOnClickListener(this);
        findViewById(R.id.filter_lomofi).setOnClickListener(this);
        findViewById(R.id.filter_lord_kelvin).setOnClickListener(this);
        findViewById(R.id.filter_nashville).setOnClickListener(this);
        findViewById(R.id.filter_rise).setOnClickListener(this);
        findViewById(R.id.filter_sierra).setOnClickListener(this);
        findViewById(R.id.filter_sutro).setOnClickListener(this);
        findViewById(R.id.filter_toaster).setOnClickListener(this);
        findViewById(R.id.filter_valencia).setOnClickListener(this);
        findViewById(R.id.filter_walden).setOnClickListener(this);
        findViewById(R.id.filter_xproii).setOnClickListener(this);


        findViewById(R.id.gpuimage).setOnClickListener(this);


          /*
        FloatingActionButtons
         */
        penAction.setOnClickListener(this);
        filterAction.setOnClickListener(this);
        saveAction.setOnClickListener(this);
        deleteAction.setOnClickListener(this);


         /*
        Description komponenter
         */
        mic.setOnClickListener(this);
        info.setOnClickListener(this);
        hide.setOnClickListener(this);


    }

    private void init() {


        mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);
        mGPUImage = new GPUImage(this);
        imageLL = (LinearLayout) findViewById(R.id.images_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;


/*
Om vi kommer från kartan
 */
        Bundle data = getIntent().getExtras();
        location = (MyLocation) data.getParcelable("location");
        if (location != null) {
            fromMain = true;
        } else {
            fromMain = false;
        }




           /*
        Check om vi kommer från en nytagen bild
         */
        String fileName;
        if (fromMain) {
            fileName = location.getPicpath();
        } else {
            fileName = getIntent().getStringExtra("file_name");

        }


        if (fileName != null) {
            imgFile = new File(fileName);
            boolean rotate = false;
            Log.e("marcusäger ", fileName);
            if (imgFile.exists()) {
                path = imgFile.getAbsolutePath();
                mGPUImageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                myBitmap = BitmapFactory.decodeFile(path);
                if (myBitmap.getWidth() > myBitmap.getHeight()) {
                    rotate = true;
                }


                //TODO: Rotera bitmap om height<width istället för att kolla cameratype

                if (!fromMain) {
                    cameraType = getIntent().getStringExtra("camera_type").toString();
                } else {
                    cameraType = "back";
                }
                FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                if (rotate) {
                    myBitmap = RotateBitmap(myBitmap, 90);
                }
                if (cameraType.equals("front")) {
                    Matrix m = new Matrix();
                    m.setScale(-1, 1);
                    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
                    wm.getDefaultDisplay().getMetrics(displayMetrics);
                    int screenWidth = displayMetrics.heightPixels;


                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), m, false);
                    myBitmap=RotateBitmap(myBitmap, 180);
                    //myBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

                    myBitmap = Bitmap.createBitmap(
                            myBitmap, 230
                            ,
                            0,
                            myBitmap.getWidth() - 460,
                            myBitmap.getHeight()
                    );


                }
                mGPUImageView.setImage(myBitmap);
                mGPUImage.setImage(myBitmap);


            } else {
                Toast.makeText(this, "Couldn't find image: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }


        filters.addFilter("1977", FilterType.I_1977);
        filters.addFilter("Amaro", FilterType.I_AMARO);
        filters.addFilter("Brannan", FilterType.I_BRANNAN);
        filters.addFilter("Earlybird", FilterType.I_EARLYBIRD);
        filters.addFilter("Hefe", FilterType.I_HEFE);
        filters.addFilter("Hudson", FilterType.I_HUDSON);
        filters.addFilter("Inkwell", FilterType.I_INKWELL);
        filters.addFilter("Lomo", FilterType.I_LOMO);
        filters.addFilter("LordKelvin", FilterType.I_LORDKELVIN);
        filters.addFilter("Nashville", FilterType.I_NASHVILLE);
        filters.addFilter("Rise", FilterType.I_RISE);
        filters.addFilter("Sierra", FilterType.I_SIERRA);
        filters.addFilter("sutro", FilterType.I_SUTRO);
        filters.addFilter("Toaster", FilterType.I_TOASTER);
        filters.addFilter("Valencia", FilterType.I_VALENCIA);
        filters.addFilter("Walden", FilterType.I_WALDEN);
        filters.addFilter("Xproll", FilterType.I_XPROII);
        filters.addFilter("Contrast", FilterType.CONTRAST);
        filters.addFilter("Brightness", FilterType.BRIGHTNESS);
        filters.addFilter("Sepia", FilterType.SEPIA);
        filters.addFilter("Vignette", FilterType.VIGNETTE);
        filters.addFilter("ToneCurve", FilterType.TONE_CURVE);
        filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA);


        /*
ställer in vilket mode vi befinner oss i
 */
        mode = REGULAR_MODE;


           /*
        MainBilden med dess komponenter
         */
        date = (TextView) findViewById(R.id.chosen_image_date);

        //date.setText(handleDate(formattedDate)); // Ändra till datum då bilden togs
        cityName = (TextView) findViewById(R.id.cityName);
        image = (ImageView) findViewById(R.id.image);
        direction = (TextView) findViewById(R.id.main_direction);
        filterLayout = (RelativeLayout) findViewById(R.id.filterLayout);


        if (!fromMain) {
            direction.setText(getIntent().getStringExtra("direction").toString());
        } else {
            direction.setText(location.getDirection());
        }
        date.setText(handleDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date())));


        cityName.setText(getCity((float) MapsActivity.currentlocation.latitude,
                (float) MapsActivity.currentlocation.longitude));



             /*
        Komponenterna i Descriptiondelen
         */
        editText = (EditText) findViewById(R.id.editText);
        info = (ImageButton) findViewById(R.id.info);
        mic = (ImageButton) findViewById(R.id.mic);
        descriptionLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);




        /*
        Vår floatingbutton med den knappar
         */
        penAction = (FloatingActionButton) findViewById(R.id.action_pen2);
        filterAction = (FloatingActionButton) findViewById(R.id.action_filter);
        saveAction = (FloatingActionButton) findViewById(R.id.action_save);
        deleteAction = (FloatingActionButton) findViewById(R.id.action_delete);

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

         /*
        Det som sköter expandering och kollaps
         */
        expanded = false;
        expandAnimation = new ResizeAnimation(descriptionLayout, 800, 0);
        expandAnimation.setDuration(500);
        collapsAnimation = new ResizeAnimation(descriptionLayout, 0, 800);
        collapsAnimation.setDuration(500);
        expandFilterAnimation = new ResizeAnimation(filterLayout, 400, 0);
        expandFilterAnimation.setDuration(500);
        collapsFilterAnimation = new ResizeAnimation(filterLayout, 0, 400);
        collapsFilterAnimation.setDuration(500);
        hide = (TextView) findViewById(R.id.hide2);

         /*
        Custom dialogerna
         */
        cdcc = new CustomDialogCommandsClass(ImageViewingActivity.this);


    }

    private String handleDate(String formattedDate) {
        String returnValue = formattedDate;

        if (returnValue.charAt(0) == '0') {
            returnValue = returnValue.substring(1, returnValue.length());
        }

        String[] temp = returnValue.split("-");
        StringBuilder sb = new StringBuilder();
        sb.append(temp[0] + " ");
        String month = "";

        switch (temp[1]) {
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
            default:
                break;

        }


        char t = Character.toUpperCase(month.charAt(0));
        month.replace(month.charAt(0), t);
        sb.append(t);
        sb.append(month.substring(1, month.length()) + " ");
        sb.append(temp[2]);
        return sb.toString();
    }


    @Override
    public void onClick(final View v) {
        GPUImageFilter filter;
        switch (v.getId()) {


            case R.id.filter_normal:
                switchFilterTo(new GPUImageFilter());
                currentFilter = "filter_normal";
                break;

            case R.id.filter_in1977:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(0));
                switchFilterTo(filter);
                currentFilter = "filter_1977";
                break;

            case R.id.filter_amaro:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(1));
                switchFilterTo(filter);
                currentFilter = "filter_amaro";
                break;

            case R.id.filter_brannan:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(2));
                switchFilterTo(filter);
                currentFilter = "filter_brannan";
                break;

            case R.id.filter_early_bird:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(3));
                switchFilterTo(filter);
                currentFilter = "filter_early_bird";
                break;

            case R.id.filter_hefe:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(4));
                switchFilterTo(filter);
                currentFilter = "filter_hefe";
                break;

            case R.id.filter_hudson:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(5));
                switchFilterTo(filter);
                currentFilter = "filter_hudson";
                break;

            case R.id.filter_inkwell:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(6));
                switchFilterTo(filter);
                currentFilter = "filter_inkwell";
                break;

            case R.id.filter_lomofi:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(7));
                switchFilterTo(filter);
                currentFilter = "filter_lomofi";
                break;

            case R.id.filter_lord_kelvin:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(8));
                switchFilterTo(filter);
                currentFilter = "filter_lord_kelvin";
                break;

            case R.id.filter_nashville:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(9));
                switchFilterTo(filter);
                currentFilter = "filter_nashville";
                break;

            case R.id.filter_rise:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(10));
                switchFilterTo(filter);
                currentFilter = "filter_rise";
                break;

            case R.id.filter_sierra:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(11));
                switchFilterTo(filter);
                currentFilter = "filter_sierra";
                break;

            case R.id.filter_sutro:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(12));
                switchFilterTo(filter);
                currentFilter = "filter_sutro";
                break;

            case R.id.filter_toaster:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(13));
                switchFilterTo(filter);
                currentFilter = "filter_toaster";
                break;

            case R.id.filter_valencia:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(14));
                switchFilterTo(filter);
                currentFilter = "filter_valencia";
                break;

            case R.id.filter_walden:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(15));
                switchFilterTo(filter);
                currentFilter = "filter_walden";
                break;

            case R.id.filter_xproii:
                filter = GPUImageFilterTools.createFilterForType(ImageViewingActivity.this, filters.filters.get(16));
                switchFilterTo(filter);
                currentFilter = "filter_xproii";
                break;







            /*
            FloatingactionButtons
             */
            case R.id.action_pen2:
                performExpandOrCollapseAction();
                break;
            case R.id.action_filter:
                performFilterAction();
                break;
            case R.id.action_save:
                saveImageAction();


                break;
            case R.id.action_delete:
                deleteImageAction();
                break;


            /*
            Descriptions komponenter
             */
            case R.id.mic:
                promptSpeechInput();
                break;
            case R.id.info:
                cdcc.show();
                break;
            case R.id.hide2:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                performExpandOrCollapseAction();

                break;


            case R.id.gpuimage:
                if (mode == FILTER_MODE) {
                    performFilterAction();
                }
                break;


            default:
                break;
        }
        mGPUImageView.requestRender();

    }


    @Override
    public void onPictureSaved(final Uri uri) {
        Toast.makeText(this, "Saved: " + uri.toString(), Toast.LENGTH_SHORT)
                .show();
    }


    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(
                filter.getClass()))) {
            mFilter = filter;
            mGPUImageView.setFilter(mFilter);
            mGPUImage.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
        }
    }


    private String handleSpeech(String descriptionText) {

        /**
         * Tar bort null från början och sätter första till stor bokstav
         */

        if (descriptionText.contains("Radera allt") || descriptionText.contains("radera allt")) {
            return "delete";
        } else {
            String temp = descriptionText;
            //temp= handleFirstLetter(temp);

            temp = temp.replaceAll(" frågetecken", "?");
            temp = temp.replaceAll(" punkt", ".");
            temp = temp.replaceAll(" utropstecken", "!");
            temp = temp.replaceAll(" kommatecken", ",");

            return handleCapitalLetters(temp);
        }

    }

    private String handleCapitalLetters(String s) {
        String temp = s;
        char first = s.charAt(0);
        first = Character.toUpperCase(first);
        temp = first + temp.substring(1, temp.length());

        StringBuilder sb = new StringBuilder();
        sb.append(temp);

        for (int i = 2; i < sb.length() - 1; i++) {
            char c = temp.charAt(i - 2);
            char t = temp.charAt(i);

            if (c == '.' || c == '!' || c == '?') {
                t = Character.toUpperCase(t);

                sb.replace(i, i + 1, String.valueOf(t));
            }
        }


        return sb.toString();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");


        try {
            startActivityForResult(intent, RESULT_SPEECH);

        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String s = handleSpeech(text.get(0));
                    if (s.equals("delete")) {
                        editText.setText("");

                    } else {


                        editText.setText(editText.getText().toString() + handleSpeech(text.get(0)));
                    }
                }
                break;
            }

        }
    }

    private void handleImage(final Uri selectedImage) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), selectedImage);
            float width = bitmap.getWidth();
            float height = bitmap.getHeight();
            float ratio = width / height;
            mGPUImageView.setRatio(ratio);
            mGPUImageView.setImage(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performFilterAction() {
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        RelativeLayout.LayoutParams top_rel_btn = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        if (expanded) {
            descriptionLayout.startAnimation(collapsAnimation);
            expanded = false;
        }

        switch (mode) {

            case FILTER_MODE:
                filterLayout.startAnimation(collapsFilterAnimation);
                menuMultipleActions.setVisibility(View.VISIBLE);
                mode = REGULAR_MODE;
                break;

            case REGULAR_MODE:
                menuMultipleActions.collapse();
                menuMultipleActions.setVisibility(View.INVISIBLE);
                filterLayout.startAnimation(expandFilterAnimation);
                mode = FILTER_MODE;

                break;
            default:
                break;

        }

        //rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //filterLayout.setLayoutParams(rel_btn);


    }

    private void performExpandOrCollapseAction() {

        if (expanded) {
            descriptionLayout.startAnimation(collapsAnimation);
            expanded = false;
        } else {
            descriptionLayout.startAnimation(expandAnimation);
            expanded = true;

        }
    }


    public String getCity(float latitude, float longitude) {


        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            return addresses.get(0).getLocality();
        }
        return "";
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


    public void startCamera() {
        startActivity(new Intent(this, CameraActivity.class).putExtra("camType", cameraType));
    }


    private void deleteImageAction() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing()) {
                    new AlertDialog.Builder(ImageViewingActivity.this)
                            .setTitle("Delete Image?")
                            .setMessage("Are you sure you want to delete this image?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    File f = new File(getIntent().getStringExtra("file_name"));

                                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                    f.delete();
                                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{

                                                    f.getAbsolutePath()},

                                            null, new MediaScannerConnection.OnScanCompletedListener() {

                                                public void onScanCompleted(String path, Uri uri)

                                                {


                                                }

                                            });
                                    startCamera();
                                    finish();


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
                }
            }
        });
    }


    private void saveImageAction() {

        /*
        Spara till SQL
         */


        mGPUImageView.saveToPictures("haj", "haj", null);


        MySqLite sqLite = new MySqLite(getApplicationContext());
        if (sqLite.addLocation(new MyLocation(path, (float) MapsActivity.currentlocation.longitude,
                (float) MapsActivity.currentlocation.latitude, SaveSharedPreferences.getCurrentTrip(getApplicationContext()), editText.getText().toString(),
                direction.getText().toString(), currentFilter, date.getText().toString()
        ))) {
            Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "You've already taken a photo here", Toast.LENGTH_SHORT).show();
        }

    }


    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
