package com.lutech.videodownloader.scenes.welcomeback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lutech.videodownloader.databinding.ActivityWelcomeBackBinding

class WelcomeBackActivity : AppCompatActivity() {

    private lateinit var mWelcomeBackBinding : ActivityWelcomeBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWelcomeBackBinding = ActivityWelcomeBackBinding.inflate(layoutInflater)
        setContentView(mWelcomeBackBinding.root)
        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mWelcomeBackBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}