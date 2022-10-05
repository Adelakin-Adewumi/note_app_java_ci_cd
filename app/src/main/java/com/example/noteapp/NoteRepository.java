package com.example.noteapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class NoteRepository {
    private final NoteDoa noteDoa;
    private final LiveData<List<Note>> allNotes;

    NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getDatabase(application);
        noteDoa = database.noteDoa();
        allNotes = noteDoa.getAllNotes();

    }

    LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    void insert(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() ->
                noteDoa.insert(note));
    }

    void update(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() ->
                noteDoa.update(note));
    }


    void delete(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() ->{
            noteDoa.delete(note);
        });
    }

    void deleteAll() {
        NoteDatabase.databaseWriteExecutor.execute(noteDoa::deleteAllNotes);
    }


}
