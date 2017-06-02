package com.xk.retrofitdemo;

import android.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment {
    protected final CompositeDisposable disposables = new CompositeDisposable();


    @Override
    public void onPause() {
        super.onPause();
        disposables.clear();
    }
}
