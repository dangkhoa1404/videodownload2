package com.lutech.videodownloader.scenes.language.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.lutech.videodownloader.R
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.databinding.ActivityLanguageBinding
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.language.adapter.LanguageAdapter
import com.lutech.videodownloader.scenes.language.model.Country
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.language
import com.lutech.videodownloader.utils.permissionManager
import com.lutech.videodownloader.utils.sharedPreference
import java.util.*

class LanguageActivity : BaseActivity() {

    private var mIntent: Intent? = null

    private lateinit var mItemLanguageAdapter: LanguageAdapter

    private var mLanguages: ArrayList<Country> = arrayListOf()

    var mPosCheck = 0

    private lateinit var mLanguageBinding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLanguageBinding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(mLanguageBinding.root)
        initData()
        initView()
        handleEvents()
    }

    private fun initView() {}

    private fun initData() {
        mLanguages.apply {
            add(Country(R.drawable.ic_flag_vietnam, getString(R.string.vietnamese), "vi"))
            add(Country(R.drawable.ic_flag_english, getString(R.string.english), "en"))
            add(Country(R.drawable.ic_flag_spain, getString(R.string.spain), "es"))
            add(Country(R.drawable.ic_flag_france, getString(R.string.french), "fr"))
            add(Country(R.drawable.ic_flag_italy, getString(R.string.italy),  "it"))
            add(Country(R.drawable.ic_flag_germany, getString(R.string.german), "de"))
            add(Country(R.drawable.ic_flag_japan, getString(R.string.japan), "ja"))
            add(Country(R.drawable.ic_flag_india, getString(R.string.india), "hi"))
            add(Country(R.drawable.ic_flag_portugal, getString(R.string.portugal), "pt"))
        }

        val defaultCountry = mLanguages.find {
            Resources.getSystem().configuration.locale.toString().contains(it.locale)
        }
        if (defaultCountry != null) {
            if (defaultCountry.locale != "en") {
                mLanguages.remove(defaultCountry)
                mLanguages.add(2, defaultCountry)
            }
        }

        mPosCheck = mLanguages.indexOfFirst { it.locale == language.getIOSCountryData() }

        mItemLanguageAdapter = LanguageAdapter(this, mLanguages, object : LanguageAdapter.OnItemLanguageListener {
            override fun onItemLanguageClick(position: Int) {
                if (position != mPosCheck){
                    val mOldPosAudioSelected = mPosCheck

                    mPosCheck = position

                    mLanguageBinding.rvLanguage.adapter!!.apply {
                        notifyItemChanged(mOldPosAudioSelected)
                        notifyItemChanged(mPosCheck)
                    }
                }
            }
        })
        mLanguageBinding.rvLanguage.adapter = mItemLanguageAdapter
    }

    private fun handleEvents() {
        mLanguageBinding.btnTickLanguage.setOnClickListener {
            applyLanguage()
        }
    }

    private fun applyLanguage() {
        language.apply {
            setCurrentFlag(mLanguages[mPosCheck].icon)
            isSetLanguage()
            setIOSCountryData(mLanguages[mPosCheck].locale)
            flagByCountry = mLanguages[mPosCheck].icon
        }

//        if (intent.getBooleanExtra(Constants.IS_FROM_MAIN_ACTIVITY, false)) {
//            mIntent = null
//            mIntent = Intent(this, HomeActivity::class.java)
//        } else {
//            mIntent = null
//            mIntent = if (sharedPreference.isShowIntro) {
//                Intent(this, IntroActivity::class.java)
//            } else if (permissionManager.isAllPermissionGranted().not()) {
//                Intent(this, PermissionActivity::class.java)
//            } else {
//                Intent(this, HomeActivity::class.java)
//            }
//        }
//        sharedPreference.isShowLanguage = false
//        startActivity(mIntent!!.apply {
//            putExtra(Constants.FROM_SETTING, true)
//        })
        finish()
    }
}