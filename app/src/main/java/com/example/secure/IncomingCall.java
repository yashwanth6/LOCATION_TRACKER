package com.example.secure;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class IncomingCall extends Activity {
    ImageView btn_accept,btn_reject;
    Ringtone ringtone;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_incoming_call);
        mp = MediaPlayer.create(this, R.raw.sing);
        btn_accept = (ImageView) findViewById(R.id.accept_call);
        btn_reject = (ImageView) findViewById(R.id.reject_call);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        ringtone.play();

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                Intent intent = new Intent(IncomingCall.this, Incoming2Activity.class);
                startActivity(intent);
               // finish();


            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                finish();
                //System.exit(0);
                moveTaskToBack(true);

            }
        });
    }

    public void onBackPressed() {

    }
}


