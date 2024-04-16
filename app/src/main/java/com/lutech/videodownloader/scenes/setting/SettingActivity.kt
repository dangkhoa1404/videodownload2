package com.lutech.videodownloader.scenes.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.lutech.videodownloader.scenes.policy.PolicyActivity
import com.lutech.videodownloader.R
import com.lutech.videodownloader.scenes.intro.activity.IntroActivity
import com.lutech.videodownloader.scenes.language.activity.LanguageActivity
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.header_setting_activity.*
import kotlinx.android.synthetic.main.setting_component_browser.*
import kotlinx.android.synthetic.main.setting_component_download.*
import kotlinx.android.synthetic.main.setting_component_general.*
import kotlinx.android.synthetic.main.setting_component_help.*
import java.io.File
import java.util.*

class SettingActivity : AppCompatActivity() {
    private var directoryDir: File? = null
    private var errorUserPick = "\n\n Error:"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)
        initView()
        initData()
        handleEvent()


    }


    private fun initData() {
        val packageManager = packageManager
        val versionCode = packageManager.getPackageInfo(this.packageName, 0).versionCode

        tvAppVersion.text = tvAppVersion.text.dropLast(5).toString()+ ": " +versionCode.toString()

    }

    private fun initView() {
        directoryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        tvVideoDownloadLocation.text = directoryDir.toString() + "/AppVideoDownloader"

        Constants.isDownloadWithWIFIOnly = Utils.getIsDownloadWithWIFIOnlySharePreference(this)
        if (Constants.isDownloadWithWIFIOnly) {
            switchDownloadWithWiFiOnly.isChecked = true
        }
        Constants.isSaveGallery = Utils.getIsSaveGallerySharePreference(this)
        if (Constants.isSaveGallery) {
            switchSyncToGallery.isChecked = true
        }

        Constants.isBlockAds = Utils.getIsBlockAdsSharePreference(this)
        Log.d("initView", "----:isBlockAds-- " + Constants.isBlockAds)

        if (Constants.isBlockAds) {
            switchBlockAds.isChecked = true
        }
    }

    private fun handleEvent() {
        btnHome.setOnClickListener {
            finish()
        }
        btnHowToDownload.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

        btnPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, PolicyActivity::class.java)
            startActivity(intent)
        }

        btnShareWithFriends.setOnClickListener {
//            val bottomshareDialog = BottomSheetDialog(
//                this@SettingActivity, R.style.BottomSheetDialogTheme
//            )
//            val bottomshareView = LayoutInflater.from(applicationContext).inflate(
//                R.layout.bottom_share_layout, findViewById(R.id.bottomshareSheet) as LinearLayout?
//            )
//            bottomshareView.findViewById<View>(R.id.btn_share_now).setOnClickListener {
////                Toast.makeText(this@SettingActivity, "Share...", Toast.LENGTH_SHORT).show()
//                shareApp()
//                bottomshareDialog.dismiss()
//            }
//            bottomshareView.findViewById<View>(R.id.btn_later).setOnClickListener {
//
//                bottomshareDialog.dismiss()
//            }
//
//            bottomshareDialog.setContentView(bottomshareView)
//            bottomshareDialog.show()
        }

        btnFeedBack.setOnClickListener {
            val bottomshareDialog = BottomSheetDialog(
                this@SettingActivity, R.style.BottomSheetDialogTheme
            )
//            val bottomshareView = LayoutInflater.from(applicationContext).inflate(
//                R.layout.bottom_feedback_layout, findViewById(R.id.bottomfeedbackSheet) as LinearLayout?
//            )

//            bottomshareView.findViewById<View>(R.id.btn_submit).setOnClickListener {
//
//                feedBack()
////                Toast.makeText(this@SettingActivity, "Feedback...", Toast.LENGTH_SHORT).show()
//                bottomshareDialog.dismiss()
//            }
//
//            bottomshareView.findViewById<View>(R.id.tag1).setOnClickListener {
//                errorUserPick += getString(R.string.download_failed) + "\n"
//            }
//
//            bottomshareView.findViewById<View>(R.id.tag2).setOnClickListener {
//                errorUserPick += getString(R.string.txt_too_many_ads) + "\n"
//
//            }
//
//            bottomshareView.findViewById<View>(R.id.tag3).setOnClickListener {
//                errorUserPick += getString(R.string.txt_low_quality) + "\n"
//
//            }
//
//            bottomshareView.findViewById<View>(R.id.tag4).setOnClickListener {
//                errorUserPick += getString(R.string.txt_other) + "\n"
//
//            }
//
//
//            bottomshareDialog.setContentView(bottomshareView)
//            bottomshareDialog.show()
        }

        btnLike.setOnClickListener {

            val bottomshareDialog = BottomSheetDialog(
                this@SettingActivity, R.style.BottomSheetDialogTheme
            )
            val bottomshareView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_like_layout, findViewById(R.id.bottomlikeSheet) as LinearLayout?
            )

            bottomshareDialog.setContentView(bottomshareView)
            bottomshareDialog.show()

            bottomshareDialog.findViewById<View>(R.id.btn_rate)?.setOnClickListener {
                val packageName = "com.lutech.videodownloader"

                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                }

                bottomshareDialog.dismiss()
            }

        }


        switchDownloadWithWiFiOnly.setOnCheckedChangeListener { compoundButton, b ->
            Constants.isDownloadWithWIFIOnly = !Constants.isDownloadWithWIFIOnly
            Utils.setIsDownloadWithWIFIOnlySharePreference(this, Constants.isDownloadWithWIFIOnly)
            Log.d("handleEvent", "----:isDownloadWithWIFIOnly-- " + Constants.isDownloadWithWIFIOnly)
        }

        switchBlockAds.setOnCheckedChangeListener { _, _ ->
            Constants.isBlockAds = !Constants.isBlockAds
            Utils.setIsBlockAdsSharePreference(this, Constants.isBlockAds)
            Log.d("handleEvent", "----:isBlockAds-- " + Constants.isBlockAds)
        }
