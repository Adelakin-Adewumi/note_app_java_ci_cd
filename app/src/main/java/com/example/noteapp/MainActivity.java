package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NoteListAdapter adapter;
    String title;
    String category;
    private ArrayList<Note> mNote;
    private static final int EXTRA_REQUEST=2;
    private NoteViewModel noteViewModel;
    String date;
    String time;
    Note note;
    boolean isDarkModeOn;
    SharedPreferences.Editor preferencesEditor;
    private AlertDialog dialog;
    private SharedPreferences mPreferences;
    public static final int Add_Note_Request = 1;
    public static final int NEW_WORD_ACTIVITY_CODE = 1;
    int position;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);

        adapter = new NoteListAdapter(new NoteListAdapter.WordDiff());
        //adapter = new NoteAdapter();
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNote = new ArrayList<>();
        noteViewModel = new ViewModelProvider(this)
                .get(NoteViewModel.class);
        noteViewModel.getAllWords().observe(this, notes -> {
            adapter.submitList(notes);
            mNote = new ArrayList<>(notes);
        });


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
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        dialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme)
                                .setTitle(getString(R.string.Delete))
                                .setMessage(getString(R.string.delete_text))
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        final int adapterPosition = viewHolder.getAdapterPosition();
                                        final Note mNote = adapter.getNoteAt(adapterPosition);


                                        Snackbar snackbar = Snackbar.make(mRecyclerView, "Note Deleted!"
                                        , Snackbar.LENGTH_SHORT).setAction(
                                                "Undo", view -> noteViewModel.insert(mNote)
                                        );

                                        snackbar.show();
                                        noteViewModel.delete(adapter.getNoteAt(viewHolder
                                        .getAdapterPosition()));
                                        dialog.cancel();
                                        //mRecyclerView.getAdapter().notifyDataSetChanged();

                                    }
                                })
                                .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                                    //mAdapter.cancelDelete(viewHolder.getAdapterPosition());
                                    adapter.notifyDataSetChanged();
                                    dialog.cancel();
                                    //mRecyclerView.getAdapter().notifyDataSetChanged();
                                })
                                .setIcon(ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_delete)).show();
                    }
                };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);

    }

    public void onTap(Note note) {
        Intent data = new Intent(MainActivity.this, WritingActivity.class);
        String info = note.getInfo();
        String category = note.getCategory();
        String date = note.getDate();

        //int id = 2;
        data.putExtra(WritingActivity.EXTRA_TITLE, info);
        data.putExtra(WritingActivity.EXTRA_CATEGORY, category);
        data.putExtra(WritingActivity.EXTRA_DATE, date);
        startActivityForResult(data, Add_Note_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == NEW_WORD_ACTIVITY_CODE && resultCode == RESULT_OK) {
            assert data != null;

            title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String ntime = timeFormat.format(calendar.getTime());
            time = ntime.replace("am", "AM").replace("pm", "PM");
        title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
        date = dateFormat.format(calendar.getTime());
            note = new Note(title, category, date, time);
            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
            noteViewModel.insert(note);
            mNote.add(note);
        } else if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

                title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
                date = dateFormat.format(calendar.getTime());
                category = data.getStringExtra(WritingActivity.EXTRA_CATEGORY);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String nTime = timeFormat.format(calendar.getTime());
                time = nTime.replace("am", "AM").replace("pm", "PM");
                note = new Note(title, category, date, currentTime);
                Toast.makeText(this, "Note Updated!", Toast.LENGTH_SHORT).show();
                noteViewModel.update(note);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void openTab(View view) {
        Intent intent = new Intent(this, WritingActivity.class);
        startActivityForResult(intent, NEW_WORD_ACTIVITY_CODE);
    }

    public void openAgain(View view) {
        //int position = mNote.get(position);
      Note note = mNote.get(position);
        onTap(note);
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

        dayMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                preferencesEditor.putBoolean(
                        "isDarkModeOn", false);
                preferencesEditor.apply();
                Toast.makeText(getApplicationContext(), "Dark Mode Off", Toast.LENGTH_SHORT).show();
                dayMode.setVisible(false);
                nightMode.setVisible(true);
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
                Toast.makeText(this, "All Deleted!", Toast.LENGTH_SHORT).show();
                mNote.clear();
                noteViewModel.deleteAll(note);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                //mAdapter.deleteAll();
                return true;//
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}