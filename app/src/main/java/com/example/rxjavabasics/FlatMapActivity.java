package com.example.rxjavabasics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlatMapActivity extends AppCompatActivity {
    private static final String TAG = "RXJava";
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map);
        getUsersObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Users, Observable<Users>>() {

            @Override
            public Observable<Users> apply(Users user) throws Exception {

                // getting each user address by making another network call
                return getAddressObservable(user);
            }
        }).subscribeWith(new Observer<Users>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Users users) {
                Log.d(TAG, "onNext " +users.getName() +" "+users.getGender() +" "+users.getAddress());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError" +e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete" +"All items emitted");
            }
        });
    }

    private Observable<Users> getAddressObservable(final Users user) {
        final String[] addresses = new String[]{
                "1600 Amphitheatre Parkway, Mountain View, CA 94043",
                "2300 Traverwood Dr. Ann Arbor, MI 48105",
                "500 W 2nd St Suite 2900 Austin, TX 78701",
                "355 Main Street Cambridge, MA 02142"
        };
        return  Observable.create(new ObservableOnSubscribe<Users>() {
            @Override
            public void subscribe(ObservableEmitter<Users> emitter) throws Exception {
                user.setAddress("1600 Amphitheatre Parkway, Mountain View, CA 94043");
                if (!emitter.isDisposed()) {
                    emitter.onNext(user);
                }
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
}
