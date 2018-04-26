package com.bookfeeds.goodreadsdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bookfeeds.goodreadsdemo.Constants.CALLBACK_SCHEME
import com.bookfeeds.goodreadsdemo.dagger.AppComponent
import com.bookfeeds.goodreadsdemo.event.OAuthVerifierFetchedEvent
import com.bookfeeds.goodreadsdemo.login.LogInController
import oauth.signpost.OAuth
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginController: LogInController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appComponent.inject(this)
        Timber.d("onCreate called")
    }

    fun onLoginClick(view: View) {
        loginController.onLogInClicked()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent called")

        val uri = intent.data
        if (uri != null && uri.scheme == CALLBACK_SCHEME) {

            val oauthVerifier = uri.getQueryParameter(OAuth.OAUTH_TOKEN)

            Timber.d("Extracted OAuth verifier: %s", oauthVerifier)

            loginController.onOAuthVerifierFetched(OAuthVerifierFetchedEvent(oauthVerifier))
        }
    }

    fun onFriendsClick(view: View) {
        startActivity(Intent(this, FriendsActivity::class.java))
    }

    private val appComponent: AppComponent
        get() = (application as GoodReadsDemoApp).component()

}
