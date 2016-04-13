package com.beatem.tj;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Martin on 2016-04-13.
 */
public class PlaneLoadingView extends DialogFragment {


    public PlaneLoadingView(){}

    Animation operationAnim;

    Dialog mDialog;

    View plane, globe;


    @Override public Dialog onCreateDialog(Bundle savedInstanceState){
        if(mDialog==null){
            mDialog=new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setContentView(R.layout.welcome_screen);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);

            operationAnim = new RotateAnimation(160f, 0f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            operationAnim.setRepeatCount(Animation.INFINITE);
            operationAnim.setDuration(2000);


            LinearInterpolator lin = new LinearInterpolator();
            operationAnim.setInterpolator(lin);

            View view = mDialog.getWindow().getDecorView();

            plane = view.findViewById(R.id.airplane);

            operationAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }return mDialog;
    }
    @Override public void onResume(){
        super.onResume();
        plane.setAnimation(operationAnim);
    }

    @Override public void onPause(){
        super.onPause();
        operationAnim.reset();
        plane.clearAnimation();
    }
    @Override public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        mDialog = null;
        System.gc();
    }

}
