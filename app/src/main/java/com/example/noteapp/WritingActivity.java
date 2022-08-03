package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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



    static String value;
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
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        /*getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        );*/
        SharedPreferences.Editor preferencesEditor =
                mPreferences.edit();
        Intent intent = getIntent();
        if (intent!=null) {
            Bundle bundle = intent.getExtras();
            if (bundle!=null) {
                String value = bundle.getString(EXTRA_TITLE);
                //String vvBoy = bundle.getString(EXTRA_CATEGORY);
                if (value != null) {
                    mMessageText.setText(value);
                    setTitle("Edit Note");
                   // mPreferences.getString(EXTRA_TITLE, value);
                    //preferencesEditor.putString(EXTRA_TITLE, value);
                    //preferencesEditor.apply();
//                    tvCategory.setText(vvBoy);
                }
            }
        }

        setTitle("Writing");


    }

    public void saveIt(View view) {
        Intent intent = new Intent();
        String message = mMessageText.getText().toString();
        TextView tvCategory = findViewById(R.id.category);
        TextView tvDate = findViewById(R.id.date);
        TextView tvTime = findViewById(R.id.time);
        if (message.trim().isEmpty()) {
            Toast.makeText(this, "Empty Note  cannot be created",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //String category = "";
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rbUncategorized:
                    Toast.makeText(this, "Uncategorized", Toast.LENGTH_SHORT).show();
                    imgView1.setHighlightColor(RED);
                    imgView1.getHighlightColor();
                    String category = "Uncategorized!";
                    intent.putExtra(EXTRA_CATEGORY, category);
                    break;
                //
                case R.id.rbStudy:
                    Toast.makeText(this, "Study", Toast.LENGTH_SHORT).show();
                    imgView2.setHighlightColor(DKGRAY);
                    imgView2.getHighlightColor();
                    category = "Study";
                    intent.putExtra(EXTRA_CATEGORY, category);
                    break;
                //
                case R.id.rbPersonal:
                    Toast.makeText(this, "Personal", Toast.LENGTH_SHORT).show();
                    imgView3.setHighlightColor(GREEN);
                    imgView3.getHighlightColor();
                    category = "Personal";
                    intent.putExtra(EXTRA_CATEGORY, category);
                    break;
                //
                case R.id.rbWork:
                    Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show();
                    imgView4.setHighlightColor(LTGRAY);
                    imgView4.getHighlightColor();
                    category = "Work";
                    //category.equals("Work");
                    intent.putExtra(EXTRA_CATEGORY, category);
                    break;
                //
                case R.id.rbFam:
                    Toast.makeText(this, "Family", Toast.LENGTH_SHORT).show();
                    //imgView5.setBackgroundColor(BLUE);
                    imgView5.setHighlightColor(BLUE);
                    category = "Family";
                    intent.putExtra(EXTRA_CATEGORY, category);
                    break;
                default:
                    break;
            }
        });
        //intent.putExtra(EXTRA_CATEGORY, category);

        intent.putExtra(EXTRA_TITLE, message);

        int id = getIntent().getIntExtra(value, -1);
        if (id != -1) {
            intent.putExtra(value, id);
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