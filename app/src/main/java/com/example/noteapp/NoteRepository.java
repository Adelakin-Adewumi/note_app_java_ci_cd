package com.example.noteapp;

import android.app.Application;
import android.os.AsyncTask;

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

    public void update(Note note) {
        new UpdateNodeAsyncTask(noteDoa).execute(note);
    }

    void delete(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() ->{
            noteDoa.delete(note);
        });
    }

    void deleteAll(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() ->{
            noteDoa.deleteAllNotes();
        });
    }

    private static class UpdateNodeAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDoa noteDoa;

        private UpdateNodeAsyncTask(NoteDoa noteDoa) {
            this.noteDoa = noteDoa;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDoa.update(notes[0]);
            return null;
        }


    }

    /*public void delete(Note note) {
        new DeleteNodeAsyncTask(noteDoa).execute(note);
    }*/

    private static class DeleteNodeAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDoa noteDoa;

        private DeleteNodeAsyncTask(NoteDoa noteDoa) {
            this.noteDoa = noteDoa;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDoa.delete(notes[0]);
            return null;
        }
    }

}
