package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;

public class CustomObservableActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_observable);
    }


    private Observable<Note> getNotesObservable() {
        final List<Note> notes = prepareNotes();

        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {

            }
        });
    }

    private List<Note> prepareNotes() {
        List<Note> notesList = new ArrayList<>();
        notesList.add(new Note(1, "Buy icecream"));
        notesList.add(new Note(2, "Binge watch"));
        notesList.add(new Note(3, "Go to school"));
        notesList.add(new Note(4, "GO to neighbour"));
        notesList.add(new Note(5, "Buy paste"));
        return notesList;
    }
}
