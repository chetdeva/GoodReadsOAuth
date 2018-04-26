package com.bookfeeds.goodreadsdemo;

import com.bookfeeds.goodreadsdemo.model.RecentUpdatesResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GoodReadsService {

    @GET("/updates/friends.xml")
    void getRecentUpdates(Callback<RecentUpdatesResponse> cb);

    @GET("/shelf/list.xml")
    void getShelves(@Query("key") String key, Callback<Object> cb);

    @GET("/review/list?format=xml&v=2")
    void getShelf(@Query("key") String key, Callback<Object> cb);
}
