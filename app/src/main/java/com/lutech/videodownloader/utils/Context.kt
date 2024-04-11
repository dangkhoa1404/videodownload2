package com.lutech.videodownloader.utils

import android.content.Context
import android.view.View
import com.lutech.videodownloader.scenes.language.preference.Language

val Context.permissionManager: PermissionManager
    get() = PermissionManager(this)

val Context.sharedPreference: SharedPreferences
    get() = SharedPreferences(this)

val Context.language: Language
    get() = Language(this)

fun visible(view: View) {
    view.visibility = View.VISIBLE
}

fun invisible(view: View) {
    view.visibility = View.INVISIBLE
}

fun gone(view: View) {
    view.visibility = View.GONE
}