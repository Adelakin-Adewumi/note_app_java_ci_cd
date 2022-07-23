package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private WritingAdapter mAdapter;
    static String title;
    static String category;
    private ArrayList<Note> mNote;
    private static int EXTRA_REQUEST=2;
    static String date;
    Note note;
    boolean isDarkModeOn;
    SharedPreferences.Editor preferencesEditor;
    private AlertDialog dialog;
    private SharedPreferences mPreferences;
    public static final int Add_Note_Request = 1;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNote = new ArrayList<>();
  //      mNote.add(note);

        setAdapter();
        setItemTouchHelper(mRecyclerView);
    }

    private void setItemTouchHelper(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT
                        | ItemTouchHelper.RIGHT) {


                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                            viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState, boolean isCurrentlyActive)
                    {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        dialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme)
                                .setTitle(getString(R.string.Delete))
                                .setMessage(getString(R.string.delete_text))
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {//
                                        mNote.remove(viewHolder.getAdapterPosition());
                                //mAdapter.deleteNote(viewHolder.getAdapterPosition());
                                        mRecyclerView.getAdapter().notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this, "Note Deleted!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //mAdapter.cancelDelete(viewHolder.getAdapterPosition());
                                        finish();
                                        //mRecyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                })
                                .setIcon(ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_delete)).show();
                    }
                };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);

    }


    private void setAdapter() {
        WritingAdapter adapter = new WritingAdapter(mNote, this);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       /**Map<String, String> map = (Map<String, String>) mPreferences.getAll();
        //Set<String> keySet = map.keySet();
        mNote.addAll((Collection<? extends Note>) note);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        for (Note str : mNote) {
            if (!map.containsKey(str)) {
                note = str;
                break;
            }
        }*/
        //if (requestCode == Add_Note_Request && resultCode == RESULT_OK) {


            assert data != null;

            title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            date = dateFormat.format(calendar.getTime());
            //date = preferencesEditor.putString(WritingActivity.EXTRA_DATE)
            mPreferences.getString("", date);
        mPreferences = getSharedPreferences(WritingActivity.sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor =
                mPreferences.edit();
        title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
        preferencesEditor.putString(WritingActivity.EXTRA_TITLE, title);
        preferencesEditor.putString(WritingActivity.EXTRA_DATE, MainActivity.date);
        preferencesEditor.apply();
            note = new Note(title, category, date);
            mNote.add(note);
            mPreferences.getClass();
            mPreferences.getAll();


        //}

        //preferencesEditor.getString(WritingActivity.EXTRA_TITLE, title);
        //preferencesEditor.putString(WritingActivity.EXTRA_CATEGORY, category);
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void openTab(View view) {
        Intent intent = new Intent(this, WritingActivity.class);
        startActivityForResult(intent, Add_Note_Request);
    }

    public void openAgain(View view) {
        Intent data = new Intent(this, SavingActivity.class);
        startActivity(data);
    }


    public void changeMode(MenuItem item) {

        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            item.setIcon(R.drawable.ic_sunny);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }
    @Override
    protected void onPause() {
        Intent data= getIntent();

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        preferencesEditor
                = mPreferences.edit();
        isDarkModeOn
                = mPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        final MenuItem nightMode = menu.findItem(R.id.night_mode);
        final MenuItem dayMode = menu.findItem(R.id.day_mode);

        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            dayMode.setVisible(true);
            nightMode.setVisible(false);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            dayMode.setVisible(false);
            nightMode.setVisible(true);
        }

        nightMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

                preferencesEditor.putBoolean(
                        "isDarkModeOn", true);
                preferencesEditor.apply();
                Toast.makeText(getApplicationContext(), "Dark Mode On ", Toast.LENGTH_SHORT).show();
                dayMode.setVisible(true);
                nightMode.setVisible(false);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortBy:
                Toast.makeText(this, "Sort Clicked", Toast.LENGTH_SHORT).show();
                Collections.sort(mNote, new Comparator<Note>() {
                    DateFormat format = new SimpleDateFormat("dd/MM/yy");
                    @Override
                    public int compare(Note note, Note t1) {
                        try {
                            return Objects.requireNonNull(format.parse(note.getDate())).compareTo(
                                    format.parse(t1.getDate())
                            );
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                return true;
            case R.id.delete:
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                mNote.clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                //mAdapter.deleteAll();
                return true;//
            default:
                return onOptionsItemSelected(item);
        }
    }
}