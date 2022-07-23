package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashScreen extends AppCompatActivity {
    public static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler han = new Handler(Looper.getMainLooper());
        han.postDelayed(goToMainActivity(), 3000);

    }

    private Runnable goToMainActivity() {
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
        return null;
    }
}