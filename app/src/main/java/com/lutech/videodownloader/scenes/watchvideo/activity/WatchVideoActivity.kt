package com.lutech.videodownloader.scenes.watchvideo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ActivityWatchVideoBinding
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import io.reactivex.disposables.CompositeDisposable

class WatchVideoActivity : AppCompatActivity() {

    private lateinit var mWatchVideoBinding : ActivityWatchVideoBinding

    private var mPathVideo : String = ""

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWatchVideoBinding = ActivityWatchVideoBinding.inflate(layoutInflater)
        setContentView(mWatchVideoBinding.root)
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {
        mPathVideo = intent.getStringExtra(Constants.PATH_VIDEO_IN_PROGRESS).toString()
        val nameVideo = intent.getStringExtra(Constants.NAME_VIDEO_IN_PROGRESS)
        if(nameVideo != null) {
            mWatchVideoBinding.tvTitle.text = nameVideo
        }
        mWatchVideoBinding.webViewWatchVideo.apply {
            loadUrl(mPathVideo)
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }

    private fun initView() {
        window.statusBarColor =
            ContextCompat.getColor(this, R.color.color_black) // set status background white
        window.navigationBarColor =
            ContextCompat.getColor(this, R.color.color_black) // set status background white
    }

    private fun handleEvent() {
        mWatchVideoBinding.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mWatchVideoBinding.imgShare.setOnClickListener {
            Utils.shareLocalVideo(mPathVideo, this)
        }

        onBackPressedDispatcher.addCallback(this) {
            if(mWatchVideoBinding.webViewWatchVideo.canGoBack()) {
                mWatchVideoBinding.webViewWatchVideo.goBack()
            } else {
                finish()
            }
        }
    }
}