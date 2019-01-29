package com.elgigs.firebaselogintwo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class loginsuccessful extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccessful);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void  run() {
                Intent i = new Intent(loginsuccessful.this, dashboard.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
