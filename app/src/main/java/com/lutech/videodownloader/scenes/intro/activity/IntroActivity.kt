package com.lutech.videodownloader.scenes.intro.activity

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ActivityIntroBinding
import com.lutech.videodownloader.model.Intro
import com.lutech.videodownloader.scenes.intro.adapter.IntroSliderAdapter
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.invisible
import com.lutech.videodownloader.utils.visible

class IntroActivity : BaseActivity() {

    private lateinit var mIntroBinding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIntroBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(mIntroBinding.root)
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {}

    private fun initView() {
        mIntroBinding.vp2Intro.adapter = IntroSliderAdapter(
            this,
            listOf(
                Intro(R.drawable.slide1, R.string.txt_intro_title_1, R.string.txt_intro_des_1),
                Intro(R.drawable.slide2, R.string.txt_intro_title_2, R.string.txt_intro_des_2),
                Intro(R.drawable.slide3, R.string.txt_intro_title_3, R.string.txt_intro_des_3),
                Intro(R.drawable.slide1, R.string.txt_intro_title_4, R.string.txt_intro_des_4),
                Intro(R.drawable.slide2, R.string.txt_intro_title_5, R.string.txt_intro_des_5),
                Intro(R.drawable.slide3, R.string.txt_intro_title_6, R.string.txt_intro_des_6),
                Intro(R.drawable.slide1, R.string.txt_intro_title_7, R.string.txt_intro_des_7)
            ),
            object : IntroSliderAdapter.OnItemIntroListener {
                override fun onItemIntroClick() {
                    if (intent.getBooleanExtra(Constants.IS_FROM_HOME_ACTIVITY, false)) {
                        onBackPressedDispatcher.onBackPressed()
                    } else {

                    }
                }
            }
        )

        mIntroBinding.dotIndicator.attachTo(mIntroBinding.vp2Intro)
    }

    private fun handleEvent() {
        var currentPosVp = 0
        mIntroBinding.vp2Intro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    5 -> {
                        currentPosVp = position
                        if(mIntroBinding.llContinue.isVisible) {
                            mIntroBinding.llContinue.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.fade_out))
                            invisible(mIntroBinding.llContinue)
                        }
                    }

                    6 -> {
                        if(currentPosVp == 5 && !mIntroBinding.llContinue.isVisible) {
                            mIntroBinding.llContinue.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.fade_in))
                            visible(mIntroBinding.llContinue)
                        }
                    }
                }
            }
        })

        mIntroBinding.llContinue.setOnClickListener {
            if (intent.getBooleanExtra(Constants.IS_FROM_HOME_ACTIVITY, false)) {
                onBackPressedDispatcher.onBackPressed()
            } else {

            }
        }
    }
}