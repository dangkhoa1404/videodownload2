package com.lutech.videodownloader.scenes.playaudio

import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.lutech.videodownloader.database.ListAudio
import com.lutech.videodownloader.databinding.ActivityPlayAudioBinding
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.invisible
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class PlayAudioActivity : AppCompatActivity() {

    private lateinit var mPlayAudioBinding : ActivityPlayAudioBinding

    private var timer = Timer()

    private val mediaPlayerScope = CoroutineScope(Dispatchers.Default)

    private var mMediaPlayer: MediaPlayer? = null

    private var mListAudio: ArrayList<Audio> = arrayListOf()

    private var mFromMyDevice: Int = -1

    private var mPosOfPathAudio: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        } else {
            window?.statusBarColor = Color.TRANSPARENT
        }

        mPlayAudioBinding = ActivityPlayAudioBinding.inflate(layoutInflater)
        setContentView(mPlayAudioBinding.root)

        initData()
        initView()
        handleEvent()
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.Main) {
            mMediaPlayer = MediaPlayer()

            withContext(Dispatchers.IO) {
                mMediaPlayer!!.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )

                mListAudio = ListAudio.mListAudio

                mPosOfPathAudio = intent.getIntExtra(Constants.POS_AUDIO, 0)
            }
            playAudio(mListAudio[mPosOfPathAudio].pathOfAudio)
        }
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mPlayAudioBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun handleEvent() {
        mPlayAudioBinding.apply {

            imgPlaySong.setOnClickListener {
                if (mMediaPlayer != null) {
                    if (!mMediaPlayer!!.isPlaying) {
                        mMediaPlayer!!.start()
                        visible(imgPauseSong)
                        invisible(imgPlaySong)
                    }
                }
            }

            imgPauseSong.setOnClickListener {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer!!.isPlaying) {
                        mMediaPlayer!!.pause()
                        invisible(imgPauseSong)
                        visible(imgPlaySong)
                    }
                }
            }

            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            sbPlayAudio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        if (mMediaPlayer != null) {
                            mMediaPlayer!!.seekTo(progress)

                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            imgSharePathAudio.setOnClickListener {
                Utils.shareSingleFile(mListAudio[mPosOfPathAudio].pathOfAudio, this@PlayAudioActivity)
            }
        }
    }

    private fun playAudio(path: String) {
        invisible(mPlayAudioBinding.imgPlaySong)
        visible(mPlayAudioBinding.imgPauseSong)
        mediaPlayerScope.launch {
            mMediaPlayer!!.run {
                if (isPlaying) {
                    pause()
                    stop()
                }
                reset()
                if (!File(path).exists()) return@launch
                val localFileInputStream = FileInputStream(path)
                setDataSource(localFileInputStream.fd)
                prepare()
                start()
                runOnUiThread {
                    mPlayAudioBinding.apply {
                        sbPlayAudio.progress = 0
                        sbPlayAudio.max = duration
                        tvEndTime.text = DateUtils.formatElapsedTime((duration / 1000).toLong())
                    }

                }
                setOnCompletionListener {
                    invisible(mPlayAudioBinding.imgPauseSong)
                    visible(mPlayAudioBinding.imgPlaySong)
                }
            }
            updateSeekBar()
        }

    }

    private fun updateSeekBar() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                setCurrentTimeForSeekbar()
            }
        }, 0, 500)
    }

    fun setCurrentTimeForSeekbar() {
        runOnUiThread {
            mPlayAudioBinding.tvCurrentTime.text = SimpleDateFormat(
                "mm:ss", Locale.ENGLISH).format(mMediaPlayer!!.currentPosition)
            mPlayAudioBinding.sbPlayAudio.progress = mMediaPlayer!!.currentPosition
        }
    }

    override fun onRestart() {

        super.onRestart()
    }

    override fun onStop() {

        super.onStop()
    }

    override fun onDestroy() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
            mMediaPlayer!!.stop()
            mMediaPlayer!!.reset()
        }
        super.onDestroy()
    }
}