//        btnClearBrowserHistory.setOnClickListener {
//            Toasty.success(this, getString(R.string.txt_history_cleared), Toast.LENGTH_LONG).show()
//        }
//        btnClearCache.setOnClickListener {
//            Toasty.success(this, getString(R.string.txt_cache_cleared), Toast.LENGTH_LONG).show()
//        }
        btnSearchEngine.setOnClickListener {
            Toast.makeText(this, "btnSearchEngine", Toast.LENGTH_SHORT).show()

        }

        switchSyncToGallery.setOnClickListener {
            Constants.isSaveGallery = !Constants.isSaveGallery
            Utils.setIsSaveGallerySharePreference(this, Constants.isSaveGallery)
            Log.d("handleEvent", "----:isSaveGallery-- " + Constants.isBlockAds)
        }
        btnLanguage.setOnClickListener {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun feedBack() {
        val uriText = "mailto:${Constants.email_feedback}" + "?subject=" + Uri.encode("Feedback") + "&body=" + Uri.encode(getSystemInfo() + errorUserPick)
        val uri = Uri.parse(uriText)
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = uri
        startActivity(Intent.createChooser(sendIntent, "Send email"))

    }

    private fun getSystemInfo(): String {
        // Get the PackageManager
        val packageManager = packageManager


        val versionCode = packageManager.getPackageInfo(this.packageName, 0).versionCode

        val deviceModel = Build.MODEL // Lấy dòng máy của thiết bị
        val androidVersion = Build.VERSION.RELEASE // Lấy phiên bản Android của thiết bị
        val defaultLanguage = Locale.getDefault().displayLanguage // Lấy ngôn ngữ mặc định của thiết bị
        val timezone = TimeZone.getDefault().displayName // Lấy múi giờ của thiết bị

        return "Version App: $versionCode\n Device model: $deviceModel\nAndroid version: $androidVersion\nDefault language: $defaultLanguage\nTimezone: $timezone"
    }

    private fun shareApp() {
        ShareCompat.IntentBuilder.from(this).setType("text/plain").setChooserTitle("Chooser title").setText("http://play.google.com/store/apps/details?id=" + this.packageName).startChooser()
    }


}