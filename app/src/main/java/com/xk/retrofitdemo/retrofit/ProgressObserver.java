package com.xk.retrofitdemo.retrofit;

import android.util.Log;

import com.xk.retrofitdemo.BaseActivity;

import io.reactivex.observers.DisposableObserver;


/**
 * 必须要继承自DisposableObserver，方便统一管理
 */
public abstract class ProgressObserver<T> extends DisposableObserver<T> {

    private BaseActivity activity;

    public ProgressObserver(BaseActivity activity){
        this.activity = activity;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        activity.showProgressDialog();
        Log.e(TAG,"onStart");
    }

    private static final String TAG = "ProgressObserver";
    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
        activity.dismissProgressDialog();
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        // TODO: by xk 2017/6/2 11:13 调用baseactivity的方法，显示对话框(网络错误)
        activity.dismissProgressDialog();
    }




    @Override
    public void onNext(T t) {
        Log.e(TAG, "onNext: ");
        onSuccess(t);
    }


    public abstract void onSuccess(T t);

}
