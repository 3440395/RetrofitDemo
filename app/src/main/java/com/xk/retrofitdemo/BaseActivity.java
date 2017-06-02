package com.xk.retrofitdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {
    protected CompositeDisposable disposables = new CompositeDisposable();
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        init();
//        查看当前的观察者数量
//        new Thread(() -> {
//            while (true) {
//                System.out.println("1111111111111111xxxxx" + disposables.size());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private void init() {
        pd = new ProgressDialog(this) {
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
        disposables = null;
        dismissProgressDialog();

    }


    public abstract int getContentView();
}
