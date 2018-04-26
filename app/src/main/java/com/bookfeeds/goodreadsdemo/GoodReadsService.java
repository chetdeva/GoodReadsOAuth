package com.bookfeeds.goodreadsdemo;

import com.bookfeeds.goodreadsdemo.model.RecentUpdatesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodReadsService {

    @GET("/updates/friends.xml")
    Call<RecentUpdatesResponse> recentUpdates();

//    @GET("/shelf/list.xml")
//    void getShelves(@Query("key") String key, Call<Object> cb);
//
//    @GET("/review/list?format=xml&v=2")
//    void getShelf(@Query("key") String key, Call<Object> cb);
}
