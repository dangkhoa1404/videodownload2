package com.lutech.videodownloader.scenes.playaudio

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ActivityPlayAudioBinding
import com.lutech.videodownloader.databinding.ActivitySplashBinding

class PlayAudioActivity : AppCompatActivity() {

    private lateinit var mPlayAudioBinding : ActivityPlayAudioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        } else {
            window?.statusBarColor = Color.TRANSPARENT
        }

        mPlayAudioBinding = ActivityPlayAudioBinding.inflate(layoutInflater)
        setContentView(mPlayAudioBinding.root)

        initData()
        initView()
        handleEvent()
    }

    private fun initData() {

    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mPlayAudioBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun handleEvent() {

    }
}