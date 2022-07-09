package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WritingActivity extends AppCompatActivity {
    EditText mMessageText;
    public static final String EXTRA_ID = "com.example.achitectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.noteapp.EXTRA_TITLE";
    public static final String EXTRA_DATE = "com.example.achitectureexample.EXTRA_ID";
    public static final String EXTRA_CATEGORY = "com.example.achitectureexample.EXTRA_TITLE";

     static String date= "Date";
    static String category="Category";
    private TextView tvCategory, tvDate;
    public List<Note> mNote = new ArrayList<>();
    Note note;
    ImageView imgView1, imgView2, imgView3, imgView4, imgView5, seniorImg;
    private SharedPreferences mPreferences;
    public static final String sharedPrefFile =
            "com.example.android.hellosharedprefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        mMessageText = findViewById(R.id.edtText);
        imgView1 = findViewById(R.id.category1);
        imgView2 = findViewById(R.id.category2);
        imgView3 = findViewById(R.id.category3);
        imgView4 = findViewById(R.id.category4);
        imgView5 = findViewById(R.id.category5);
        seniorImg = findViewById(R.id.controlImg);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            mMessageText.setText(intent.getStringExtra(EXTRA_TITLE));
            tvCategory.setText(intent.getStringExtra(EXTRA_CATEGORY));
            tvDate.setText(intent.getStringExtra(EXTRA_DATE));
            //spinnerPriority.setSelection(intent.getIntExtra(EXTRA_PRIORITY_NUMBER,1));
        } else {
            setTitle("Add Note");
//            tvDate.setText(date);
        }

    }

    public String category() {
        String category = "";
        if (imgView1.equals(seniorImg)) {
            Toast.makeText(this, "Uncategorized", Toast.LENGTH_SHORT).show();
            category="Uncategorized";
            //imgView1.setBackgroundColor(2);
        } else if (imgView2.equals(seniorImg)) {
            Toast.makeText(this, "Study", Toast.LENGTH_SHORT).show();
            category.equals("Study");
            //imgView2.setBackgroundColor(3);
        } else if (imgView3.equals(seniorImg)) {
            Toast.makeText(this, "Personal", Toast.LENGTH_SHORT).show();
            category.equals("Personal");
            //   imgView3.setBackgroundColor(4);
        } else if (imgView4.equals(seniorImg)) {
            Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show();
            category.equals("Work");
            //imgView4.setBackgroundColor(5);
        } else if (imgView5.equals(seniorImg)) {
            Toast.makeText(this, "Family Affair", Toast.LENGTH_SHORT).show();
            category.equals("Family Affair");
            //imgView5.setBackgroundColor(1);
        }
        return category;
    }

    public void saveIt(View view) {
        Intent intent = new Intent();
        String message = mMessageText.getText().toString();
        tvCategory=findViewById(R.id.category);
        tvDate=findViewById(R.id.date);
        String category = category();
//        tvDate.setText(date);
//        String time = tvDate.getText().toString();
        intent.putExtra(EXTRA_TITLE, message);
//        intent.putExtra(EXTRA_DATE, time);
        intent.putExtra(EXTRA_CATEGORY, category);
//        Map<String, ?> map = mPreferences.getAll();
        SharedPreferences.Editor preferencesEditor =
                mPreferences.edit();
        //String key = "";
        /**for (Note str : mNote) {
            if (!map.containsKey(str)) {
                note=str;
                break;
            }
        }*/
        for (Note str : mNote) {
                note=str;
                break;
        }
        //intent.putExtra(String.valueOf(list), key);
        /*preferencesEditor.putString(EXTRA_TITLE, note.getInfo());
        preferencesEditor.putString(EXTRA_CATEGORY, note.getCategory());
        preferencesEditor.putString(EXTRA_DATE, note.getDate());
        preferencesEditor.apply();*/
        setResult(RESULT_OK, intent);
        finish();
    }

    public void click(){
        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WritingActivity.this, "Uncategorized", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**public void clickIt(View view) {
        Toast.makeText(this,
                "Uncategorized", Toast.LENGTH_SHORT).show();
    }*/

    public void clickIt2(View view) {
        Toast.makeText(this,
                "Study", Toast.LENGTH_SHORT).show();
    }

    public void clickIt3(View view) {
        Toast.makeText(this,
                "Personal", Toast.LENGTH_SHORT).show();
    }

    public void clickIt4(View view) {
        Toast.makeText(this,
                "Work", Toast.LENGTH_SHORT).show();
    }

    public void clickIt5(View view) {
        Toast.makeText(this,
                "Family", Toast.LENGTH_SHORT).show();
    }

}