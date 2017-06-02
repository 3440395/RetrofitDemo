package com.xk.retrofitdemo.manager;


import android.content.Context;

import com.xk.retrofitdemo.Constant;
import com.xk.retrofitdemo.entry.UrlMap;
import com.xk.retrofitdemo.retrofit.Networks;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AuthManager {
//	private static final String PLAY_URL_VERSION = "2.0";

    private static final String CDN_TYPE_LETV = "3";
    private static AuthManager instance = new AuthManager();

    private AuthManager() {
    }

    public static AuthManager GetInstance() {
        return instance;
    }

    private Context context = null;
    private UrlMap mUrlMap = null;

    public void init(Context context) {
        this.context = context;
    }


    public String getPortalUrl() {
        String portal_url = Constant.GOOGLE;
        return portal_url;
    }


    /**
     * 获取动态入口map
     */
    public Observable<UrlMap> getUrlMap() {
        String portalUrl = getPortalUrl();
        return io_main(
                Networks
                        .getInstance()
                        .getApiService()
                        .getDDD(portalUrl, new HashMap<String, String>(), "DDDDDDDDDD")
        );
    }


    public <T> Observable<T> io_main(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

