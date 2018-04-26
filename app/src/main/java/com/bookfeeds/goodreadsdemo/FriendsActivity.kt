package com.bookfeeds.goodreadsdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bookfeeds.goodreadsdemo.dagger.AppComponent
import com.bookfeeds.goodreadsdemo.model.RecentUpdatesResponse
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import timber.log.Timber
import javax.inject.Inject

class FriendsActivity : AppCompatActivity() {

    @Inject
    lateinit var goodReadsService: GoodReadsService

    private val appComponent: AppComponent
        get() = (application as GoodReadsDemoApp).component()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        appComponent.inject(this)
    }

    fun onFriendsClick(view: View) {
        goodReadsService.recentUpdates(object : Callback<RecentUpdatesResponse> {
            override fun success(t: RecentUpdatesResponse?, response: Response?) {
                Timber.d("getRecentUpdates success $t")
            }

            override fun failure(error: RetrofitError?) {
                Timber.e("getRecentUpdates failure $error")
            }
        })
    }
}
