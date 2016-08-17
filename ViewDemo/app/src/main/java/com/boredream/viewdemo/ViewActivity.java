package com.boredream.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewActivity extends AppCompatActivity {

    private View iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        setFinishOnTouchOutside(false);

//        getWindowManager().addView();
    }
}
