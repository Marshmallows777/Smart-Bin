package com.jss.smartdustbin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.jss.smartdustbin.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /*String dustbinId = getIntent().getStringExtra("id");
        Intent notificationActivity = new Intent(SplashActivity.this, DustbinDetailsActivity.class);
        notificationActivity.putExtra("id", dustbinId);
        startActivity(notificationActivity);*/
        /*if(dustbinId != null){
            Intent notificationActivity = new Intent(SplashActivity.this, DustbinDetailsActivity.class);
            notificationActivity.putExtra("id", dustbinId);
            startActivity(notificationActivity);
        }*/
       /* if (getIntent().getStringExtra("bin") != null) {
            String dustbinId = getIntent().getStringExtra("bin");
            Intent notificationActivity = new Intent(SplashActivity.this, DustbinDetailsActivity.class);
            notificationActivity.putExtra("bin", dustbinId);
            startActivity(notificationActivity);
        }*/

        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            Intent mainIntent;
            String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", null);
            if (accessToken == null)
                mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
            else
                mainIntent = new Intent(SplashActivity.this, UserHomeActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
