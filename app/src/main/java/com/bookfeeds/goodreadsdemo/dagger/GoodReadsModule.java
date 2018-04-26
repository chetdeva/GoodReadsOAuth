package com.bookfeeds.goodreadsdemo.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bookfeeds.goodreadsdemo.GoodReadsService;
import com.bookfeeds.goodreadsdemo.retrofit.RetrofitHttpOAuthConsumer;
import com.bookfeeds.goodreadsdemo.retrofit.SigningOkClient;
import com.bookfeeds.goodreadsdemo.session.SessionStore;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import oauth.signpost.OAuthConsumer;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

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
    public HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    Retrofit provideRestAdapter(SessionStore sessionStore) {
        OAuthConsumer consumer = sessionStore.getSession().getConsumer();

        RetrofitHttpOAuthConsumer c = new RetrofitHttpOAuthConsumer(consumer.getConsumerKey(), consumer.getConsumerSecret());
        c.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());
        return new Retrofit.Builder()
                .baseUrl("https://www.goodreads.com")
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(
                        new Persister(new AnnotationStrategy() // important part!
                        )
                ))
                .client(new SigningOkClient(c))
                .build();
    }

    @Provides
    @Singleton
    GoodReadsService provideGoodReadsService(Retrofit restAdapter) {
        return restAdapter.create(GoodReadsService.class);
    }
}