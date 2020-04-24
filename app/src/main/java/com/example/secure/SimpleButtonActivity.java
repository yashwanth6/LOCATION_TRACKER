package com.example.secure;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SimpleButtonActivity extends Activity  {
    Button btn_incomingCall, btn_alerm, btn_timer, btn_guardian,btn_sensor;
    MediaPlayer mediaPlayer;
    AlertDialog alert;
    ToggleButton mToggleButton;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    TextView textView;
    SensorManager sensorManager;
//    Sensor sensor;
//    SensorActivity sensorActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_simple_button);
//        sensorActivity=new SensorActivity();
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //SensorActivity sensorActivity = new SensorActivity();
        Intent intent = new Intent(SimpleButtonActivity.this, GPSTracker.class);
        startService(intent);
        checkRunTimepermission();
        //btn_sensor = findViewById(R.id.sensor);
        btn_incomingCall = (Button) findViewById(R.id.btn_incomingCall);
        btn_alerm = (Button) findViewById(R.id.btn_alerm);
        btn_timer = (Button) findViewById(R.id.btn_timer);
        btn_guardian = (Button) findViewById(R.id.btn_viewGuardian);
        //textView=findViewById(R.id.txt_msg);
        mToggleButton = findViewById(R.id.volume_toggle);
//        btn_sensor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(SimpleButtonActivity.this,SensorActivity.class);
//                startActivity(intent1);
//            }
//        });

        final SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        prefs.getBoolean("volume_controls", false);



        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                                                         SharedPreferences.Editor editor = prefs.edit();
                                                         editor.putBoolean("volume_controls", b);
                                                         editor.commit();
                                                         if (mToggleButton.isChecked()){
                                                             Intent shakeIntent = new Intent(SimpleButtonActivity.this,ShakeService.class);
                                                             startService(shakeIntent);

                                                         }
                                                         else {
                                                             Intent shakeIntent = new Intent(SimpleButtonActivity.this,ShakeService.class);
                                                             stopService(shakeIntent);


                                                         }
//                                                         if (b=true){
//                                                             Intent shakeIntent = new Intent(SimpleButtonActivity.this,ShakeService.class);
//                                                             startService(shakeIntent);
//
//                                                         }
//                                                         else {
//                                                             Intent shakeIntent = new Intent(SimpleButtonActivity.this,ShakeService.class);
//                                                             stopService(shakeIntent);
//                                                         }


                                                     }
                                                 });

        btn_guardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRunTimepermission()) {
                    Intent timerIntent = new Intent(SimpleButtonActivity.this, MainActivity.class);
                    startActivity(timerIntent);
                } else {
                    //
                }
            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRunTimepermission()) {
                    Intent timerIntent = new Intent(SimpleButtonActivity.this, TimerActivity.class);
                    startActivity(timerIntent);
                } else {
                    //
                }

            }
        });
        btn_incomingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent incomingCallIntent = new Intent(SimpleButtonActivity.this, IncomingCall.class);
                startActivity(incomingCallIntent);


            }
        });
        btn_alerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent alermIntent = new Intent(SimpleButtonActivity.this,AlermActivity.class);
//                 startActivity(alermIntent);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song);
                mediaPlayer.start();
                AlertDialog.Builder alert = new AlertDialog.Builder(SimpleButtonActivity.this);
                alert.setTitle("This Allowing You To Call To 911");
                alert.setMessage("Do Want To call to 911 Number");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediaPlayer.stop();
                        if (checkPermission(Manifest.permission.CALL_PHONE)) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:911"));
                            String dial = "tel:" + 911;
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                        } else {
                            Toast.makeText(SimpleButtonActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mediaPlayer.stop();
                            }
                        }).create();
                alert.show();

                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    //dial.setEnabled(true);

                } else {
                    //dial.setEnabled(false);
                    ActivityCompat.requestPermissions(SimpleButtonActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
                }
            }

            private boolean checkPermission(String permission) {
                

                return ContextCompat.checkSelfPermission(SimpleButtonActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
            }
        });
    }

           @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
               switch (requestCode) {
                   case MAKE_CALL_PERMISSION_REQUEST_CODE:
                       if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                           // dial.setEnabled(true);
                           Toast.makeText(getApplicationContext(), "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                       }
                       return;
               }


               if (ActivityCompat.checkSelfPermission(SimpleButtonActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   // TODO: Consider calling
                   //    ActivityCompat#requestPermissions
                   // here to request the missing permissions, and then overriding
                   //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                   //                                          int[] grantResults)
                   // to handle the case where the user grants the permission. See the documentation
                   // for ActivityCompat#requestPermissions for more details.
                   return;
               }
               // startActivity(callIntent);

           }

    public boolean checkRunTimepermission(){

        String gpsPermission= android.Manifest.permission.ACCESS_COARSE_LOCATION;
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        String sms_send_permission = Manifest.permission.SEND_SMS;
        String phone_state_permission = Manifest.permission.READ_PHONE_STATE;

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            int has_permission = checkSelfPermission(gpsPermission);
            int allow_permission=checkSelfPermission(permission);
            int send_sms = checkSelfPermission(sms_send_permission);
            int read_phone_state = checkSelfPermission(phone_state_permission);


            String[] permissions = new String[]{gpsPermission,permission, sms_send_permission,phone_state_permission};
            if (has_permission != PackageManager.PERMISSION_GRANTED
                    && allow_permission!=PackageManager.PERMISSION_GRANTED
                    && send_sms != PackageManager.PERMISSION_GRANTED
                    && read_phone_state != PackageManager.PERMISSION_GRANTED
                    ) {
                requestPermissions(permissions, 100); // Request
            } else {
                return true; //Accepted
            }
        }else {
            return true; //Not needed.
        }
        return false;
    }
    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).show();

    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        float x = sensorEvent.values[0];
//        float y = sensorEvent.values[1];
//        if (Math.abs(x) > Math.abs(y)) {
//            if (x < 0) {
//                sensorActivity.sendLocationToContacts();
//
//
//                textView.setText("move right " );
//                //Toast.makeText(getApplicationContext(),"hjkj"+url,Toast.LENGTH_SHORT).show();
//            }
//            if (x > 0) {
//                sensorActivity.sendLocationToContacts();
//
//                textView.setText("You tilt the device left");
//                //Toast.makeText(getApplicationContext(),"sms send"+url,Toast.LENGTH_SHORT).show();
//
//
//            }
//        } else {
//            if (y < 0) {
//                sensorActivity.sendLocationToContacts();
//
//                textView.setText("You tilt the device up");
//            }
//            if (y > 0) {
//
//                textView.setText("You tilt the device down");
//            }
//        }
//        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
//            sensorActivity.sendLocationToContacts();
//
//            textView.setText("Not tilt device");
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //unregister Sensor listener
//        sensorManager.unregisterListener(this);
//    }
//
//
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
}
