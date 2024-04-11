package com.lutech.videodownloader.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lutech.videodownloader.R
import com.lutech.videodownloader.utils.language

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language.setLanguageForApp()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //  set status text dark
        window.statusBarColor =
            ContextCompat.getColor(this, R.color.color_white) // set status background white
        window.navigationBarColor =
            ContextCompat.getColor(this, R.color.color_white) // set status background white
    }
}