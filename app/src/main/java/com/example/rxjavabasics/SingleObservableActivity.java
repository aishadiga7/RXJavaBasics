package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SingleObservableActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_observable);
        Single<Note> noteObservable = getNotesObservable();
        SingleObserver<Note> singleObserver = getSingleNoteObserver();
        noteObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeWith(singleObserver);
    }

    private SingleObserver<Note> getSingleNoteObserver() {
        return new SingleObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:");
                disposable = d;
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess:" +note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:" +e.getMessage());
            }
        };
    }

    private Single<Note> getNotesObservable() {
        return Single.create(new SingleOnSubscribe<Note>() {
            @Override
            public void subscribe(SingleEmitter<Note> emitter) throws Exception {
                Note note = new Note(1, "Buy Milk!");
                emitter.onSuccess(note);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
