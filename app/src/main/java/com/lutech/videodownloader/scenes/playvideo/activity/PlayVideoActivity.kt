package com.lutech.videodownloader.scenes.playvideo.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lutech.videodownloader.R
import com.lutech.videodownloader.database.ListVideo
import com.lutech.videodownloader.databinding.ActivityPlayVideo3Binding
import com.lutech.videodownloader.databinding.DialogFileVideoPlayBinding
import com.lutech.videodownloader.databinding.ExoPlaybackControlViewYtBinding
import com.lutech.videodownloader.model.Video
import com.lutech.videodownloader.scenes.playvideo.adapter.PlayVideoAdapter
import com.lutech.videodownloader.scenes.playvideo.base.BaseVideoActivity
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.invisible
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class PlayVideoActivity : BaseVideoActivity() {

    private lateinit var mPlayVideoBinding : ActivityPlayVideo3Binding

    private var isVideoFullscreen = false

    var mPosCurrentVideo = -1

    private lateinit var controlsBinding: ExoPlaybackControlViewYtBinding

    private var mListVideoPlay: ArrayList<Video> = arrayListOf()

    private var mDialogListVideoPlay: BottomSheetDialog? = null

    private var mPlayVideoAdapter : PlayVideoAdapter? = null

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

        lifecycleScope.launch(Dispatchers.Main) {
            videoPlayer = mPlayVideoBinding.previewPlayerView

            val mDialogVideoAudio = DialogFileVideoPlayBinding.inflate(layoutInflater)

            mDialogListVideoPlay = BottomSheetDialog(this@PlayVideoActivity, R.style.BottomSheetDialogTheme)

            mDialogListVideoPlay!!.behavior.apply {
                maxHeight = resources.displayMetrics.heightPixels * 3 / 4
                peekHeight = resources.displayMetrics.heightPixels * 3 / 4
            }

            mDialogListVideoPlay!!.setContentView(mDialogVideoAudio.root)

            withContext(Dispatchers.IO) {
                mPosCurrentVideo = intent.getIntExtra(Constants.POS_VIDEO, 0)

                mListVideoPlay = ListVideo.mListVideo

                mPlayVideoAdapter = PlayVideoAdapter(this@PlayVideoActivity, mListVideoPlay, object : PlayVideoAdapter.OnItemVideoPlayListener {
                    override fun onItemVideoPlayClick(position: Int) {
                        val mOldPos = mPosCurrentVideo

                        mPosCurrentVideo = position

                        if(mOldPos != mPosCurrentVideo) {
                            mPlayVideoAdapter!!.apply {
                                notifyItemChanged(mOldPos)
                                notifyItemChanged(mPosCurrentVideo)
                            }

                            startNextVideo(
                                mListVideoPlay[position].pathOfVideo,
                                mListVideoPlay[position].nameOfVideo,
                                mListVideoPlay[position].durationOfVideo
                            )
                        }
                    }

                    override fun onItemDeleteClick(position: Int) {
                        mPosCurrentVideo = position

                        mPlayVideoAdapter!!.notifyItemRemoved(position)

                        mListVideoPlay.removeAt(position)

                        if(position != mListVideoPlay.size) {

                            startNextVideo(
                                mListVideoPlay[position].pathOfVideo,
                                mListVideoPlay[position].nameOfVideo,
                                mListVideoPlay[position].durationOfVideo)
                        }

                        mDialogVideoAudio.rcvListFilePlay.adapter = mPlayVideoAdapter
                    }
                })

                mPlayVideoAdapter!!.notifyItemChanged(mPosCurrentVideo)
            }

            mDialogVideoAudio.apply {
                rcvListFilePlay.apply {
                    adapter = mPlayVideoAdapter
                    layoutParams.height = mDialogListVideoPlay!!.behavior.maxHeight * 3 / 4
                }
                tvSizeOfListPlay.text = mListVideoPlay.size.toString()
                imgBackCloseDialog.setOnClickListener {
                    mDialogListVideoPlay!!.dismiss()
                }
            }

            initDoubleTapPlayerView()

            startNextVideo(
                mListVideoPlay[mPosCurrentVideo].pathOfVideo,
                mListVideoPlay[mPosCurrentVideo].nameOfVideo,
                mListVideoPlay[mPosCurrentVideo].durationOfVideo)
        }
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mPlayVideoBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun handleEvent() {
        controlsBinding.apply {

            imgBack.setOnClickListener {
                if (isVideoFullscreen) {
                    toggleFullscreen()
                    return@setOnClickListener
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }

            imgPlaylistVideo.setOnClickListener {
                mDialogListVideoPlay!!.show()
            }

            //fullscreenButton.setOnClickListener {
//            toggleFullscreen()
//        }

            imgNext.setOnClickListener {
                if (!Utils.isClickRecently(1000)) {
                    val oldPos = mPosCurrentVideo
                    mPosCurrentVideo += 1

                    if(mPosCurrentVideo <= mListVideoPlay.size - 1) {
                        startNextVideo(
                            mListVideoPlay[mPosCurrentVideo].pathOfVideo,
                            mListVideoPlay[mPosCurrentVideo].nameOfVideo,
                            mListVideoPlay[mPosCurrentVideo].durationOfVideo)

                        mPlayVideoAdapter!!.apply {
                            notifyItemChanged(oldPos)
                            notifyItemChanged(mPosCurrentVideo)
                        }
                    }


                } else {
                    Toast.makeText(this@PlayVideoActivity, getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                }
            }

            imgPrevious.setOnClickListener {
                if (!Utils.isClickRecently(1000)) {
                    val oldPos = mPosCurrentVideo
                    mPosCurrentVideo -= 1

                    if(mPosCurrentVideo >= 0) {
                        startNextVideo(
                            mListVideoPlay[mPosCurrentVideo].pathOfVideo,
                            mListVideoPlay[mPosCurrentVideo].nameOfVideo,
                            mListVideoPlay[mPosCurrentVideo].durationOfVideo)

                        mPlayVideoAdapter!!.apply {
                            notifyItemChanged(oldPos)
                            notifyItemChanged(mPosCurrentVideo)
                        }
                    }

                } else {
                    Toast.makeText(this@PlayVideoActivity, getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                }
            }

            imgMuteVideo.setOnClickListener {
                player?.volume = 0F
                visible(imgUnmuteVideo)
                invisible(imgMuteVideo)
            }

            imgUnmuteVideo.setOnClickListener {
                player?.volume = 1F
                visible(imgMuteVideo)
                invisible(imgUnmuteVideo)
            }
        }
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

    private fun startNextVideo(pathVideo : String, nameVideo : String, durationVideo : String) {
        releasePlayer()

        initializePlayer()

        mPlayVideoBinding.ytOverlay.player(player!!)

        buildMediaSource(Uri.parse(pathVideo))

        player?.play()

        //change Layout
        controlsBinding.apply {
            tvEndTime.text = durationVideo

            tvAudioName.text = nameVideo

            tvAudioName.requestFocus()

            imgNext.apply {
                isEnabled = mPosCurrentVideo != mListVideoPlay.size - 1
                setColorFilter(
                    ContextCompat.getColor(this@PlayVideoActivity,
                    if(mPosCurrentVideo == mListVideoPlay.size - 1)
                        R.color.color_bg_play_audio_icon_play_audio_last_pos
                    else
                        R.color.color_white)
                )
            }
        }
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

    override fun onDestroy() {
        lifecycleScope.cancel()
        super.onDestroy()
    }
}