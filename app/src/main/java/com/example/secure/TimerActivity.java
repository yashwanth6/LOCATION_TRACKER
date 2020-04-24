package com.example.secure;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class TimerActivity extends Activity  {
    private static TextView countdownTimerText;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private static EditText minutes;
    private static Button startTimer, resetTimer, btn_alerm;
    private CountDownTimer countDownTimer;
    GPSTracker gpsTracker;
    MyDBHelper helper;
    SQLiteDatabase db;
    LocationManager manager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //checkRunTimepermission();
        helper = new MyDBHelper(this);
        gpsTracker = new GPSTracker(this);
        //gpsTracker.getLocation();
        btn_alerm = (Button) findViewById(R.id.btn_alerm);
        countdownTimerText = (TextView) findViewById(R.id.countdownText);
        minutes = (EditText) findViewById(R.id.enterMinutes);
        startTimer = (Button) findViewById(R.id.startTimer);
        resetTimer = (Button) findViewById(R.id.resetTimer);
        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    //minutes.getText().toString();

                    //countDownTimer = s;
                    countDownTimer = null;

                }
                //stopCountdown();//stop count down
                startTimer.setText(getString(R.string.start_timer));//Change text to Start Timer
                countdownTimerText.setText(getString(R.string.timer));//Change Timer text
            }
        });
        startTimer.setOnClickListener(new View.OnClickListener() {



            //Set Listeners over button
            private void setListeners() {
                startTimer.setOnClickListener(this);
                resetTimer.setOnClickListener(this);
            }


            // @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.startTimer:
                        //If CountDownTimer is null then start timer
                        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Toast.makeText(getApplicationContext(), "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();


                            if (countDownTimer == null) {
                                String getMinutes = minutes.getText().toString();//Get minutes from edittexf
                                //Check validation over edittext

                                if (!getMinutes.equals("") && getMinutes.length() > 0) {
                                    int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds

                                    startTimer(noOfMinutes);//start countdown
                                    //startTimer.setText(getString(R.string.stop_timer));//Change Text
                                    //startTimer.stopNestedScroll();

                                }
//                             else {
////                                //Else stop timer and change text
//                                stopCountdown();
//                                startTimer.setText(getString(R.string.start_timer));
//                            }


                        } else
                               Toast.makeText(TimerActivity.this, "Please enter no. of Minutes.", Toast.LENGTH_SHORT).show();//Display toast if edittext is empty

//
                        }else  {
                        buildAlertMessageNoGps();
                    }
                    //stopCountdown();

                        break;
                    case R.id.resetTimer:
                        stopCountdown();//stop count down
                        startTimer.setText(getString(R.string.start_timer));//Change text to Start Timer
                        countdownTimerText.setText(getString(R.string.timer));//Change Timer text
                        break;
                }
            }


            //Stop Countdown method
            private void stopCountdown() {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    //minutes.getText().toString();

                    //countDownTimer = s;
                    countDownTimer = null;

                }
            }


            //Start Countodwn method
            private void startTimer(final int noOfMinutes) {
                countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished;
                        //Convert milliseconds into hour,minute and seconds
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        countdownTimerText.setText(hms);//set text
                    }

                    public void onFinish() {

                        countdownTimerText.setText("TIME'S UP!!");//On finish change timer text
                        String url = "http://maps.google.com/?q=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
                        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();

                        try {
                            double lat = gpsTracker.getLatitude();
                            double lon = gpsTracker.getLongitude();

                            helper = new MyDBHelper(getBaseContext());
                            db = helper.getReadableDatabase();
                            Cursor c = db.rawQuery("select phnumber from addcontact", null);
                            if (c != null) {
                                c.moveToFirst();
                                do {
                                    //while (c.moveToNext()) {
                                    String number = c.getString(c.getColumnIndex("phnumber"));
                                    String geoAddress = url;
                                    Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                    android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                                    smsManager.sendTextMessage(number, null, geoAddress, null, null);
                                    Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();

                                } while (c.moveToNext());
                            }
//

                        } catch (Exception e) {
                            e.printStackTrace();


                        }
                    }
                }.start();
            }

            public void checkRunTimepermission() {
                //String camerapermission = Manifest.permission.CAMERA;
                String gpsPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
                String permission = Manifest.permission.ACCESS_FINE_LOCATION;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int has_permission = checkCallingOrSelfPermission(gpsPermission);
                    int allow_permission = checkCallingOrSelfPermission(permission);

                    String[] permissions = new String[]{gpsPermission, permission};
                    if (has_permission != PackageManager.PERMISSION_GRANTED && allow_permission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, 100);
                    } else {
                        // permissions must accept to use fetures.
                    }
                } else {
                    // callCamera();

                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}










