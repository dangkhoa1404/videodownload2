package com.lutech.videodownloader.scenes.policy

import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.databinding.ActivityPolicyBinding
import androidx.activity.addCallback

class PolicyActivity : BaseActivity() {

    private lateinit var mPolicyBinding: ActivityPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPolicyBinding = ActivityPolicyBinding.inflate(layoutInflater)
        setContentView(mPolicyBinding.root)
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {}

    private fun initView() {
        mPolicyBinding.webView.apply {
//            loadUrl("https://viblo.asia/p/tim-hieu-ve-webview-trong-android-Ljy5VPqzZra")
            loadUrl("https://www.google.com/")
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }

    private fun handleEvent() {
        mPolicyBinding.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            Log.d("===>024924", "handleOnBackPressed: ")
            if(mPolicyBinding.webView.canGoBack()) {
                mPolicyBinding.webView.goBack()
            } else {
                finish()
            }
        }
    }

}