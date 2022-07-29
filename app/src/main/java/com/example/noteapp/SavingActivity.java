package com.example.noteapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class SavingActivity extends AppCompatActivity {
    public static final int Edit_Note_Request = 2;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reading");

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };

        TextView cateTxtView = findViewById(R.id.categoryView);
        TextView dateTxtView = findViewById(R.id.dateView);
        TextView infoTxtView = findViewById(R.id.infoView);

        cateTxtView.setText(MainActivity.category);
        infoTxtView.setText(MainActivity.title);
        dateTxtView.setText(MainActivity.date);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void openWritingTab(View view) {
        Intent data = new Intent(this, WritingActivity.class);
        String info = MainActivity.title;
        String category = MainActivity.category;
        String date = MainActivity.date;

        //int id = 2;
        data.putExtra(WritingActivity.EXTRA_TITLE, info);
        data.putExtra(WritingActivity.EXTRA_CATEGORY, category);
        data.putExtra(WritingActivity.EXTRA_DATE, date);
        //data.putExtra(WritingActivity.EXTRA_ID, id);
        startActivityForResult(data, Edit_Note_Request);
    }
}