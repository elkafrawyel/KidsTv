package com.kids.funtv

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.Utils
import com.google.android.gms.ads.MobileAds
import com.kids.funtv.common.changeLanguage
import com.kids.funtv.data.storage.PreferencesHelper
import com.kids.funtv.service.remote.YoutubeAPI
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val CHANNEL_ID= "com.kids.funtv"

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
//        changeLanguage()
        Utils.init(this)

        MobileAds.initialize(this,getString(R.string.appId))

        createNotificationChannel()

    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "KidsTv"
            val descriptionText = "KidsTv App"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        lateinit var instance: MyApp
            private set

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .cache(Cache(MyApp.instance.cacheDir, 60 * 60 * 24 * 28))
                .addInterceptor(getLoggingInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .build()
        }

        fun createApiService(): YoutubeAPI {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .client(getOkHttpClient())
                .build()

            return retrofit.create(YoutubeAPI::class.java)
        }


        fun getPreferenceHelper() = PreferencesHelper(instance)
    }
}