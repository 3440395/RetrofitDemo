package com.xk.retrofitdemo.retrofit;


import com.xk.retrofitdemo.api.ApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by yiyi on 2016/12/27.
 */

public class Networks {
    private String TAG = "Networks";

    private int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private ApiService apiService;
    private static Networks networks;
    private OkHttpClient okHttpClient;


    private Networks() {
        Interceptor logging = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                System.out.println(request.url().queryParameter("LogTag")+"----->"+request.url().toString());
                return chain.proceed(request);
            }
        };

        okHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://ip.taobao.com/service/")
                .build();
        apiService = retrofit.create(ApiService.class);

    }


    public static Networks getInstance() {
        if (networks == null) {
            networks = new Networks();
        }
        return networks;
    }

    public ApiService getApiService() {
        return apiService;
    }



}
