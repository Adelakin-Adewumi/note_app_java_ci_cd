package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WritingActivity extends AppCompatActivity {
    private EditText mMessageText;
    public static final String EXTRA_ID = "com.example.achitectureexample.EXTRA_ID";

    public static final String EXTRA_NOTE = "com.example.noteapp.EXTRA_NOTE";
    public static final int BLUE = -16776961;
    public static final int DKGRAY = -12303292;
    public static final int GREEN = -16711936;
    public static final int LTGRAY = -3355444;
    public static final int RED = -65536;
    private Note note;

    RadioButton imgView1, imgView2, imgView3, imgView4, imgView5;
    RadioGroup group;

    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        mMessageText = findViewById(R.id.edtText);
        imgView1 = findViewById(R.id.rbUncategorized);
        imgView2 = findViewById(R.id.rbStudy);
        imgView3 = findViewById(R.id.rbPersonal);
        imgView4 = findViewById(R.id.rbWork);
        imgView5 = findViewById(R.id.rbFam);
        group = findViewById(R.id.radioImg);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE)) {
            setTitle("Edit Note");
            note =(Note) intent.getSerializableExtra(EXTRA_NOTE);
            String mMessage = note.getInfo();
            mMessageText.setText(mMessage);
        } else {
            setTitle("Writing");
        }
    }

    public void saveIt(View view) {
        Intent intent = new Intent();
        String message = mMessageText.getText().toString();
        if (message.trim().isEmpty()) {
            Toast.makeText(this, "Empty Note cannot be created",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");


        String date = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String ntime = timeFormat.format(calendar.getTime());
        String time = ntime.replace("am", "AM").replace("pm", "PM");
        note = new Note();
        note.setInfo(message);
        note.setCategory("Category");
        note.setDate(date);
        note.setTime(time);
        intent.putExtra(EXTRA_NOTE, note);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            Toast.makeText(this, "Note It", Toast.LENGTH_SHORT).show();
            intent.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveIt(view);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}