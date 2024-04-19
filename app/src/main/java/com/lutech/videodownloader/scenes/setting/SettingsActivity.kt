package com.lutech.videodownloader.scenes.setting

import android.os.Bundle
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private lateinit var mSettingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mSettingsBinding.root)
        initView()
        initData()
        handleEvent()
    }

    private fun initView() {

    }

    private fun initData() {

    }

    private fun handleEvent() {
        mSettingsBinding.apply {
            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}