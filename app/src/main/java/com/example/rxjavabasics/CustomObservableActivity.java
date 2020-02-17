package com.example.rxjavabasics;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CustomObservableActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_observable);
        compositeDisposable.add(getNotesObservable().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                map(new Function<Note, Note>() {
                    @Override
                    public Note apply(Note note) throws Exception {
                        note.setNote(note.getNote().toUpperCase());
                        return note;
                    }
                }).subscribeWith(getNotesObserver()));
    }


    private DisposableObserver<Note> getNotesObserver() {
        return new DisposableObserver<Note>() {
            @Override
            public void onNext(Note note) {
                Log.d(TAG, "onNext:" + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete():");
            }
        };
    }

    private Observable<Note> getNotesObservable() {
        final List<Note> notes = prepareNotes();
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
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
