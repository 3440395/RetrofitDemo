package com.example;

import com.example.entry.UrlMap;
import com.example.retrofit.Networks;

import java.util.HashMap;

import rx.Subscriber;

public class MyClass {
    HashMap<String, String> stringStringHashMap = new HashMap<>();

    public void getDDDD() {
         Networks.getInstance()
                .getApiService()
                .getDDD(Constant.authentry
                        , stringStringHashMap, "dongtairikou")
                .subscribe(new Subscriber<UrlMap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UrlMap urlMap) {

                    }
                });

    }


}
