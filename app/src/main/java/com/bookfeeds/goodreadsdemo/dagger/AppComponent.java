package com.bookfeeds.goodreadsdemo.dagger;

import android.app.Application;

import com.bookfeeds.goodreadsdemo.FriendsActivity;
import com.bookfeeds.goodreadsdemo.GoodReadsDemoApp;
import com.bookfeeds.goodreadsdemo.MainActivity;
import com.bookfeeds.goodreadsdemo.session.SessionStore;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 24/04/18
 */

@Singleton
@Component(modules = GoodReadsModule.class)
public interface AppComponent {

    void inject(GoodReadsDemoApp goodReadsDemoApp);

    void inject(MainActivity mainActivity);

    void inject(FriendsActivity friendsActivity);

    SessionStore sessionStore();

    @Component.Builder
    interface Builder {

        AppComponent build();

        @BindsInstance
        Builder application(Application application);
    }
}
