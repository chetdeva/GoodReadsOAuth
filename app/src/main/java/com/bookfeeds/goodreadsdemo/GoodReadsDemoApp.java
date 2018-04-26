package com.bookfeeds.goodreadsdemo;

import android.app.Application;

import com.bookfeeds.goodreadsdemo.dagger.AppComponent;
import com.bookfeeds.goodreadsdemo.dagger.DaggerAppComponent;

import timber.log.Timber;

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 24/04/18
 */
public class GoodReadsDemoApp extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        configureLogging();
        component().inject(this);
    }

    private static void configureLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent component() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                    .application(this)
                    .build();
        }
        return component;
    }
}
