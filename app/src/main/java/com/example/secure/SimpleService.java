package com.example.secure;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.secure.GPSTracker;
import com.example.secure.MyDBHelper;

import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class SimpleService extends Service {
    SQLiteDatabase db;
    MyDBHelper helper;
    public String text;
    public String phoneNumber;
    GPSTracker gps;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, Intent intent) {

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
                                if ((phoneNumber).equals(number)) {
                                    gps = new GPSTracker(getBaseContext());

                                    Toast.makeText(getApplicationContext(), "you have a missed call from:"
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
                                    text = "Location: " + lat + "," + lon
                                            + "\n" +
                                            getMyCurrentLocation(lat, lon) + url;

                                    Log.d("TAGTest1", text);

                                    SmsManager smsManager = SmsManager.getDefault();

                                    smsManager.sendTextMessage(number, null, text, null, null);

                                    smsManager.sendTextMessage(number, null, Address, null, null);

                                    Toast.makeText(getApplicationContext(), "SMS Sent!" + text,
                                            Toast.LENGTH_LONG).show();

                                    text = number;
                                }

                            } while (c.moveToNext());


                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Exception : " + e.getMessage());
                    e.printStackTrace();
                    e.printStackTrace();
                } finally {
                    phoneNumber = "";
                }


            }


        },new IntentFilter("android.intent.action.PHONE_STATE"));

        super.onCreate();
    }


    @Override
    public void onStart(Intent intent, int startId) {

    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


    private String getMyCurrentLocation(double LATITUDE, double LONGITUDE) {
        String myAddress = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder("");

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                myAddress = stringBuilder.toString();
                Log.w("location address", "" + stringBuilder.toString());
            } else {
                Log.w("location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Cannot get Address!");
        }
        return myAddress;
    }

}
