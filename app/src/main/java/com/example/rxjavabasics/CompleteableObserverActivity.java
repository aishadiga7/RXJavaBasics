package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CompleteableObserverActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeable_observer);
        Note note = new Note(1, "Buy Icecream");
        Completable completableObservable = updateNote(note);
        CompletableObserver completableObserver = getCompleteObserver();
        completableObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeWith(completableObserver);
    }

    private CompletableObserver getCompleteObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:");
                disposable = d;
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete:");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:");
            }
        };
    }

    private Completable updateNote(Note note) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    Thread.sleep(1000);
                    emitter.onComplete();
                }
            }
        });
    }
}
