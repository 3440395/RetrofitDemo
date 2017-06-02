package com.xk.retrofitdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xk.retrofitdemo.entry.UrlMap;
import com.xk.retrofitdemo.manager.AuthManager;
import com.xk.retrofitdemo.retrofit.ProgressObserver;

public class MainActivity extends BaseActivity {

    private TextView tv;
    private TextView tv1;
    private ListView listView;
    private UrlMap urlMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.click);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = urlMap.getKeys()[position];
                String url = urlMap.getUrl((String) o);
                Log.e("TAG", "onItemClick" + url);
            }
        });
    }

    public void click(View v) {
        disposables.add(
                AuthManager.GetInstance().getUrlMap()
                        .subscribeWith(showUrlMap())
        );


    }

    @NonNull
    private ProgressObserver<UrlMap> showUrlMap() {
        return new ProgressObserver<UrlMap>(MainActivity.this) {
            @Override
            public void onSuccess(UrlMap urlMap) {
                Object[] keys = urlMap.getKeys();
                String[] strings = new String[keys.length];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = (String) keys[i];
                }
                MainActivity.this.urlMap = urlMap;
                listView.setAdapter(new ArrayAdapter(MainActivity.this
                        , android.R.layout.simple_list_item_1, strings));
            }
        };
    }

//    public void click1(View v) {
//        Networks.getInstance().getApiService()
//                .getDDD(Constant.authentry1, new HashMap<String, String>(), "ddddd")
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<UrlMap>() {
//                    @Override
//                    public void onNext(@NonNull UrlMap urlMap) {
//                        tv1.setText(urlMap.getUrl("voole_recommenended"));
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        e.printStackTrace();
//                        tv1.setText(e.getMessage());
//                        Log.e("click1", e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e("click1", "onComplete");
//                    }
//
//                });
//
//    }
}
