package com.kids.funtv

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.google.android.gms.ads.MobileAds
import com.kids.funtv.service.remote.YoutubeAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)

        MobileAds.initialize(this,"ca-app-pub-5669751081498672~1629715633")

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

    }
}