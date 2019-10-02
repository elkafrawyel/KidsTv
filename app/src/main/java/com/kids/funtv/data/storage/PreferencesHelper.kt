package com.kids.funtv.data.storage

import android.content.Context
import android.preference.PreferenceManager
import com.kids.funtv.common.Constants


class PreferencesHelper(context: Context) {

    companion object {
        private const val LANGUAGE = "language"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var language = preference.getString(LANGUAGE, Constants.Language.ENGLISH.value)
        set(value) = preference.edit().putString(LANGUAGE, value).apply()

    fun clear() {
        val lang = language
        preference.edit().clear().apply()
        language = lang
    }
}