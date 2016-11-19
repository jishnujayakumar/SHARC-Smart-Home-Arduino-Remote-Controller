package com.jishnu_jayakumar.embeddedsystems.sem7.iiitv.sharc;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    // After 5 seconds redirect to another intent

                    Intent i=new Intent(getBaseContext(),BluetoothActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread to have the desired functionality
        background.start();

    }
}
