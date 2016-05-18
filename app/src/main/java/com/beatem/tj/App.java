package com.beatem.tj;

/**
 * Created by JoelBuhrman on 16-05-18.
 */
import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.osama.firecrasher.CrashListener;
import com.osama.firecrasher.FireCrasher;

/**
 * Created by Osama Raddad.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        FireCrasher.install(this, new CrashListener() {


            @Override
            public void onCrash(Throwable throwable, final Activity activity) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();

//                start the recovering process
                recover(activity);

                //you need to add your crash reporting tool here
                //Ex: Crashlytics.logException(throwable);
            }
        });
        super.onCreate();
    }
}