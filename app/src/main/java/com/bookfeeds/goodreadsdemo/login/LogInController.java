package com.bookfeeds.goodreadsdemo.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bookfeeds.goodreadsdemo.Constants;
import com.bookfeeds.goodreadsdemo.event.OAuthVerifierFetchedEvent;
import com.bookfeeds.goodreadsdemo.session.Session;
import com.bookfeeds.goodreadsdemo.session.SessionStore;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import timber.log.Timber;

import static com.bookfeeds.goodreadsdemo.Constants.ACCESS_URL;
import static com.bookfeeds.goodreadsdemo.Constants.AUTHORIZE_URL;
import static com.bookfeeds.goodreadsdemo.Constants.REQUEST_URL;
import static com.bookfeeds.goodreadsdemo.SecretConstants.CONSUMER_KEY;
import static com.bookfeeds.goodreadsdemo.SecretConstants.CONSUMER_SECRET;

public class LogInController {
    private final OAuthProvider provider = new DefaultOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
    private final OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

    private final Context context;
    private final SessionStore sessionStore;

    private LogInDisplay logInDisplay;

    @Inject
    public LogInController(Context context, SessionStore sessionStore) {
        this.context = context;
        this.sessionStore = sessionStore;
    }

    public void onStart(LogInDisplay logInDisplay) {
        this.logInDisplay = logInDisplay;
    }

    public void onStop() {
        this.logInDisplay = null;
    }

    public void onLogInClicked() {
        Timber.d("onLoginClicked called");

        getRequestToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String url) {
                        Timber.d("onNext called with %s", url);

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Timber.e(throwable, "onError called");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onCompleted called");
                    }
                });
    }

    private Observable<String> getRequestToken() {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return provider.retrieveRequestToken(consumer, Constants.CALLBACK_URL);
            }
        });
    }

    public void onOAuthVerifierFetched(OAuthVerifierFetchedEvent event) {
        Timber.d("onOAuthVerifierFetched called.");
        final String oauthVerifier = event.getOauthVerifier();

        getSession(oauthVerifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Session>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Session session) {
                        Timber.d("onNext called with %s", session);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Session> getSession(final String oauthVerifier) {
        return Observable.fromCallable(new Callable<Session>() {
            @Override
            public Session call() throws Exception {
                Timber.d("Retrieving access token for OAuth Verifier: %s", oauthVerifier);
                provider.retrieveAccessToken(consumer, oauthVerifier);

                String token = consumer.getToken();
                String tokenSecret = consumer.getTokenSecret();

                Timber.d("OAuth token: %s, OAuth token secret: %s", token, tokenSecret);

                consumer.setTokenWithSecret(token, tokenSecret);
                Session session = new Session(consumer);
                sessionStore.setSession(session);
                Timber.d("Session saved.");

                return session;
            }
        });
    }

    public void dummyMethod() {
        logInDisplay.inProgress();
    }
}
