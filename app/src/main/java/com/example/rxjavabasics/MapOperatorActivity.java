package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapOperatorActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_operator);
        getUsersObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                map(new Function<Users, Users>() {
                    @Override
                    public Users apply(Users users) throws Exception {
                        users.setEmailId("hi@gmail.com");
                        return users;
                    }
                }).subscribeWith(new Observer<Users>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:");
                disposable = d;
            }

            @Override
            public void onNext(Users users) {
                Log.d(TAG, "onNext:" +users.getName() +" "+users.getEmailId() +" "+users.getGender());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All Users emitted:");
            }
        });
    }

    private Observable<Users> getUsersObservable() {
        String[] names = new String[]{"mark", "john", "trump", "obama"};
        final List<Users> users = new ArrayList<>();
        for (String name: names) {//mocking server result
            Users user = new Users();
            user.setName(name);
            user.setGender("Male");
            users.add(user);
        }
        return Observable.create(new ObservableOnSubscribe<Users>() {
            @Override
            public void subscribe(ObservableEmitter<Users> emitter) throws Exception {
                for (Users users1: users) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(users1);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
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
