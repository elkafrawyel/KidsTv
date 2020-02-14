package com.cartoons.kids.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cartoons.kids.MyApp
import com.cartoons.kids.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
fun ImageView.loadWithPlaceHolder(drawable: Int) {
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


private var FACEBOOK_URL = "https://www.facebook.com/سليني-100570851534713/"
private var FACEBOOK_PAGE_ID = "سليني-100570851534713"
private fun Context.getFacebookPageURL(): String {
    val packageManager = packageManager
    try {
        val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
        return if (versionCode >= 3002850) { //newer versions of fb app
            "fb://facewebmodal/f?href=$FACEBOOK_URL"
        } else { //older versions of fb app
            "fb://page/$FACEBOOK_PAGE_ID"
        }
    } catch (e: PackageManager.NameNotFoundException) {
        return FACEBOOK_URL //normal web url
    }

}

fun Context.openFaceBookPage() {
    val facebookIntent = Intent(Intent.ACTION_VIEW)
    val facebookUrl = getFacebookPageURL()
    facebookIntent.data = Uri.parse(facebookUrl)
    startActivity(facebookIntent)
}

fun Context.shareApp() {
    val share = Intent(Intent.ACTION_SEND)
    share.type = "text/plain"
    // Add data to the intent, the receiving app will decide
    // what to do with it.
    share.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
    val message =
        "تطبيق تعليمي مفيد للاطفال يحتوي على العديد من البرامج والفيديوهات التعليمية والمسلية\n" +
                "تساعد على تنمية ذكاء ومهارة طفلك وتعليمه العديد من الاشياء المفيدة\n"
    share.putExtra(Intent.EXTRA_TEXT, message + APP_LINK_IN_STORE)
    startActivity(Intent.createChooser(share, "مشاركة التطبيق"))
}

const val APP_LINK_IN_STORE = "https://play.google.com/store/apps/details?id=com.cartoons.kids"

fun Context.openAppOnStore() {
    val appPackageName = "com.cartoons.kids"
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
    } catch (ex: android.content.ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun Context.showMessageInDialog(
    message: String,
    okAction: () -> Unit,
    cancelAction: () -> Unit
) {
    MaterialAlertDialogBuilder(this).setTitle(getString(R.string.app_name))
        .setMessage(message)
        .setPositiveButton(getString(R.string.Ok)) { dialog, which ->
            okAction.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            cancelAction.invoke()
            dialog.dismiss()
        }.show()

}