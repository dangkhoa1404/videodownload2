package com.lutech.videodownloader.scenes.home.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.HomeActivityBinding
import com.lutech.videodownloader.scenes.home.adapter.HomeActivityViewPager
import com.lutech.videodownloader.utils.Constants
import com.google.android.material.navigation.NavigationBarView
import com.lutech.videodownloader.scenes.home.viewmodel.AudioViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.VideoViewModel
import com.lutech.videodownloader.utils.permissionManager
import com.lutech.videodownloader.utils.sharedPreference

class HomeActivity : BaseActivity() {

    private lateinit var homeBinding: HomeActivityBinding

    lateinit var mHomeVM : HomeViewModel

    lateinit var mVideoVM : VideoViewModel

    lateinit var mAudioVM : AudioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        mHomeVM = ViewModelProvider(this)[HomeViewModel::class.java]
        mVideoVM = ViewModelProvider(this)[VideoViewModel::class.java]
        mAudioVM = ViewModelProvider(this)[AudioViewModel::class.java]
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {
        mHomeVM.checkAllowVideoInProgress(true)
        if(permissionManager.isStorageGranted()) {
            mHomeVM.checkAllowAudioPermission(true)
            mHomeVM.checkAllowVideoPermission(true)

            if(!permissionManager.isNotificationStorageGranted()) {
                when(sharedPreference.countTimeDenyNotifyPer) {
                    0 -> {
                        setNotificationPermission()
                    }
                    else -> {
                        setDialogRequestNotificationPermission()
                    }
                }
            }
        } else {
            when (sharedPreference.countTimeDenyPer) {
                0 -> {
                    setStoragePermission()
                }

                else -> {
                    val timeDeny = sharedPreference.countTimeDenyPer

                    val alert = AlertDialog.Builder(this)
                    alert.setTitle(getString(R.string.txt_permisison_required))
                    alert.setMessage(getString(R.string.txt_to_save_your_videos_we_need_storage))
                    alert.setPositiveButton(getString(if (timeDeny == 1) R.string.txt_ok_new else R.string.txt_setting_new)) { _, _ ->
                        if(timeDeny == 1) {
                            setStoragePermission()
                        } else {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.fromParts(Constants.PACKAGE, this.packageName, null)
                            startActivityForResult(intent, Constants.CODE_REQUEST_STORAGE)
                        }
                    }
                    alert.setNegativeButton(getString(R.string.txt_cancel_new), null)
                    val alertDialog = alert.create()
                    alertDialog.show()
                }
            }
        }


    }

    private fun initView() {
        homeBinding.vpDownloader.apply {
            adapter = HomeActivityViewPager(this@HomeActivity)
            isUserInputEnabled = false
            offscreenPageLimit = 4
        }
    }

    private fun handleEvent() {
        homeBinding.apply {
            btNavMain.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
                when (item.itemId) {

                    R.id.it_home -> {
                        vpDownloader.setCurrentItem(0, false)
                        return@OnItemSelectedListener true
                    }

                    R.id.it_hot_video -> {
                        vpDownloader.setCurrentItem(1, false)
                        return@OnItemSelectedListener true
                    }

                    R.id.it_download -> {
                        vpDownloader.setCurrentItem(2, false)
                        return@OnItemSelectedListener true
                    }

                    R.id.it_player -> {
                        vpDownloader.setCurrentItem(3, false)
                        return@OnItemSelectedListener true
                    }
                }
                false
            })
        }
    }

    fun createNotification(currentProgress : Float, currentText : String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mChannel = NotificationChannel("1404", "notification", NotificationManager.IMPORTANCE_LOW)
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val builder = NotificationCompat.Builder(this, "1404")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(currentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, currentProgress.toInt(), currentProgress == -1F)
                .setOngoing(true)

            with(NotificationManagerCompat.from(this)) { if (ActivityCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
                notify(1, builder.build())}
        }
    }

    fun setSuccessDownloadNotification(titleNotification : String) {
        Log.d("===>024920942", "create succes notify: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel("140401", "notification", NotificationManager.IMPORTANCE_LOW)
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val builder = NotificationCompat.Builder(this, "140401")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(titleNotification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) { if (ActivityCompat.checkSelfPermission(
                    this@HomeActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) { return }
                notify(1, builder.build()) }
        }
    }

    fun setDialogRequestNotificationPermission() {
        val timeDeny = sharedPreference.countTimeDenyNotifyPer

        val alert = AlertDialog.Builder(this)
        alert.setTitle(getString(R.string.txt_permisison_required))
        alert.setMessage(getString(R.string.txt_to_show_you_the_realtime_download_progress_notification))
        alert.setPositiveButton(getString(if (timeDeny == 1) R.string.txt_ok_new else R.string.txt_setting_new)) { _, _ ->
            if(timeDeny == 1) {
                setNotificationPermission()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts(Constants.PACKAGE, this.packageName, null)
                startActivityForResult(intent, Constants.CODE_REQUEST_NOTIFICATION)
            }
        }
        alert.setNegativeButton(getString(R.string.txt_cancel_new), null)
        val alertDialog = alert.create()
        alertDialog.show()
    }

    private fun setStoragePermission() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ), Constants.CODE_REQUEST_STORAGE
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ), Constants.CODE_REQUEST_STORAGE
            )
        }
    }

    private fun setNotificationPermission() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ), Constants.CODE_REQUEST_NOTIFICATION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CODE_REQUEST_STORAGE) {
            if (permissionManager.isStorageGranted()) {
                mHomeVM.checkAllowAudioPermission(true)
                mHomeVM.checkAllowVideoPermission(true)
                setNotificationPermission()
            } else {
                mHomeVM.checkAllowAudioPermission(false)
                mHomeVM.checkAllowVideoPermission(false)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (requestCode == Constants.CODE_REQUEST_STORAGE) {
                sharedPreference.countTimeDenyPer += 1
                Toast.makeText(this, getString(R.string.txt_permisison_denied), Toast.LENGTH_SHORT).show()
                mHomeVM.checkAllowAudioPermission(false)
                mHomeVM.checkAllowVideoPermission(false)
            }

            if (requestCode == Constants.CODE_REQUEST_NOTIFICATION) {
                sharedPreference.countTimeDenyNotifyPer += 1
                Toast.makeText(this, getString(R.string.txt_permisison_denied), Toast.LENGTH_SHORT).show()
            }

        } else {
            if (requestCode == Constants.CODE_REQUEST_STORAGE) {
                mHomeVM.checkAllowAudioPermission(true)
                mHomeVM.checkAllowVideoPermission(true)
                setNotificationPermission()
            }
        }
    }
}