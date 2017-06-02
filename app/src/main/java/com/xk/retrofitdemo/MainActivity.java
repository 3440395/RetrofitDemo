package com.xk.retrofitdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xk.retrofitdemo.entry.UrlMap;
import com.xk.retrofitdemo.manager.AuthManager;
import com.xk.retrofitdemo.retrofit.ProgressObserver;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.list)
    ListView listView;
    private UrlMap urlMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @OnItemClick(R.id.list)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = urlMap.getKeys()[position];
        String url = urlMap.getUrl((String) o);
        Log.e("TAG", "onItemClick" + url);

    }

    @OnClick(R.id.click)
    public void click(View v) {
        AuthManager.GetInstance().getUrlMap()
                .subscribeWith(showUrlMap());
    }

    @NonNull
    private ProgressObserver<UrlMap> showUrlMap() {
        return new ProgressObserver<UrlMap>(disposables, MainActivity.this) {
            @Override
            public void onSuccess(UrlMap urlMap) {
                Object[] keys = urlMap.getKeys();
                String[] strings = new String[keys.length];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = (String) keys[i];
                }
                MainActivity.this.urlMap = urlMap;
                listView.setAdapter(new ArrayAdapter<>(MainActivity.this
                        , android.R.layout.simple_list_item_1, strings));
            }
        };
    }

}
