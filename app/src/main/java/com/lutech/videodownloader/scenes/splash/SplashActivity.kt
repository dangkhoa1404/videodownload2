package com.lutech.videodownloader.scenes.splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lutech.videodownloader.BuildConfig
import com.lutech.videodownloader.databinding.ActivitySplashBinding
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.utils.sharedPreference
import com.yausername.youtubedl_android.YoutubeDL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashActivity :AppCompatActivity() {

    private lateinit var splashBinding : ActivitySplashBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        } else {
            window?.statusBarColor = Color.TRANSPARENT
        }

        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        initView()
        initData()
    }

    private fun initData() {
        if(sharedPreference.isUpdateYoutubeDL) {
            Log.d("===>02492049204", "is update: ")
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            Log.d("===>02492049204", "is not update: ")
            updateYoutubeDL()
        }
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, splashBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun updateYoutubeDL() {
        val disposable = Observable.fromCallable<YoutubeDL.UpdateStatus?> {
            YoutubeDL.getInstance().updateYoutubeDL(this, YoutubeDL.UpdateChannel._STABLE)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ status: YoutubeDL.UpdateStatus? ->
                when (status) {
                    YoutubeDL.UpdateStatus.DONE -> {
                        Log.d("===>02049299", "done")
                        sharedPreference.isUpdateYoutubeDL = true
                        startActivity(Intent(this, HomeActivity::class.java))
                    }

                    YoutubeDL.UpdateStatus.ALREADY_UP_TO_DATE -> {
                        Log.d("===>02049299", "already to update")
                        startActivity(Intent(this, HomeActivity::class.java))
                    }

                    else ->
                        Toast.makeText(this, status.toString(), Toast.LENGTH_LONG).show()
                }
            }) { e: Throwable? ->
                if (BuildConfig.DEBUG)
                    Log.e("===>20492042", "failed to update", e)
                Toast.makeText(this, "update failed", Toast.LENGTH_LONG).show()
            }
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

}