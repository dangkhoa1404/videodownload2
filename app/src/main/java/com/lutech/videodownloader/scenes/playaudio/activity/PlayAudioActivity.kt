package com.lutech.videodownloader.scenes.playaudio.activity

import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lutech.videodownloader.R
import com.lutech.videodownloader.database.ListAudio
import com.lutech.videodownloader.databinding.ActivityPlayAudioBinding
import com.lutech.videodownloader.databinding.DialogFilePlayBinding
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.scenes.playaudio.adapter.PlayAudioAdapter
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.invisible
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
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

    private var mListAudioPlay: ArrayList<Audio> = arrayListOf()

//    private var mFromMyDevice: Int = -1

    var mPosOfPathAudio: Int = 0

    private var mDialogListAudioPlay: BottomSheetDialog? = null

    private var mPlayAudioAdapter : PlayAudioAdapter? = null

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

            val mDialogPlayAudio = DialogFilePlayBinding.inflate(layoutInflater)

            mDialogListAudioPlay = BottomSheetDialog(this@PlayAudioActivity, R.style.BottomSheetDialogTheme)

            mDialogListAudioPlay!!.behavior.apply {
                maxHeight = resources.displayMetrics.heightPixels * 3 / 4
                peekHeight = resources.displayMetrics.heightPixels * 3 / 4
            }

            mDialogListAudioPlay!!.setContentView(mDialogPlayAudio.root)

            withContext(Dispatchers.IO) {
                
                mMediaPlayer!!.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )

                mListAudioPlay = ListAudio.mListAudio

                mPosOfPathAudio = intent.getIntExtra(Constants.POS_AUDIO, 0)

                mPlayAudioAdapter =
                    PlayAudioAdapter(this@PlayAudioActivity, mListAudioPlay,
                        object : PlayAudioAdapter.OnItemAudioPlayListener {
                            override fun onItemAudioPlayClick(position: Int) {
                                val mOldPos = mPosOfPathAudio

                                mPosOfPathAudio = position

                                if(mOldPos != mPosOfPathAudio) {
                                    mPlayAudioAdapter!!.notifyItemChanged(mOldPos)
                                    mPlayAudioAdapter!!.notifyItemChanged(mPosOfPathAudio)

//                                    mPosOfPathAudio = position

                                    playAudio(mListAudioPlay[position].pathOfAudio)

                                    changeLayoutCurrentSoundPlaying(
                                        mListAudioPlay[position].pathOfAudio,
                                        mListAudioPlay[position].nameOfAudio,
                                        mListAudioPlay[position].artistOfAudio)
                                }
                            }

                            override fun onItemDeleteClick(position: Int) {
                                mPosOfPathAudio = position

                                mPlayAudioAdapter!!.notifyItemRemoved(position)

                                mListAudioPlay.removeAt(position)

                                if(position != mListAudioPlay.size) {
                                    playAudio(mListAudioPlay[position].pathOfAudio)

                                    changeLayoutCurrentSoundPlaying(
                                        mListAudioPlay[position].pathOfAudio,
                                        mListAudioPlay[position].nameOfAudio,
                                        mListAudioPlay[position].artistOfAudio)
                                }

                                mDialogPlayAudio.rcvListFilePlay.adapter = mPlayAudioAdapter
                            }
                })
                mPlayAudioAdapter!!.notifyItemChanged(mPosOfPathAudio)

            }

            mDialogPlayAudio.apply {
                rcvListFilePlay.apply {
                    adapter = mPlayAudioAdapter
                    layoutParams.height = mDialogListAudioPlay!!.behavior.maxHeight * 3 / 4
                }
                tvSizeOfListPlay.text = mListAudioPlay.size.toString()
                imgBackCloseDialog.setOnClickListener {
                    mDialogListAudioPlay!!.dismiss()
                }
            }

            changeLayoutCurrentSoundPlaying(
                mListAudioPlay[mPosOfPathAudio].pathOfAudio,
                mListAudioPlay[mPosOfPathAudio].nameOfAudio,
                mListAudioPlay[mPosOfPathAudio].artistOfAudio)

            playAudio(mListAudioPlay[mPosOfPathAudio].pathOfAudio)
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
                if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.start()
                    visible(imgPauseSong)
                    invisible(imgPlaySong)
                }
            }

            imgPauseSong.setOnClickListener {
                if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                    invisible(imgPauseSong)
                    visible(imgPlaySong)
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

            imgMuteAudio.setOnClickListener {
                mMediaPlayer!!.setVolume(0F, 0F)
                visible(imgUnmuteAudio)
                invisible(imgMuteAudio)
            }

            imgUnmuteAudio.setOnClickListener {
                mMediaPlayer!!.setVolume(0F, 1F)
                visible(imgMuteAudio)
                invisible(imgUnmuteAudio)
            }

            imgPlaylistAudio.setOnClickListener {
                mDialogListAudioPlay!!.show()
            }

            imgPrevious.setOnClickListener {
                if (!Utils.isClickRecently(1000)) {
                    val oldPos = mPosOfPathAudio
                    mPosOfPathAudio -= 1
                    if(mPosOfPathAudio >= 0) {
                        playAudio(mListAudioPlay[mPosOfPathAudio].pathOfAudio)

                        changeLayoutCurrentSoundPlaying(
                            mListAudioPlay[mPosOfPathAudio].pathOfAudio,
                            mListAudioPlay[mPosOfPathAudio].nameOfAudio,
                            mListAudioPlay[mPosOfPathAudio].artistOfAudio
                        )

                        mPlayAudioAdapter!!.apply {
                            notifyItemChanged(oldPos)
                            notifyItemChanged(mPosOfPathAudio)
                        }
                    }
                } else {
                    Toast.makeText(this@PlayAudioActivity, getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                }
            }

            imgNext.setOnClickListener {
                if (!Utils.isClickRecently(1000)) {
                    val oldPos = mPosOfPathAudio
                    mPosOfPathAudio += 1
                    if(mPosOfPathAudio <= mListAudioPlay.size - 1) {
                        playAudio(mListAudioPlay[mPosOfPathAudio].pathOfAudio)

                        changeLayoutCurrentSoundPlaying(
                            mListAudioPlay[mPosOfPathAudio].pathOfAudio,
                            mListAudioPlay[mPosOfPathAudio].nameOfAudio,
                            mListAudioPlay[mPosOfPathAudio].artistOfAudio
                        )

                        mPlayAudioAdapter!!.apply {
                            notifyItemChanged(oldPos)
                            notifyItemChanged(mPosOfPathAudio)
                        }
                    }
                } else {
                    Toast.makeText(this@PlayAudioActivity, getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                }
            }

            imgSharePathAudio.setOnClickListener {
                Utils.shareSingleFile(mListAudioPlay[mPosOfPathAudio].pathOfAudio, this@PlayAudioActivity)
            }
        }
    }

    private fun changeLayoutCurrentSoundPlaying(
        pathSong : String,
        nameAudio : String,
        artistAudio : String
    ) {
        mPlayAudioBinding.apply {
            tvAudioName.text = nameAudio

            tvAudioName.requestFocus()

            tvArtistName.text = artistAudio

            imgNext.apply {
                isEnabled = mPosOfPathAudio != mListAudioPlay.size - 1
                setColorFilter(ContextCompat.getColor(this@PlayAudioActivity,
                    if(mPosOfPathAudio == mListAudioPlay.size - 1)
                        R.color.color_bg_play_audio_icon_play_audio_last_pos
                    else
                        R.color.color_white)
                )
            }
            Glide.with(this@PlayAudioActivity).load(pathSong)
                .placeholder(R.drawable.ic_item_music).error(R.drawable.ic_item_music)
                .into(imgPathAudio)
        }
    }

    private fun playAudio(path: String) {
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
                        invisible(imgPlaySong)
                        visible(imgPauseSong)
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

    private fun setCurrentTimeForSeekbar() {
        runOnUiThread {
            mPlayAudioBinding.tvCurrentTime.text = SimpleDateFormat(
                "mm:ss", Locale.ENGLISH).format(mMediaPlayer!!.currentPosition)
            mPlayAudioBinding.sbPlayAudio.progress = mMediaPlayer!!.currentPosition
        }
    }

    override fun onRestart() {
        if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
        }
        super.onRestart()
    }

    override fun onStop() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        lifecycleScope.cancel()
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
            mMediaPlayer!!.stop()
            mMediaPlayer!!.reset()
        }
        super.onDestroy()
    }
}