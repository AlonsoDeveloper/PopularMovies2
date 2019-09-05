package com.alonso.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Context mContext = SplashActivity.this;
                Class nextAcitivy = MainActivity.class;
                Intent mIntent = new Intent(mContext, nextAcitivy);
                startActivity(mIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
