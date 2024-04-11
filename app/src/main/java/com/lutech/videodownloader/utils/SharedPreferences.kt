package com.lutech.videodownloader.utils

import android.content.Context
import androidx.preference.PreferenceManager

import androidx.core.content.edit

class SharedPreferences(context: Context) {

    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    var isUpdateYoutubeDL
        get() = sharedPreferences.getBoolean(Constants.YOUTUBE_DL, false)
        set(value) = sharedPreferences.edit {
            putBoolean(Constants.YOUTUBE_DL, value)
        }

    var viewType
        get() = sharedPreferences.getInt(Constants.VIEW_TYPE, 1)
        set(value) = sharedPreferences.edit {
            putInt(Constants.VIEW_TYPE, value)
        }

}