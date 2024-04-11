package com.lutech.videodownloader.scenes.language.preference

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import com.lutech.videodownloader.R
import com.lutech.videodownloader.utils.Constants
import java.util.Locale

class Language(context: Context) {

    private val sharedPreferencesLanguage =
        context.getSharedPreferences(Constants.APP_NAME_NEW, Activity.MODE_PRIVATE)
    
    fun isGetLanguage(): Boolean {
        return sharedPreferencesLanguage.getBoolean(Constants.IS_SET_LANG, false)
    }

    fun isSetLanguage() {
        val editor = sharedPreferencesLanguage.edit()
        editor.putBoolean(Constants.IS_SET_LANG, true)
        editor.apply()
    }

    fun getIOSCountryData(): String {
        return sharedPreferencesLanguage.getString(Constants.KEY_LANG, "en").toString()
    }

    fun setIOSCountryData(lang: String) {
        val editor = sharedPreferencesLanguage.edit()
        editor.putString(Constants.KEY_LANG, lang)
        editor.apply()
    }

    fun getCurrentFlag(): Int {
        return sharedPreferencesLanguage.getInt(Constants.KEY_FLAG, 0)
    }

    fun setCurrentFlag(flag: Int) {
        val editor = sharedPreferencesLanguage.edit()
        editor.putInt(Constants.KEY_FLAG, flag)
        editor.apply()
    }

    fun setLanguageForApp() {
        val languageToLoad = getIOSCountryData()
        Log.d("998888888888", languageToLoad)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(Locale.forLanguageTag(languageToLoad)))
    }

    var flagByCountry
        get() = sharedPreferencesLanguage.getInt(Constants.FLAG_BY_COUNTRY, R.drawable.ic_flag_english)
        set(flagByCountry) = sharedPreferencesLanguage.edit {
            putInt(Constants.FLAG_BY_COUNTRY, flagByCountry)
        }
}