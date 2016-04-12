package com.beatem.tj;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by Ludvig on 2016-04-12.
 */
public class WelcomeScreen extends AppCompatActivity {

   protected static final int TIMER_RUNTIME = 10000;
   protected ProgressBar pb;
    protected boolean mbActive;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        pb= (ProgressBar)findViewById(R.id.progressBar);

        final Thread timeThread = new Thread(){
            @Override
            public void run(){
                mbActive = true;
                try{
                    int waited = 0;
                    while(mbActive && (waited<TIMER_RUNTIME)){
                        sleep(200);
                        if(mbActive){
                            waited+=200;
                            updateProgress(waited);
                        }

                    }
                }catch (InterruptedException e){

                }finally {
                    onContinue();
                }
            }
        };
        timeThread.start();


    }

    public void updateProgress(final int timePassed){
        if(null!=pb){
            final int progress = pb.getMax()*timePassed/TIMER_RUNTIME;
            pb.setProgress(progress);
        }
    }

    public void onContinue(){
        Log.d("hejhopp", "lololol");
    }


}
