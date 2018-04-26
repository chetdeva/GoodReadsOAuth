package com.bookfeeds.goodreadsdemo.retrofit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SigningOkClient extends OkHttpClient {
    private final RetrofitHttpOAuthConsumer oauthConsumer;

    public SigningOkClient(RetrofitHttpOAuthConsumer oauthConsumer) {
        this.oauthConsumer = oauthConsumer;
    }

    @Override
    public Call newCall(Request request) {
        try {
            RetrofitHttpRequest signedRetrofitHttpRequest = (RetrofitHttpRequest) oauthConsumer.sign(request);
            request = (Request) signedRetrofitHttpRequest.unwrap();
        } catch (Exception e) {
//            throw new IOException(e);
            e.printStackTrace();
        }

        return super.newCall(request);
    }
}