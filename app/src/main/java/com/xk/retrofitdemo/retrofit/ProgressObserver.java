package com.xk.retrofitdemo.retrofit;

import com.xk.retrofitdemo.BaseActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


/**
 * 必须要继承自DisposableObserver，方便统一管理
 */
public abstract class ProgressObserver<T> extends DisposableObserver<T> {
    private static final String TAG = "ProgressObserver";

    private BaseActivity activity;
    private CompositeDisposable disposables;

    public ProgressObserver(CompositeDisposable disposables, BaseActivity activity) {
        this.disposables = disposables;
        this.activity = activity;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        disposables.add(this);
        activity.showProgressDialog();
    }

    @Override
    public void onComplete() {
        activity.dismissProgressDialog();
        disposables.remove(this);
        disposables = null;

    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        // TODO: by xk 2017/6/2 11:13 调用baseactivity的方法，显示对话框(网络错误)
        activity.dismissProgressDialog();
    }


    @Override
    public void onNext(T t) {
        onSuccess(t);
    }


    public abstract void onSuccess(T t);

}
