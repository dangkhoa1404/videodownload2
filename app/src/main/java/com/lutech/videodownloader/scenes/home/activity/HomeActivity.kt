package com.lutech.videodownloader.scenes.home.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.lutech.videodownloader.base.BaseActivity
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.HomeActivityBinding
import com.lutech.videodownloader.scenes.home.adapter.HomeActivityViewPager
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.google.android.material.navigation.NavigationBarView
import com.lutech.videodownloader.scenes.home.viewmodel.AudioViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.VideoViewModel
import com.lutech.videodownloader.utils.permissionManager

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
            Log.d("===>204924", "isStorageGranted: ")
            mHomeVM.checkAllowAudioPermission(true)
            mHomeVM.checkAllowVideoPermission(true)
        } else {
            Log.d("===>204924", "is not StorageGranted: ")
            setStoragePermission()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel("1404", "notification", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val builder = NotificationCompat.Builder(this, "1404")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(titleNotification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) { if (ActivityCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
                notify(1, builder.build()) }
        }
    }

    private fun setStoragePermission() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (requestCode == Constants.CODE_REQUEST_STORAGE) {
                mHomeVM.checkAllowAudioPermission(false)
                mHomeVM.checkAllowVideoPermission(false)

            }
        } else {
            if (requestCode == Constants.CODE_REQUEST_STORAGE) {
                mHomeVM.checkAllowAudioPermission(true)
                mHomeVM.checkAllowVideoPermission(true)
            }
        }
    }
}