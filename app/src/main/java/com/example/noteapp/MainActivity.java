package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private RecyclerView mRecyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> mNote;
    private NoteViewModel noteViewModel;
    static String time;
    boolean isDarkModeOn;
    SharedPreferences.Editor preferencesEditor;
    private AlertDialog dialog;
    private SharedPreferences mPreferences;
    public static final int Add_Note_Function = 1;
    public static final int Edit_Note_Function = 2;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;


    @NonNull
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new
                NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified")
                .setContentText("This is your notification text")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    private void createNotificationChannel() {
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        //adapter = new NoteListAdapter(new NoteListAdapter.WordDiff());
        setTitle("NoteX");
        adapter = new NoteAdapter();
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        System.out.println("Hey");

        //Get the time for the alert
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String ntime = timeFormat.format(calendar.getTime());
        time = ntime.replace("am", "AM").replace("pm", "PM");
        if (time.equals("10.30 PM")) {
            sendNotification();
        }
        createNotificationChannel();


        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNote = new ArrayList<>();
        noteViewModel = new ViewModelProvider(this)
                .get(NoteViewModel.class);
        noteViewModel.getAllWords().observe(this, notes -> {
            adapter.submitList(notes);
            mNote = new ArrayList<>(notes);
        });
        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent data = new Intent(MainActivity.this, WritingActivity.class);
                data.putExtra(WritingActivity.EXTRA_NOTE, note);

                startActivityForResult(data, Edit_Note_Function);
            }
        });

        setItemTouchHelper(mRecyclerView);
    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
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
                        dialog = new AlertDialog.Builder(MainActivity.this, androidx.appcompat.R.style.ThemeOverlay_AppCompat_ActionBar)
                                .setTitle(getString(R.string.Delete))
                                .setMessage(getString(R.string.delete_text))
                                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
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
                                })
                                .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                                    adapter.notifyDataSetChanged();
                                    dialog.cancel();

                                })
                                .setIcon(ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_delete)).show();
                    }
                };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Add_Note_Function && resultCode == RESULT_OK) {
            assert data != null;
            Note note1 = (Note) data.getSerializableExtra(WritingActivity.EXTRA_NOTE);

            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();

            noteViewModel.insert(note1);
        }
        else if (requestCode == Edit_Note_Function && resultCode == RESULT_OK) {

            assert data != null;
            int id = data.getIntExtra(WritingActivity.EXTRA_ID, -2);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            Note note1 = (Note) data.getSerializableExtra(WritingActivity.EXTRA_NOTE);
            note1.setId(note1.getId());
            noteViewModel.insert(note1);
            Toast.makeText(this, "Note Updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openTab(View view) {
        Intent intent = new Intent(this, WritingActivity.class);
        startActivityForResult(intent, Add_Note_Function);
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

        nightMode.setOnMenuItemClickListener(item -> {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);

            preferencesEditor.putBoolean(
                    "isDarkModeOn", true);
            preferencesEditor.apply();
            Toast.makeText(getApplicationContext(), "Night Mode", Toast.LENGTH_SHORT).show();
            dayMode.setVisible(true);
            nightMode.setVisible(false);
            return true;
        });

        dayMode.setOnMenuItemClickListener(item -> {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            preferencesEditor.putBoolean(
                    "isDarkModeOn", false);
            preferencesEditor.apply();
            Toast.makeText(getApplicationContext(), "Day Mode", Toast.LENGTH_SHORT).show();
            dayMode.setVisible(false);
            nightMode.setVisible(true);
            return true;

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
                dialog = new AlertDialog.Builder(MainActivity.this, androidx.appcompat.R.style.ThemeOverlay_AppCompat_ActionBar)
                        .setTitle(getString(R.string.Delete))
                        .setMessage(getString(R.string.delete_text))
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            noteViewModel.deleteAll();
                            Toast.makeText(this, "All Deleted!", Toast.LENGTH_SHORT).show();
                            mNote.clear();
                            dialog.cancel();
                        })
                        .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                            adapter.notifyDataSetChanged();
                            dialog.cancel();

                        })
                        .setIcon(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_delete)).show();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                //mAdapter.deleteAll();
                return true;//
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
    }
}