package com.xk.retrofitdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

public class BaseActivity extends AppCompatActivity {
    protected final CompositeDisposable disposables = new CompositeDisposable();
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        pd = new ProgressDialog(this){
            @Override
            public void onBackPressed() {
                BaseActivity.this.onBackPressed();
            }
        };
        pd.setCancelable(false);
        pd.dismiss();
    }

    public void showProgressDialog() {
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    public void dismissProgressDialog() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
        dismissProgressDialog();

    }
}
