package com.lutech.videodownloader.scenes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lutech.videodownloader.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util


class PlayVideoActivity : AppCompatActivity(), Player.Listener {

    private var mPlayer: ExoPlayer? = null
    private var simpleExoPlayer: ExoPlayer? = null
    private lateinit var playerView: StyledPlayerView

        private val videoURL =
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    ///storage/emulated/0/Download/AppVideoDownloader/Youtube/.trashed-1675678071-Countdown 5 seconds timer.mp4
//    private val videoURL =
//        "file:///storage/emulated/0/Download/InSaver_Album/d9983d346188dda1e1d81df537a7259c.mp4"
//    private var filePath = listVideoDownloaded[0].file.toString()

    //huy97


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        //get PlayerView by its id
//        Log.d("", "onCreate: listVideoDownloaded.size"+listVideoDownloaded.size+"---"+listVideoDownloaded[0].file?.absolutePath)
        playerView = findViewById(R.id.playerView)

    }

    private fun initPlayer() {

        // Create a player instance.
        mPlayer = SimpleExoPlayer.Builder(this).build()

        // Bind the player to the view.
        playerView.player = mPlayer

        //setting exoplayer when it is ready.
        mPlayer!!.playWhenReady = true

        // Set the media source to be played.
        mPlayer!!.setMediaSource(buildMediaSource())

        // Prepare the player.
        mPlayer!!.prepare()






    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || mPlayer == null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        //release player when done
        mPlayer!!.release()
        mPlayer = null
    }

    //creating mediaSource
    private fun buildMediaSource(): MediaSource {
        // Create a data source factory.
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        // Create a progressive media source pointing to a stream uri.
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoURL))
        //huy99
        return mediaSource
    }

}