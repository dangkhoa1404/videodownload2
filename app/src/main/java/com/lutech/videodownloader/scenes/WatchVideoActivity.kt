package com.lutech.videodownloader.scenes

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.lutech.videodownloader.BuildConfig
import com.lutech.videodownloader.R
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.custom_controller.*
import java.io.File

class WatchVideoActivity : AppCompatActivity() {

    private var filePath = ""
    var isFullScreen = false
    var isLock = false
    var isPause = false

    lateinit var bt_fullscreen: ImageView
//    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var simpleExoPlayer: ExoPlayer
    private var isMute = false
    private var currentVolume = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)
        initData()
        handleEvent()
    }

    private fun initData() {
        filePath = intent.getStringExtra("videoUri").toString()
        val playerView = findViewById<PlayerView>(R.id.player)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)
        ///
//        simpleExoPlayer = SimpleExoPlayer.Builder(this).setSeekBackIncrementMs(5000)
//            .setSeekForwardIncrementMs(5000).build()
        simpleExoPlayer = ExoPlayer.Builder(this).setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000).build()
        playerView.player = simpleExoPlayer
        playerView.keepScreenOn = true

        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.visibility = View.GONE
                }else if (playbackState == Player.STATE_ENDED) {
                   finish()
                }
            }
        })

//        val mediaItem = MediaItem.fromUri(filePath)
        val mediaItem = MediaItem.fromUri(Uri.parse(filePath))
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()


        bt_fullscreen.setOnClickListener {
            if (!isFullScreen) {
                bt_fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_baseline_fullscreen_exit_24
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                bt_fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_baseline_fullscreen_24
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isFullScreen = !isFullScreen
        }

        bt_lockscreen.setOnClickListener {
            if (!isLock) {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_baseline_lock_24
                    )
                )
            } else {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_baseline_lock_open_24
                    )
                )
            }
            isLock = !isLock
            lockScreen(isLock)
        }
    }

    private fun lockScreen(lock: Boolean) {
        val sec_mid = findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom = findViewById<LinearLayout>(R.id.sec_controlvid2)
        if (lock) {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        } else {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
//        initData()

    }

    override fun onBackPressed() {
        if (isLock) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bt_fullscreen.performClick()
        } else super.onBackPressed()

    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        Log.d("simpleExoPlayer", "onDestroy: ")
        simpleExoPlayer.pause()
        simpleExoPlayer.stop()
        simpleExoPlayer.release()
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }

    private fun handleEvent() {


        exo_exit.setOnClickListener {
            finish()
        }

        btnOpenBrowser2.setOnClickListener {
            Utils.openInApp(Constants.videoLinkUrl, this, Constants.packageAppName)
        }
        btnShareFile2.setOnClickListener {
            val fileUri = FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider", File(Constants.videoUrl)
            )
            val intent: Intent? = Utils.createFileShareIntent(
                getString(R.string.txt_share), fileUri
            )
            startActivity(Intent.createChooser(intent, "share.."))
        }
        btnVolume2.setOnClickListener {
            // mute/unmute volume device
            val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (!isMute) {
                currentVolume = getCurrentVolume(this)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                btnVolume2.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_volume_mute_24))
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
                btnVolume2.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_volume_up_24))
            }
            isMute =! isMute

        }

        exo_play_pause.setOnClickListener {
            if (!isPause) {
                simpleExoPlayer.pause()
                exo_play_pause.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_baseline_play_arrow_24
                    )
                )
            } else {
                simpleExoPlayer.play()
                exo_play_pause.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_baseline_pause_24
                    )
                )
            }
            isPause = !isPause

        }

    }

    fun getCurrentVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

}