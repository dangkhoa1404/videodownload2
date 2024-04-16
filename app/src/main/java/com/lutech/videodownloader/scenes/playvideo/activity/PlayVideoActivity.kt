package com.lutech.videodownloader.scenes.playvideo.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.lutech.videodownloader.R
import com.lutech.videodownloader.database.ListVideo
import com.lutech.videodownloader.databinding.ActivityPlayVideo3Binding
import com.lutech.videodownloader.databinding.ExoPlaybackControlViewYtBinding
import com.lutech.videodownloader.model.Video
import com.lutech.videodownloader.scenes.playvideo.base.BaseVideoActivity
import com.lutech.videodownloader.utils.Constants
import java.util.ArrayList

class PlayVideoActivity : BaseVideoActivity() {

    private lateinit var mPlayVideoBinding : ActivityPlayVideo3Binding

    private var isVideoFullscreen = false

    private var currentVideoId = -1

    private lateinit var controlsBinding: ExoPlaybackControlViewYtBinding

    private var mListVideoPlay: ArrayList<Video> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        } else {
            window?.statusBarColor = Color.TRANSPARENT
        }
        mPlayVideoBinding = ActivityPlayVideo3Binding.inflate(layoutInflater)
        val controls = mPlayVideoBinding.root.findViewById<ConstraintLayout>(R.id.exo_controls_root)
        controlsBinding = ExoPlaybackControlViewYtBinding.bind(controls)
        setContentView(mPlayVideoBinding.root)

        initData()
        initView()
        handleEvent()
    }

    private fun initData() {
//        ytOverlay = binding.ytOverlay
        videoPlayer = mPlayVideoBinding.previewPlayerView
        currentVideoId = intent.getIntExtra(Constants.POS_VIDEO, 0)
        mListVideoPlay = ListVideo.mListVideo
        initDoubleTapPlayerView()
        startNextVideo()
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mPlayVideoBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        controlsBinding.apply {
            tvEndTime.text = mListVideoPlay[currentVideoId].durationOfVideo
            tvAudioName.text = mListVideoPlay[currentVideoId].nameOfVideo
            tvAudioName.requestFocus()
        }
    }

    private fun handleEvent() {
        controlsBinding.imgBack.setOnClickListener {
            if (isVideoFullscreen) {
                toggleFullscreen()
                return@setOnClickListener
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
//        controlsBinding.fullscreenButton.setOnClickListener {
//            toggleFullscreen()
//        }
    }

    private fun initDoubleTapPlayerView() {
        mPlayVideoBinding.apply {
            ytOverlay
                // Uncomment this line if the DoubleTapPlayerView is not set via XML
                //.playerView(previewPlayerView)
                .performListener(object : YouTubeOverlay.PerformListener {
                    override fun onAnimationStart() {
                        previewPlayerView.useController = false
                        ytOverlay.visibility = View.VISIBLE
                    }
                    override fun onAnimationEnd() {
                        ytOverlay.visibility = View.GONE
                        previewPlayerView.useController = true
                    }
                })

            previewPlayerView.doubleTapDelay = 800
        }
    }

    private fun startNextVideo() {
        releasePlayer()
        initializePlayer()

        mPlayVideoBinding.ytOverlay.player(player!!)
        buildMediaSource(Uri.parse(mListVideoPlay[currentVideoId].pathOfVideo))
        player?.play()
    }

    private fun toggleFullscreen() {
        if (isVideoFullscreen) {
            setFullscreen(false)
            if (supportActionBar != null) {
                supportActionBar?.show()
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            isVideoFullscreen = false
        } else {
            setFullscreen(true)
            if (supportActionBar != null) {
                supportActionBar?.hide()
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            isVideoFullscreen = true
        }
    }

    override fun onBackPressed() {
        if (isVideoFullscreen) {
            toggleFullscreen()
            return
        }
        super.onBackPressed()
    }
}