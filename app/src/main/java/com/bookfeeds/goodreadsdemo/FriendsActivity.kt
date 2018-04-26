package com.bookfeeds.goodreadsdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bookfeeds.goodreadsdemo.dagger.AppComponent
import com.bookfeeds.goodreadsdemo.model.RecentUpdatesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        goodReadsService.recentUpdates().enqueue(object : Callback<RecentUpdatesResponse> {
            override fun onFailure(call: Call<RecentUpdatesResponse>?, t: Throwable?) {
                Timber.d("onFailure ${t?.message}")
            }

            override fun onResponse(call: Call<RecentUpdatesResponse>?, response: Response<RecentUpdatesResponse>?) {
                Timber.d("onResponse ${response?.body()}")
            }
        })
    }
}
