package com.example.secure;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView;
    SQLiteDatabase db;
    MyDBHelper helper;
    public String text;
    GPSTracker gps;
    String url;
    String phoneNumber;
    Myservices myservices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        myservices=new Myservices();
        gps = new GPSTracker(SensorActivity.this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //locate views
        //textView = (TextView) findViewById(R.id.txt);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
                sendLocationToContacts();


                textView.setText(" " + url);
                //Toast.makeText(getApplicationContext(),"hjkj"+url,Toast.LENGTH_SHORT).show();
            }
            if (x > 0) {
                sendLocationToContacts();

                textView.setText("You tilt the device left"+url);
                //Toast.makeText(getApplicationContext(),"sms send"+url,Toast.LENGTH_SHORT).show();


            }
        } else {
            if (y < 0) {
                sendLocationToContacts();

                textView.setText("You tilt the device up");
            }
            if (y > 0) {

                textView.setText("You tilt the device down");
            }
        }
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
            sendLocationToContacts();

            textView.setText("Not tilt device");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }





    public void sendLocationToContacts() {
        try {

            helper = new MyDBHelper(getBaseContext());
            db = helper.getWritableDatabase();
            Cursor c = db.rawQuery("select phnumber from addcontact", null);
            if (c != null) {
                c.moveToFirst();
                {
                    do{
                        String number = c.getString(c.getColumnIndex("phnumber"));
                        number = number.replaceAll("-", "");
                        Log.e(TAG, "incomingNumber : " + phoneNumber);
                        Log.e(TAG, "number : " + number);
//                                                        if ((phoneNumber).equals(number)) {
                        gps = new GPSTracker(getBaseContext());

                        Toast.makeText(getApplicationContext(), "Sms Sent:"
                                        + phoneNumber
                                , Toast.LENGTH_LONG).show();


                        double lat = gps.getLatitude();
                        double lon = gps.getLongitude();

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        // StringBuffer sb = new StringBuffer();

                                                        /*sb.append(addresses.get(0).getAddressLine(0) + "," + sb.append(addresses.get(0).getLocality()) + "," +
                                                                sb.append(addresses.get(0).getAdminArea()) + "\n" +
                                                                sb.append(addresses.get(0).getCountryName()) + "," +sb.append(addresses.get(0).getPostalCode())+","+sb.append(addresses.get(0).getFeatureName()));
*/                                                      String Address = "Address Not availble";
                        if(addresses.size() > 0){
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state_name = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            Address = address + "" + city + "" + state_name + "" + country + "" + postalCode + "" + knownName;
                        }

                        String url = "http://maps.google.com/?q=" + lat + "," + lon;
                        text = "Location: " + lat + "," + lon;

                               // myservices.getMyCurrentLocation(lat, lon) + url;

                        Log.d("TAGTest1", text);

                        SmsManager smsManager = SmsManager.getDefault();

                        smsManager.sendTextMessage(number, null, Address, null, null);

                        smsManager.sendTextMessage(number, null, text, null, null);


                        Toast.makeText(getApplicationContext(), "SMS Sent!" + text,
                                Toast.LENGTH_LONG).show();

                        text = number;
//                                                        }

                    }while (c.moveToNext());




                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage());
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            //phoneNumber = "";
        }

    }
//        MyDBHelper helper = new MyDBHelper(this);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor c = db.rawQuery("select phnumber from addcontact", null);
//        if (c != null) {
//            if (c.moveToFirst()) {
//                do {
//                    //while (c.moveToNext()) {
//                    String number = c.getString(c.getColumnIndex("phnumber"));
//                    String geoAddress = url;
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(number,null,"sms sent",null,null);
//
////                    android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
////                    smsManager.sendTextMessage(number, null, geoAddress, null, null);
//                    // Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
//
//                } while (c.moveToNext());
//            }
//
//        }
//    }
}

