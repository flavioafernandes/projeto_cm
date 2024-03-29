package com.example.projectcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.projectcm.activities.MainActivity;

public class SplashScreen extends AppCompatActivity {

    private static int Splash_Time_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //findViewById(R.id.intro_img).setAlpha(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()  {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        },Splash_Time_OUT);
    }
}