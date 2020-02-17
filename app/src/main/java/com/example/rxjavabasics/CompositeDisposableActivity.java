package com.example.rxjavabasics;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CompositeDisposableActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite_disposable);
        Observable<String> animalObservable = createObservable();
        DisposableObserver<String> animalObserver = createAnimalObserver();
        DisposableObserver<String> capsAnimalObserver = createCapsAnimalObserver();
        //add all subscriptions to composite disposable
        compositeDisposable.add(animalObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                }).subscribeWith(animalObserver));

        compositeDisposable.add(animalObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("c");
                    }
                }).map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribeWith(capsAnimalObserver));

    }


    private DisposableObserver<String> createAnimalObserver() {
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: All items are emitted");
            }
        };
    }

    private DisposableObserver<String> createCapsAnimalObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: All items are emitted");
            }
        };
    }

    private Observable<String> createObservable() {
        return Observable.just("Cat", "Dog", "Camel", "Lion", "Tiger", "Bat", "Beer", "Bee", "Cobra");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
