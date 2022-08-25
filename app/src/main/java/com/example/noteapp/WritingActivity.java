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
    public static final String EXTRA_NOTE = "com.example.noteapp.EXTRA_NOTE";
    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        mMessageText = findViewById(R.id.edtText);

        //Gets intent from the MainActivity
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

    public void saveIt() {
        Intent intent = new Intent();
        String message = mMessageText.getText().toString();
        if (message.trim().isEmpty()) {
            Toast.makeText(this, "Empty Note cannot be created",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //Gets date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String date = dateFormat.format(calendar.getTime());

        //Gets time
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String ntime = timeFormat.format(calendar.getTime());
        String time = ntime.replace("am", "AM").replace("pm", "PM");
        if (note == null) {
            note = new Note();
        }
        note.setInfo(message);
        note.setCategory("");
        note.setDate(date);
        note.setTime(time);
        intent.putExtra(EXTRA_NOTE, note);

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
                saveIt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}