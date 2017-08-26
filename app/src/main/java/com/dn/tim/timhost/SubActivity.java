package com.dn.tim.timhost;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        TextView textView = new TextView(this);
//        textView.setText("SubActivity");
//        setContentView(textView);
    }
}
