package com.bookfeeds.goodreadsdemo.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bookfeeds.goodreadsdemo.GoodReadsService;
import com.bookfeeds.goodreadsdemo.retrofit.RetrofitHttpOAuthConsumer;
import com.bookfeeds.goodreadsdemo.retrofit.SigningOkClient;
import com.bookfeeds.goodreadsdemo.session.SessionStore;

import org.simpleframework.xml.core.Persister;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import oauth.signpost.OAuthConsumer;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

@Module
public class GoodReadsModule {

    @Provides
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }


    @Provides
    @Singleton
    RestAdapter provideRestAdapter(SessionStore sessionStore) {
        OAuthConsumer consumer = sessionStore.getSession().getConsumer();

        RetrofitHttpOAuthConsumer c = new RetrofitHttpOAuthConsumer(consumer.getConsumerKey(), consumer.getConsumerSecret());
        c.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.goodreads.com")
                .setConverter(new SimpleXMLConverter(new Persister()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new SigningOkClient(c))
                .build();

        return restAdapter;
    }

    @Provides
    @Singleton
    GoodReadsService provideGoodReadsService(RestAdapter restAdapter) {
        GoodReadsService service = restAdapter.create(GoodReadsService.class);
        return service;
    }
}