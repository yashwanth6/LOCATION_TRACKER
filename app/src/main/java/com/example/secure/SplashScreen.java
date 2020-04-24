package com.example.secure;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;



public class SplashScreen extends Activity {
    private final Handler mHandler = new Handler();
    private static final int duration = 3000;
    TextView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
       final boolean frstStart= prefs.getBoolean("frstStart",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if(frstStart){
                    Intent i = new Intent(SplashScreen.this, HelpActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(SplashScreen.this, SimpleButtonActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, duration);
    }
}
