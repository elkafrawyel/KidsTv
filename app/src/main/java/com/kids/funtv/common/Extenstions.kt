package com.kids.funtv.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kids.funtv.MyApp
import com.kids.funtv.R
import java.util.*

fun Context.changeLanguage() {
    var locale: Locale? = null
    when (MyApp.getPreferenceHelper().language) {
        Constants.Language.ARABIC.value -> {
            locale = Locale("ar")
        }
        Constants.Language.ENGLISH.value -> {
            locale = Locale("en")
        }
    }
    Locale.setDefault(locale!!)
    val config = this.resources.configuration
    config.setLocale(locale)
    this.createConfigurationContext(config)
    this.resources.updateConfiguration(config, this.resources.displayMetrics)
}

@SuppressLint("CheckResult")
fun ImageView.loadWithPlaceHolder(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(MyApp.instance)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val requestOptions = RequestOptions()
    requestOptions.placeholder(circularProgressDrawable)
    requestOptions.error(MyApp.instance.getDrawable(R.drawable.error_image))

    Glide.with(MyApp.instance)
        .load(url)
        .apply(requestOptions)
        .into(this)

}

@SuppressLint("CheckResult")
fun ImageView.loadWithPlaceHolder(drawable:Int) {
    val circularProgressDrawable = CircularProgressDrawable(MyApp.instance)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val requestOptions = RequestOptions()
    requestOptions.placeholder(circularProgressDrawable)
    requestOptions.error(MyApp.instance.getDrawable(R.drawable.error_image))

    Glide.with(MyApp.instance)
        .load(drawable)
        .apply(requestOptions)
        .into(this)

}

@SuppressLint("CheckResult")
fun ImageView.loadWithPlaceHolder(uri: Uri) {
    val circularProgressDrawable = CircularProgressDrawable(MyApp.instance)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val requestOptions = RequestOptions()
    requestOptions.placeholder(circularProgressDrawable)
    requestOptions.error(MyApp.instance.getDrawable(R.drawable.error_image))

    Glide.with(MyApp.instance)
        .load(uri)
        .apply(requestOptions)
        .into(this)

}