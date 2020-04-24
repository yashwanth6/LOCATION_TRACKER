package com.example.secure;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisteringNumber extends Activity {

    SQLiteDatabase db;
    MyDBHelper helper;
    EditText e;
    String countrycodedata, phoneno, phoneNo, name, numwithcode;
    AutoCompleteTextView actv;
    String[] aray, aray1;
    String phnum, phno, ph;
    Cursor cursor;
    String[] countrycode = {"Afghanistan (+93)", "Albania (+355)",
            "Algeria (+213)", "American Samoa (+1)", "Andorra (+376)",
            "Angola (+244)", "Anguilla (+1)", "Antigua and Barbuda (+1)",
            "Argentina (+54)", "Armenia (+374)", "Aruba (+297)",
            "Australia (+61)", "Austria (+43)", "Azerbaijan (+994)",
            "Bahamas (+1)", "Bahrain (+973)", "Bangladesh (+880)",
            "Barbados (+1)", "Belarus (+375)", "Belgium (+32)",
            "Belize (+501)", "Benin (+229)", "Bermuda (+1)", "Bhutan (+975)",
            "Bolivia (+591)", "Bosnia and HerZegovina (+387)",
            "Botswana (+267)", "Brazil (+55)", "British Virgin Islands (+1)",
            "Brunei Darussalam (+673)", "Bulgaria (+359)",
            "Burkina Faso (+226)", "Burundi (+257)", "Cambodia (+855)",
            "Cameroon (+237)", "Canada (+1)", "Cape Verde (+238)",
            "Cayman Islands (+1)", "Central African Republic (+236)",
            "Chad (+235)", "Chile (+56)", "China (+86)", "Colombia (+57)",
            "Comoros (+269)", "Cook Islands (+682)", "Costa Rica (+506)",
            "Cote d'lvoire (+225)", "Croatia (+385)", "Cuba (+53)",
            "Cyprus (+357)", "Czech Republic (+420)",
            "Democratic Republic of the Congo (+243)", "Denmark (+45)",
            "Djibouti (+253)", "Dominica (+1)", "Dominican Republic (+1)",
            "East Timor (+670)", "Ecuador (+593)", "Egypt (+20)",
            "El Salvador (+503)", "Equatorial Guinea (+240)", "Eritrea (+291)",
            "Estonia (+372)", "Ethiopia (+251)", "Falkland Islands (+500)",
            "Faroe Islands (+298)", "Federated States of Micrones (+691)",
            "Fiji (+679)", "Finland (+358)", "France (+33)",
            "French Guiana (+594)", "French Polynesia (+689)", "FYROM (+389)",
            "Gabon (+241)", "Gambia (+220)", "Georgia (+995)", "Germany (+49)",
            "Ghana (+233)", "Gibraltar (+350)", "Greece (+30)",
            "Greenland (+299)", "Grenada (+1)", "Guadeloupe (+590)",
            "Guadeloupe & Martinique (+596)", "Guam (+1)", "Guatemala (+502)",
            "Guinea-Bissau (+245)", "Guyana (+592)", "Haiti (+509)",
            "Honduras (+504)", "Hong Kong (+852)", "Hungary (+36)",
            "Iceland (+354)", "India (+91)", "Indonesia (+62)", "Iran (+98)",
            "Iraq (+964)", "Ireland (+353)", "Israel (+972)", "Italy (+39)",
            "Jamaica (+1)", "Japan (+81)", "Jordan (+962)", "Kazakhstan (+7)",
            "Kenya (+254)", "Kiribati (+686)", "Korea (+82)", "Kuwait (+965)",
            "Kyrgyzstan (+996)", "Laos (+856)", "Latvia (+371)",
            "Lebanon (+961)", "Lesotho (+266)", "Liberia (+231)",
            "Libya (+218)", "Liechtenstein (+423)", "Lithuania (+370)",
            "Luxembourg (+352)", "Macau (+853)", "Madagascar (+261)",
            "Malawi (+265)", "Marshall Islands (+692)", "Mexico (+52)",
            "Mongolia (+976)", "Montenegro (+382)", "Mozambique (+258)",
            "Myanmar (+95)", "Namibia (+264)", "Nauru (+674)", "Nepal (+977)",
            "Netherlands Antilles (+599)", "Netherlands (+31)",
            "New Caledonia (+687)", "New Zealand (+64)", "Nicaragua (+505)",
            "Niger (+227)", "Nigeria (+234)", "Nothern Mariana Islands (+1)",
            "Norway (+47)", "Oman (+968)", "Pakistan (+92)", "Palau (+680)",
            "Palestine (+970)", "Panama (+507)", "Papua New Guinea (+675)",
            "Paraguay (+595)", "Peru (+51)", "Philippines (+63)",
            "Poland (+48)", "Portugal (+351)", "Puerto Rico (+1)",
            "Qatar (+974)", "Republic of the Congo (+242)",
            "Reunion Island (+262)", "Romania (+40)",
            "Russian Federation (+7)", "Rwanda (+250)",
            "Saint Kitts and Nevis (+1)", "Saint Lucia (+1)",
            "Saint Pierre and Miquelon (+508)",
            "Saint Vincent and the Grenadines (+1)", "Samoa (+685)",
            "San Marino (+378)", "Sao Tome and Principe (+239)",
            "Saudi Arabia (+966)", "senegal (+221)", "Serbia (+381)",
            "Seychelles (+248)", "Sierra Leone (+232)", "Singapore (+65)",
            "Slovakia (+421)", "Slovenia (+386),solomon Islands (+677)",
            "Somalia (+252)", "South africa (+27)", "South sudan (+211)",
            "Spain (+34)", "sri Lanka (+94)", "St.Helena (+290)",
            "Sudan (+249)", "Suriname (+597)", "Swaziland (+268)",
            "Sweden (+46)", "switzerland (+41)", "Syria (+963)",
            "Taiwan (+886)", "Tajikastan (+992)", "Tanzania (+255)",
            "Thailand (+66)", "Togo (+228)", "Tonga (+676)",
            "Trinidad and Tobago (+1)", "Tunisia (+216)", "Turkey (+90)",
            "Turkmenistan (+993)", "Turks and Caicos Islands (+1)",
            "Uganda (+256)", "Ukraine (+380)", "United arab Emirates (+971)",
            "United Kingdom (+44)", "United States of America (+1)",
            "United States Virgin Islands (+1)", "Uruguay (+598)",
            "Uzbekistan (+998),Vanuatu(+678)", "Vatican city State (+379)",
            "Venezuela (+58)", "Vietnam (+84)",
            "Virgin Islands,British Virgin Islands (+1)",
            "Wallis and Futuna (+681)", "Yemen (+967)", "Zambia (+260)",
            "Zimbabwe (+263)"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.register_guardian);
        helper = new MyDBHelper(this);
        db = helper.getWritableDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, countrycode);
        e = (EditText) findViewById(R.id.editText1);
        // Getting the instance of AutoCompleteTextView
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setThreshold(1);// will start working from first character
        actv.setAdapter(adapter);// setting the adapter data into the
        // AutoCompleteTextView
        actv.setTextColor(Color.RED);

    }


    public void getcontactnumber(View v) {
        try {
            if (checkContactsPermission()) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void submit(View v) {


        countrycodedata = actv.getText().toString().trim();
        if (countrycodedata.length()==0)

            Toast.makeText(RegisteringNumber.this, "please Entry country Code", Toast.LENGTH_LONG).show();

        else {
            phno = e.getText().toString().trim();


            aray = countrycodedata.split("[(]");

            aray1 = aray[1].split("[)]");
            String code = aray1[0];
            System.out.println(code);
            if (phno.length()==0) {
                Toast.makeText(RegisteringNumber.this, "check phone number field", Toast.LENGTH_LONG).show();

            }
            else {
                String numwithcode = code + phno;
                numwithcode = numwithcode.trim();
                Toast.makeText(getApplicationContext(), numwithcode, Toast.LENGTH_SHORT).show();




                ContentValues cv = new ContentValues();
                if(name  == null){
                    name = "UNKNOWN";
                }
                cv.put("name", name);
                cv.put("phnumber", numwithcode);
                db.insert("addcontact", null, cv);

                Toast.makeText(getApplicationContext(),
                        "saved successfully    " + name, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (10):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);

                    if (c.moveToFirst()) {
                        String contactId = c.getString(c
                                .getColumnIndex(ContactsContract.Contacts._ID));
                        name = c.getString(c
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = c
                                .getString(c
                                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false";

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver()
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                    + " = " + contactId, null, null);
                            if (phones.moveToNext()) {
                                phoneNo = phones
                                        .getString(phones
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                phoneNo = phoneNo.replaceAll(" ", "");
                                if (phoneNo.startsWith("+91")) {
                                    phoneNo = phoneNo.replace("+91", "");
                                    Log.e("PhoneNumber===", phoneNo);
                                    Toast.makeText(getApplicationContext(), phoneNo, Toast.LENGTH_SHORT).show();
                                    e.setText(phoneNo);
                                } else {
                                    if(phoneNo.isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Phone number not available", Toast.LENGTH_LONG).show();
                                    }

                                    e.setText(phoneNo);
                                }
                            }

                        }

                    }
                }

                break;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean checkContactsPermission(){

        String gpsPermission= Manifest.permission.READ_CONTACTS;
        String permission = Manifest.permission.WRITE_CONTACTS;

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            int has_permission = checkSelfPermission(gpsPermission);
            int allow_permission=checkSelfPermission(permission);

            String[] permissions = new String[]{gpsPermission,permission};
            if (has_permission != PackageManager.PERMISSION_GRANTED && allow_permission!=PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 100); // Request
            } else {
                return true; //Accepted
            }
        }else {
            return true; //Not needed.
        }
        return false;
    }
}



