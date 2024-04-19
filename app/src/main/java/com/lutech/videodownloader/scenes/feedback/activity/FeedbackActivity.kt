package com.lutech.videodownloader.scenes.feedback.activity

import android.os.Bundle
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.databinding.ActivityFeedbackBinding

class FeedbackActivity : BaseActivity() {

    private lateinit var mFeedbackBinding : ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFeedbackBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(mFeedbackBinding.root)
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {

    }

    private fun initView() {

    }

    private fun handleEvent() {

    }
}