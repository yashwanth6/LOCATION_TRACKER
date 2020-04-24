package com.example.secure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HelpActivity extends Activity {
    TextView helpactivity, instructions, text, textView;
    Button start;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean frstStart= prefs.getBoolean("frstStart",false);
        if (frstStart){
            Intent intent = new Intent(HelpActivity.this,SimpleButtonActivity.class);
            startActivity(intent);
            finish();
        }

        helpactivity = (TextView) findViewById(R.id.text);
        /*instructions=(TextView)findViewById(R.id.textView);*/
        text = (TextView) findViewById(R.id.textView3);
        textView = (TextView) findViewById(R.id.textView4);
        start = (Button) findViewById(R.id.button3);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, SimpleButtonActivity.class));
                SharedPreferences pref = getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("frstStart",true);
                editor.apply();
            }
        });
    }


}
