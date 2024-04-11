package com.lutech.videodownloader.scenes

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.lutech.videodownloader.BuildConfig
import com.lutech.videodownloader.R
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.activity_play_video2.*
import java.io.File


class PlayVideoActivity2 : AppCompatActivity(), Player.Listener {
    private var isMute = false
    private var currentVolume = 0

    //    private var filePath = DownloadAnyVideoFragment.listVideoDownloaded[0].file.toString()
    private var filePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video2)

        filePath = intent.getStringExtra("videoUrl").toString()


        initVideoPlayer()
        handleEvent()

    }

    private fun initVideoPlayer() {
        val videoView: VideoView = findViewById<View>(R.id.videoView1) as VideoView

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        Log.d("", "onCreate:filePath ---->$filePath")
        val uri = Uri.parse(filePath)

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }


    private fun handleEvent() {
        btnBack.setOnClickListener {
            finish()
        }
        btnOpenBrowser.setOnClickListener {
            Utils.openInApp(Constants.videoLinkUrl, this, Constants.packageAppName)
        }
        btnShareFile.setOnClickListener {
            val fileUri = FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider", File(Constants.videoUrl)
            )
            val intent: Intent? = Utils.createFileShareIntent(
                getString(R.string.txt_share), fileUri
            )
            startActivity(Intent.createChooser(intent, "share.."))
        }
        btnVolume.setOnClickListener {
            //mute/unmute volume device

            val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (!isMute) {
                currentVolume = getCurrentVolume(this)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                isMute = true
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
                isMute = false
            }
        }
    }

    fun getCurrentVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }




}
