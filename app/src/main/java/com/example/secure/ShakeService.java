package com.example.secure;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class ShakeService extends Service /*implements SensorEventListener*/ {
    private SensorManager sensorManager;
    private long lastUpdate;
    MyDBHelper helper;
    String phoneNumber;
    GPSTracker gps;
    SQLiteDatabase db;
    public String text;
    CountDownTimer countDownTimer;
    SensorListen listen;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        listen = new SensorListen();

        sensorManager.registerListener(listen,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(listen);
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            getAccelerometer(sensorEvent);
//        }
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Device was shuffled", Toast.LENGTH_SHORT).show();
            sendLocationToContacts();
            sensorManager.unregisterListener(listen);

            new CountDownTimer(1000, 5000) {

                @Override
                public void onTick(long millisUntilFinished) {


                }

                @Override
                public void onFinish() {
                    sensorManager.registerListener(listen, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
            }.start();


            // Call send sms code here.
        }
    }

    public void sendLocationToContacts() {
        try {

            helper = new MyDBHelper(getBaseContext());
            db = helper.getWritableDatabase();
            Cursor c = db.rawQuery("select phnumber from addcontact", null);
            if (c != null) {
                c.moveToFirst();
                {
                    do {
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
*/
                        String Address = "Address Not availble";
                        if (addresses.size() > 0) {
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
                        smsManager.sendTextMessage(number, null, url, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!" + text,
                                Toast.LENGTH_LONG).show();

                        text = number;
                    } while (c.moveToNext());
                    //stopSelf();
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

    public class SensorListen implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                getAccelerometer(event);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

    }
}

