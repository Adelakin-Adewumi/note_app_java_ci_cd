package com.example.noteapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository mRepository;

    private final LiveData<List<Note>> mAllWords;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllWords = mRepository.getAllNotes();
    }

    LiveData<List<Note>> getAllWords() {
        return mAllWords;
    }

    public void insert(Note note) {
        mRepository.insert(note);
    }

    public void update(Note note) {
        mRepository.update(note);
    }

    public void delete(Note note) {
        mRepository.delete(note);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }


}
