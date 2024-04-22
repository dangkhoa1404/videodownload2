package com.lutech.videodownloader.scenes.setting

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.lutech.videodownloader.R
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.databinding.ActivitySettingsBinding
import com.lutech.videodownloader.databinding.DialogDeleteItemBinding
import com.lutech.videodownloader.databinding.DialogRateUsBinding
import com.lutech.videodownloader.scenes.feedback.activity.FeedbackActivity
import com.lutech.videodownloader.scenes.language.activity.LanguageActivity
import com.lutech.videodownloader.scenes.policy.PolicyActivity
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.language

class SettingsActivity : BaseActivity() {

    private lateinit var mSettingsBinding: ActivitySettingsBinding

    private var mDialogRate: Dialog? = null

    private lateinit var mDialogRateBinding : DialogRateUsBinding

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

    private fun initData() {}

    private fun View.viewSettingsOnClickListener(onClickListener: () -> Unit) {
        setOnClickListener {
            Toast.makeText(
                this@SettingsActivity, getString(R.string.txt_coming_soon), Toast.LENGTH_SHORT).show()
//            onClickListener()
        }
    }

    private fun handleEvent() {
        mSettingsBinding.apply {
            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            clLanguage.setOnClickListener {
                startActivity(Intent(this@SettingsActivity, LanguageActivity::class.java))
            }

            clWatchLater.viewSettingsOnClickListener {}

            clIncognitoMode.viewSettingsOnClickListener {}

            clHistory.viewSettingsOnClickListener {}

            clPrivateVault.viewSettingsOnClickListener {}

            clBookmark.viewSettingsOnClickListener {}

            clStorageLocation.viewSettingsOnClickListener {}

            clSearchEngine.viewSettingsOnClickListener {}

            clPrivacyPolicy.setOnClickListener {
                startActivity(Intent(this@SettingsActivity, PolicyActivity::class.java))
            }

            clFeedback.setOnClickListener {
                startActivity(Intent(this@SettingsActivity, FeedbackActivity::class.java))
            }

            clRate.setOnClickListener {
                if(mDialogRate != null) {
                    mDialogRate!!.show()
                } else {
                    mDialogRateBinding = DialogRateUsBinding.inflate(layoutInflater)

                    mSettingsBinding.tvDefaultLanguge.text = language.getLanguageName(this@SettingsActivity)

                    mDialogRate = Dialog(this@SettingsActivity)

                    mDialogRate!!.apply {
                        setContentView(mDialogRateBinding.root)
                        setCanceledOnTouchOutside(false)
                        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    }

                    mDialogRateBinding.apply {
                        ivClose.setOnClickListener {
                            mDialogRate!!.show()
                        }

                        rateus.setOnClickListener {
                            when (rateus.rating) {
                                4F, 5F -> {
                                    Utils.goToCHPlay(this@SettingsActivity)
                                }
                                else -> {
                                    startActivity(Intent(this@SettingsActivity, FeedbackActivity::class.java))
                                }
                            }
                        }
                    }

                    mDialogRate!!.show()
                }
            }

            clShare.setOnClickListener {

            }
        }
    }
}