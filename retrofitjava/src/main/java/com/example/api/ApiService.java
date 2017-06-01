package com.example.api;


import com.example.entry.UrlMap;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {
//

    @GET
    Observable<UrlMap> getDDD(@Url String url, @QueryMap Map<String,String> params, @Query("LogTag") String key);

//    Cal<List<Movie>> getMovies(@Url String url, @QueryMap Map<String,String> params,@Query("android") String key);
//
//    Call<Movie> getMovie(@Url String url, @QueryMap Map<String,String> params,@Query("android") String key);




}
