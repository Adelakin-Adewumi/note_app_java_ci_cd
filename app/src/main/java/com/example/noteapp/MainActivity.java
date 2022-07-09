package com.example.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private WritingAdapter mAdapter;
    private ArrayList<Note> mNote;
    private static int EXTRA_REQUEST=2;
    Note note;
    SharedPreferences mPreference;
    public static final int Add_Note_Request = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNote = new ArrayList<>();
        Intent data = getIntent();

        setAdapter();
        userInfo();
        //
    }

    private void setAdapter() {
        WritingAdapter adapter = new WritingAdapter(mNote, this);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    private void userInfo() {
        /**Intent data= getIntent();
        String title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
        String category = data.getStringExtra(WritingActivity.EXTRA_CATEGORY);
        String date = data.getStringExtra(WritingActivity.EXTRA_DATE);
        note = new Note(title, category, date);
        mNote.add(note);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
/**        Map<String, String> map = (Map<String, String>) mPreference.getAll();
        Set<String> keySet = map.keySet();
        mNote.addAll((Collection<? extends Note>) note);
        SharedPreferences.Editor preferencesEditor = mPreference.edit();
        for (Note str : mNote) {
            if (!map.containsKey(str)) {
                note = str;
                break;
            }
        }*/
        if (requestCode == Add_Note_Request && resultCode == RESULT_OK) {
            String title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
            String category = data.getStringExtra(WritingActivity.EXTRA_CATEGORY);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String date = dateFormat.format(calendar.getTime());
            note = new Note(title, category, date);
            mNote.add(note);
        }
        /*preferencesEditor.putString(WritingActivity.EXTRA_TITLE, note.getInfo());
        preferencesEditor.putString(WritingActivity.EXTRA_CATEGORY, note.getCategory());
        preferencesEditor.putString(WritingActivity.EXTRA_DATE, note.getDate());*/
        //preferencesEditor.apply();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openTab(View view) {
        Intent intent = new Intent(this, WritingActivity.class);
        mRecyclerView.getAdapter().notifyItemInserted(mNote.size());
        startActivity(intent);
    }
}