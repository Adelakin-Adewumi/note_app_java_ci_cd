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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class WritingActivity extends AppCompatActivity {
    EditText mMessageText;
    public static final String EXTRA_ID = "com.example.achitectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.noteapp.EXTRA_TITLE";
    public static final String EXTRA_DATE = "com.example.achitectureexample.EXTRA_ID";
    public static final String EXTRA_CATEGORY = "com.example.achitectureexample.EXTRA_CATEGORY";
    public static final int BLUE = -16776961;
    public static final int DKGRAY = -12303292;
    public static final int GREEN = -16711936;
    public static final int LTGRAY = -3355444;
    public static final int RED = -65536;


    private TextView tvCategory, tvDate;
    public List<Note> mNote = new ArrayList<>();
    String value;
    Note note;
    RadioButton imgView1, imgView2, imgView3, imgView4, imgView5;
    RadioGroup group;
    private SharedPreferences mPreferences;
    public static final String sharedPrefFile =
            "com.example.android.hellosharedprefs";
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
        if (intent!=null) {
            Bundle bundle = intent.getExtras();
            if (bundle!=null) {
                value = bundle.getString(EXTRA_TITLE);
                String vvBoy = bundle.getString(EXTRA_CATEGORY);
                if (value != null) {
                    mMessageText.setText(value);
//                    tvCategory.setText(vvBoy);
                }
            }
        }


        String category = "";
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rbUncategorized:
                    Toast.makeText(this, "Uncategorized", Toast.LENGTH_SHORT).show();
                    imgView1.setHighlightColor(RED);
                    imgView1.getHighlightColor();
                    category.equals("Uncategorized");
                    break;
                //
                case R.id.rbStudy:
                    Toast.makeText(this, "Study", Toast.LENGTH_SHORT).show();
                    imgView2.setHighlightColor(DKGRAY);
                    imgView2.getHighlightColor();
                    category.equals("Study");
                    break;
                //
                case R.id.rbPersonal:
                    Toast.makeText(this, "Personal", Toast.LENGTH_SHORT).show();
                    imgView3.setHighlightColor(GREEN);
                    imgView3.getHighlightColor();
                    category.equals("Personal");
                    break;
                //
                case R.id.rbWork:
                    Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show();
                    imgView4.setHighlightColor(LTGRAY);
                    imgView4.getHighlightColor();
                    category.equals("Work");
                    break;
                //
                case R.id.rbFam:
                    Toast.makeText(this, "Family", Toast.LENGTH_SHORT).show();
                    //imgView5.setBackgroundColor(BLUE);
                    imgView5.setHighlightColor(BLUE);
                    imgView5.getHighlightColor();
                    category.equals("Family");
                    break;
                default:
                    break;
            }
        });

    }


    public void saveIt(View view) {
        Intent intent = new Intent();
        String message = mMessageText.getText().toString();
        tvCategory=findViewById(R.id.category);
        tvDate=findViewById(R.id.date);

        if (message.trim().isEmpty()) {
            Toast.makeText(this, "Empty Note Cannot be created",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra(EXTRA_TITLE, message);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor =
                mPreferences.edit();
        String message = mMessageText.getText().toString();
        preferencesEditor.putString(EXTRA_TITLE, message);
        preferencesEditor.putString(EXTRA_DATE, MainActivity.date);
        preferencesEditor.apply();
        super.onPause();
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