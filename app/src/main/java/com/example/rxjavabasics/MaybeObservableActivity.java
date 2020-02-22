package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MaybeObservableActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maybe_observable);
        Maybe<Note> maybeObservable = getNotesObservable();
        MaybeObserver<Note> maybeObserver = getNoteObserver();
        maybeObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeWith(maybeObserver);
    }

    private MaybeObserver<Note> getNoteObserver() {
        return new MaybeObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe:");
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess:" +note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:" +e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete():");
            }
        };
    }

    private Maybe<Note> getNotesObservable() {
        return Maybe.create(new MaybeOnSubscribe<Note>() {
            @Override
            public void subscribe(MaybeEmitter<Note> emitter) throws Exception {
                Note note = new Note(1, "Buy bread");
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(note);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }


}
