package com.beatem.tj;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Runnable  {

    CatLoadingView mView;
    Thread mThread;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        mThread = new Thread();

        mThread.run();

       mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");


        //findViewById(R.id.button).setOnClickListener(
        //      new View.OnClickListener() {
        //        @Override public void onClick(View v) {
        //          mView.show(getSupportFragmentManager(), "");
        //    }
        //});
    }
    public void run(){

        try {
            wait(3000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.finish();
    }



    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "hej", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}