package com.example.noteapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class splashscreen extends AppCompatActivity {
    public static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(SPLASH_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                startActivity(new Intent(this, MainActivity.class));
            }
        });
        thread.start();

    }


}