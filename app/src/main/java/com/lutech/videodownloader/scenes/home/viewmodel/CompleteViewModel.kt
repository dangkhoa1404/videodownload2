package com.lutech.videodownloader.scenes.home.viewmodel

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutech.videodownloader.model.Video
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("Range")
class CompleteViewModel : ViewModel() {

    private var _listVideoByFolder = MutableLiveData<List<Video>>()

    val listVideoByFolder: LiveData<List<Video>>
        get() = _listVideoByFolder

    fun getListVideoByFolder(fileDir: File, mViewLoading : View) {
        viewModelScope.launch {
            loadingView(mViewLoading, true)
            val listVideo = arrayListOf<Video>()
            val files = fileDir.listFiles()
            withContext(Dispatchers.IO) {
                if(files != null) {
                    for (i in files.indices) {
                        try {

                            if (files[i].exists() && files[i].length() != 0L && Utils.getDurationByPath(files[i].path) >= 1000) {
                                val video = files[i]
                                val path = video.path
                                val name = video.name.substringBeforeLast(".")
                                val sizePath = Utils.formatSizeFile(video.length().toDouble())
                                val dateAdded = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                    .format(Date(video.lastModified()))
                                val parentName = video.parentFile?.name ?: "null"
                                val duration = DateUtils.formatElapsedTime(Utils.getDurationByPath(video.path) / 1000)
                                listVideo.add(
                                    Video(path, name, sizePath, dateAdded, parentName, duration)
                                )
                            }

                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        } catch (e: RuntimeException) {
                            e.printStackTrace()
                        }
                    }

                    listVideo.sortWith { track1, track2 ->
                        val k: Long = File(track2.pathOfVideo).lastModified() - File(track1.pathOfVideo).lastModified()
                        if (k > 0) {
                            1
                        } else if (k == 0L) {
                            0
                        } else {
                            -1
                        }
                    }
                    _listVideoByFolder.postValue(listVideo)
                } else {
                    _listVideoByFolder.postValue(arrayListOf())
                }
            }
            loadingView(mViewLoading, false)
        }
    }

    private fun loadingView(mViewLoading : View, isVisible : Boolean) {
        if(isVisible) {
            visible(mViewLoading)
        } else {
            gone(mViewLoading)
        }
    }
}