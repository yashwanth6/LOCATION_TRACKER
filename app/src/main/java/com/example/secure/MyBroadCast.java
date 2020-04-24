package com.example.secure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

//public class MyBroadCast extends BroadcastReceiver implements SensorEventListener {
//    private SensorManager sensorManager;
//    private long lastUpdate;
//    MyDBHelper helper;
//    String phoneNumber;
//    GPSTracker gps;
//    SQLiteDatabase db;
//    public String text;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        sensorManager = (SensorManager) context.getSystemService();
//        lastUpdate = System.currentTimeMillis();
//
//        sensorManager.registerListener(this,
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
//}
