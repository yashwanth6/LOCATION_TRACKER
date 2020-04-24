package com.example.secure;

import android.app.Activity;
import android.os.CountDownTimer;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Incoming2Activity extends Activity {
    TextView txt_sec, txt_min;
    CountDownTimer countDownTimer;
    ImageView call_end;
    int sec = 0;
    int min = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_incoming2);
        txt_sec = (TextView) findViewById(R.id.txt_duration);
        call_end = (ImageView) findViewById(R.id.call_end);
        call_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });
        txt_min = (TextView) findViewById(R.id.txt_sec);


        new CountDownTimer(100000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                sec++;
                if (sec == 60) {
                    sec = 0;
                    min++;
                }

                txt_sec.setText(String.valueOf(sec < 10 ? "0"+sec : sec));
                txt_min.setText(String.valueOf(min < 10 ? "0"+min : min));

            }

            @Override
            public void onFinish() {


            }
        }.start();
    }

    @Override
    public void onBackPressed() {

    }
    }

