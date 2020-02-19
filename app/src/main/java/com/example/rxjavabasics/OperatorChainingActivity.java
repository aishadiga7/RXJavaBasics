package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class OperatorChainingActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_chaining);
        Observable.range(1, 20).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer +" is an even number";
            }
        }).subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe:");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext:" +s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete:");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
