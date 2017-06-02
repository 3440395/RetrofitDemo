//package com.xk.retrofitdemo;
//
//
//import com.xk.retrofitdemo.entry.UrlMap;
//import com.xk.retrofitdemo.retrofit.Networks;
//import com.xk.retrofitdemo.retrofit.RxSchedulerHelper;
//
//import java.util.HashMap;
//
//import io.reactivex.annotations.NonNull;
//import io.reactivex.observers.DisposableObserver;
//
//public class MyClass {
//    HashMap<String, String> stringStringHashMap = new HashMap<>();
//
//    public void getDDDD() {
//        Networks.getInstance()
//                .getApiService()
//                .getDDD(Constant.authentry
//                        , stringStringHashMap, "dongtairikou")
//                .compose(RxSchedulerHelper.io_main())
//                .subscribe(new DisposableObserver<UrlMap>() {
//                    @Override
//                    public void onNext(@NonNull UrlMap urlMap) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//
//}
