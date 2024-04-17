package com.lutech.videodownloader.scenes.home.viewmodel

import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutech.videodownloader.model.VideoInProgress
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.net.URLConnection

class InProgressViewModel : ViewModel() {

    private val mGlobalListVideos = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
//        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    )

    private var _listAllVideoInProgress = MutableLiveData<List<VideoInProgress>>()

    val listAllVideoInProgress: LiveData<List<VideoInProgress>>
        get() = _listAllVideoInProgress

    fun getListGlobalVideo(mViewLoading: View) {
        val mListVideo: ArrayList<VideoInProgress> = arrayListOf()
        viewModelScope.launch {
            loadingView(mViewLoading, true)
            var contentLengthStr = ""
            withContext(Dispatchers.IO) {
                for (i in mGlobalListVideos.indices) {
                    val uri = URL(mGlobalListVideos[i])
                    val connection: URLConnection
                    try {
                        connection = uri.openConnection()
                        connection.connect()
                        contentLengthStr = connection.getHeaderField("content-length")

                        val nameVideo =
                            mGlobalListVideos[i].substringAfterLast("/").substringBefore(".mp4")

                        val memoryVideo = Utils.formatSizeFile(contentLengthStr.toDouble())

                        val durationVideo =
                            DateUtils.formatElapsedTime(Utils.getDurationByPath(mGlobalListVideos[i]) / 1000)

                        mListVideo.add(
                            i,
                            VideoInProgress(mGlobalListVideos[i], nameVideo, memoryVideo, durationVideo)
                        )

                    } catch (_: IOException) {}
                }
                _listAllVideoInProgress.postValue(mListVideo)
            }
            loadingView(mViewLoading, false)
        }
    }

    private fun loadingView(mViewLoading: View, isVisible: Boolean) {
        if (isVisible) {
            visible(mViewLoading)
        } else {
            gone(mViewLoading)
        }
    }
}