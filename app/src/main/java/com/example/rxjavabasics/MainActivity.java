package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable<String> animalObservable = createObservable();
        Observer<String> animalObserver  = createObserver();
        animalObservable
                .subscribeOn(Schedulers.io())//observables perform operations on the background thread
                .observeOn(AndroidSchedulers.mainThread())//hearing on the main thread
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribe(animalObserver);
    }

    private Observer<String> createObserver() {
       return new Observer<String>() {
           @Override
           public void onSubscribe(Disposable d) {
               Log.d(TAG, "onSubscribe");
               disposable = d;
           }

           @Override
           public void onNext(String s) {
               Log.d(TAG, "onNext: "+s);
           }

           @Override
           public void onError(Throwable e) {
               Log.d(TAG, "onError: " +e.getMessage());
           }

           @Override
           public void onComplete() {
               Log.d(TAG, "onComplete: All items are emitted");
           }
       };
    }

    private Observable<String> createObservable() {
        return Observable.just("Cat", "Dog", "Camel", "Lion", "Tiger", "Bat");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //don't send inputs once activity is destroyed
        disposable.dispose();
    }
}
