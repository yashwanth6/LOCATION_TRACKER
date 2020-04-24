package com.example.secure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class VolumeButtonReceiver extends BroadcastReceiver {
    //        int clicks_count = 0;
//        GPSTracker gpsTracker;
//      //  Switch mySwitch;
//
//
//
//        @Override
    public void onReceive(final Context context, final Intent intent) {
//            gpsTracker = new GPSTracker(context);
//            //
//             final SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
//
//            if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
//                int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
//                int clicks_count = prefs.getInt("clicks_count", 0);
//                clicks_count += 1;
//
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putInt("clicks_count", clicks_count);
//                editor.commit();
//
//
//                if(clicks_count >= 2){
//                    gpsTracker.getLatitude();
//                    gpsTracker.getLongitude();
//                    String url = "http://maps.google.com/?q=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
//                    Log.e("", "Url : "+url);
//                    sendLocationToContacts(url, context);
//                    editor.putInt("clicks_count", 0);
//                }
//            }
//
//            Toast.makeText(context, "count :"+clicks_count, Toast.LENGTH_SHORT).show();
//        }
//
//
//        public void sendLocationToContacts(String url, Context mContext){
//            MyDBHelper helper = new MyDBHelper(mContext);
//            SQLiteDatabase db = helper.getReadableDatabase();
//            Cursor c = db.rawQuery("select phnumber from addcontact", null);
//            if (c != null) {
//                if(c.moveToFirst()){
//                    do {
//                        //while (c.moveToNext()) {
//                        String number = c.getString(c.getColumnIndex("phnumber"));
//                        String geoAddress = url;
//                        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
//                        smsManager.sendTextMessage(number, null, geoAddress, null, null);
//                        // Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
//
//                    } while (c.moveToNext());
//                }
//
//            }
//        }
//


    }
}
