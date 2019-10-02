package com.kids.funtv.common

import android.content.Context
import com.kids.funtv.MyApp
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

