package com.beatem.tj.Camera;

/**
 * Created by JoelBuhrman on 16-04-22.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beatem.tj.CapturedImage.ImageViewingActivity;
import com.beatem.tj.R;
import com.beatem.tj.SaveSharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;


public class CameraActivity extends Activity implements OnClickListener {

    private static String timeStamp;
    private static String currentTrip;
    static boolean frontCamera = false;
    boolean autoFlashActivated = true;
    private TextView direction;
    private Compass compass;
    private ImageView flash;
    private GLSurfaceView glSurfaceView;

    private GPUImage mGPUImage;
    private CameraHelper mCameraHelper;
    private CameraLoader mCamera;


    private RelativeLayout cameraView; Bitmap bmp;



    ViewGroup.LayoutParams lp;
    View cameraSwitchView;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cameraView=(RelativeLayout)findViewById(R.id.cameraView);
        init();
        activateClickListener();


    }

    public void TakeScreenshot(){    //THIS METHOD TAKES A SCREENSHOT AND SAVES IT AS .jpg
        Random num = new Random();
        int nu=num.nextInt(1000); //PRODUCING A RANDOM NUMBER FOR FILE NAME
        cameraView.setDrawingCacheEnabled(true); //CamView OR THE NAME OF YOUR LAYOUR
        cameraView.buildDrawingCache(true);
        bmp = Bitmap.createBitmap(cameraView.getDrawingCache());
        cameraView.setDrawingCacheEnabled(false); // clear drawing cache
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream fis = new ByteArrayInputStream(bitmapdata);

        String picId=String.valueOf(nu);
        String myfile="Ghost"+picId+".jpeg";

        File dir_image = new  File(Environment.getExternalStorageDirectory()+//<---
                File.separator+"Ultimate Entity Detector");          //<---
        dir_image.mkdirs();                                                  //<---
        //^IN THESE 3 LINES YOU SET THE FOLDER PATH/NAME . HERE I CHOOSE TO SAVE
        //THE FILE IN THE SD CARD IN THE FOLDER "Ultimate Entity Detector"

        try {
            File tmpFile = new File(dir_image,myfile);
            FileOutputStream fos = new FileOutputStream(tmpFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
            Toast.makeText(getApplicationContext(),
                    "The file is saved at :SD/Ultimate Entity Detector",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void activateClickListener() {
        findViewById(R.id.flash_icon).setOnClickListener(this);
    }

    private void init() {

        currentTrip = SaveSharedPreferences.getCurrentTrip(getApplicationContext());

        if (getIntent().getStringExtra("camType") != null && getIntent().getStringExtra("camType").equals("front")) {
            frontCamera = true;
        }

        glSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceView);

        direction = (TextView) findViewById(R.id.direction);
        flash = (ImageView) findViewById(R.id.flash_icon);
        compass = new Compass(this, direction, "back");
        compass.start();


        lp = new ViewGroup.LayoutParams(300, 300);


        findViewById(R.id.button_capture).setOnClickListener(this);

        mGPUImage = new GPUImage(this);
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));

        mCameraHelper = new CameraHelper(this);
        mCamera = new CameraLoader();


        cameraSwitchView = findViewById(R.id.img_switch_camera);
        cameraSwitchView.setOnClickListener(this);
        if (!mCameraHelper.hasFrontCamera() || !mCameraHelper.hasBackCamera()) {
            cameraSwitchView.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.onResume();

    }

    @Override
    protected void onPause() {
        mCamera.onPause();
        super.onPause();
    }


    @Override
    public void onClick(final View v) {


        switch (v.getId()) {


            case R.id.button_capture:
                if (mCamera.mCameraInstance.getParameters().getFocusMode().equals(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {

                    takePicture();
                } else {
                    mCamera.mCameraInstance.autoFocus(new Camera.AutoFocusCallback() {

                        @Override
                        public void onAutoFocus(final boolean success, final Camera camera) {

                            takePicture();
                        }
                    });
                }
                break;

            case R.id.img_switch_camera:

                mCamera.switchCamera();


                if (frontCamera) {
                    frontCamera = false;
                    flash.setImageResource(R.drawable.vector_drawable_ic_flash_auto_white___px);

                } else {
                    frontCamera = true;
                    flash.setImageResource(0);

                }

                break;

            case R.id.flash_icon:
                mCamera.changeFlashMode();
                break;
        }
    }

    private void takePicture() {
        // TODO get a size that is about the size of the screen
        Camera.Parameters params = mCamera.mCameraInstance.getParameters();
        //params.setRotation(90);
        Log.e("ASDF", "Här under borde det skrivas!:");

        mCamera.mCameraInstance.setParameters(params);
        for (Camera.Size size : params.getSupportedPictureSizes()) {
            Log.e("ASDF", "Supported: " + size.width + "x" + size.height);
        }


        mCamera.mCameraInstance.takePicture(null, null,
                new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, final Camera camera) {

                        final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        if (pictureFile == null) {
                            Log.d("ASDF",
                                    "Error creating media file, check storage permissions");
                            return;
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Log.d("ASDF", "File not found: " + e.getMessage());
                        } catch (IOException e) {
                            Log.d("ASDF", "Error accessing file: " + e.getMessage());
                        }

                        data = null;
                        Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
                        // mGPUImage.setImage(bitmap);
                        final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);



                        /*
                        Filnamnet
                         */
                        long time = System.currentTimeMillis();
                        String camType = "back";
                        if (frontCamera) {
                            camType = "front";
                        } else
                            mGPUImage.saveToPictures(bitmap, "TJ",
                                    currentTrip + timeStamp + camType + ".jpg",
                                    new OnPictureSavedListener() {

                                        @Override
                                        public void onPictureSaved(final Uri
                                                                           uri) {
                                            //pictureFile.delete();
                                            view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                                        }
                                    });


                        Intent intent = new Intent(getApplicationContext(), ImageViewingActivity.class);
                        intent.putExtra("file_name", pictureFile.getAbsolutePath());
                        intent.putExtra("captureTime", time);
                        //intent.putExtra("file_name2", "/storage/emulated/0/Pictures/TJ/" + currentTrip + timeStamp +camType+ "%.jpg");
                        intent.putExtra("direction", direction.getText().toString());
                        if (frontCamera) {
                            intent.putExtra("camera_type", "front");
                        } else {
                            intent.putExtra("camera_type", "back");
                        }

                        startActivity(intent);
                        finish();
                    }
                });
    }


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static File getOutputMediaFile(final int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TJ");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            String camType= "back";
            if (frontCamera){
                camType="front";
            }

            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    currentTrip + timeStamp + camType + ".jpg");

        } else if (type == MEDIA_TYPE_VIDEO)

        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else

        {
            return null;
        }

        return mediaFile;
    }


    private class CameraLoader {

        private int mCurrentCameraId = 0;
        private Camera mCameraInstance;

        public void onResume() {
            if (frontCamera) {
                mCurrentCameraId = 1;
                flash.setImageResource(0);

            }
            setUpCamera(mCurrentCameraId);
        }

        public void onPause() {
            releaseCamera();
        }

        public void switchCamera() {
            releaseCamera();
            mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
            setUpCamera(mCurrentCameraId);
            compass.changeCam();


        }

        private void setUpCamera(final int id) {
            mCameraInstance = getCameraInstance(id);

            Parameters parameters = mCameraInstance.getParameters();
            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }


            mCameraInstance.setParameters(parameters);

            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    CameraActivity.this, mCurrentCameraId);

            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);


        }

        /**
         * A safe way to get an instance of the Camera object.
         */
        private Camera getCameraInstance(final int id) {
            Camera c = null;
            try {
                c = mCameraHelper.openCamera(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return c;
        }

        private void releaseCamera() {
            mCameraInstance.setPreviewCallback(null);
            mCameraInstance.release();
            mCameraInstance = null;
        }

        public void changeFlashMode() {
            List<String> mSupportedFlashModes = mCameraInstance.getParameters().getSupportedFlashModes();

            if (autoFlashActivated) {
                if (mSupportedFlashModes != null && mSupportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                    Camera.Parameters parameters = mCameraInstance.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCameraInstance.setParameters(parameters);
                    autoFlashActivated = false;

                    flash.setImageResource(R.drawable.vector_drawable_ic_flash_off_white___px);

                }
            } else {

                if (mSupportedFlashModes != null && mSupportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                    Camera.Parameters parameters = mCameraInstance.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    mCameraInstance.setParameters(parameters);
                    autoFlashActivated = true;
                    flash.setImageResource(R.drawable.vector_drawable_ic_flash_auto_white___px);
                }

            }
        }
    }
}