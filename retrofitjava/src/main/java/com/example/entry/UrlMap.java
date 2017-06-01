package com.example.entry;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuekai on 2017/6/1.
 */

public class UrlMap {
    private boolean isTransition = false;
    private String Version;
    private String spid;
    private String epgid;
    private String Time;
    private String Count;
    private List<UrlList> UrlList;
    private Map<String, String> map;


    public String getUrl(String key) {
        String result=null;
        if (!isTransition) {
            transition();
        }
        if (map != null) {
            result = map.get(key);
        }
        return result;
    }


    private void transition() {
        map = new HashMap<>();
        for (UrlMap.UrlList urlList : UrlList) {
            map.put(urlList.Key, urlList.text);
        }
        isTransition = true;
    }

    private class UrlList {
        private String Key;
        private String Name;
        @SerializedName("#text")
        private String text;

        @Override
        public String toString() {
            return "UrlList{" +
                    "Key='" + Key + '\'' +
                    ", Name='" + Name + '\'' +
                    ", #text='" + text + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UrlMap{" +
                "Version='" + Version + '\'' +
                ", spid='" + spid + '\'' +
                ", epgid='" + epgid + '\'' +
                ", Time='" + Time + '\'' +
                ", Count='" + Count + '\'' +
                ", UrlList=" + UrlList +
                '}';
    }
}
