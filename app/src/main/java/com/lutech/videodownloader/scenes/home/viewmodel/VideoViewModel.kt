package com.lutech.videodownloader.scenes.home.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutech.videodownloader.model.Folder
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
class VideoViewModel : ViewModel() {

    //get List Video
    private var _listAllVideo = MutableLiveData<List<Video>>()

    val listAllVideo: LiveData<List<Video>>
        get() = _listAllVideo

    private var _listVideoByFolder = MutableLiveData<List<Video>>()

    val listVideoByFolder: LiveData<List<Video>>
        get() = _listVideoByFolder

    private var _folderVideoLists = MutableLiveData<ArrayList<Folder>>()

    val folderVideoLists: LiveData<ArrayList<Folder>>
        get() = _folderVideoLists

    fun getListVideos(context : Context, mViewLoading : View) {
        viewModelScope.launch {
            loadingView(mViewLoading, true)
            val videoList: ArrayList<Video> = ArrayList()
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val videoCursor = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC"
            )
            withContext(Dispatchers.IO) {
                if (videoCursor != null) {
                    while (videoCursor.moveToNext()) {
                        val videoPath =
                            File(videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATA)))
                        if (videoPath.exists() && videoPath.length() != 0L && Utils.getDurationByPath(videoPath.path) >= 1000) {
                            val path = videoPath.path
                            val name = videoPath.name.substringBeforeLast(".")
                            val sizePath = Utils.formatSizeFile(videoPath.length().toDouble())
                            val dateAdded = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                .format(Date(videoPath.lastModified()))
                            val parentName = videoPath.parentFile?.name ?: "null"
                            val duration = DateUtils.formatElapsedTime(Utils.getDurationByPath(videoPath.path) / 1000)
                            videoList.add(
                                Video(path, name, sizePath, dateAdded, parentName, duration)
                            )
                        }
                    }
                    videoCursor.close()
                }
                _listAllVideo.postValue(videoList)
            }
            loadingView(mViewLoading, false)
        }
    }

    fun getAllFoldersVideos(context : Context) {
        val tempFolderList = ArrayList<String>()
        val listFoldersVideos = ArrayList<Folder>()
        var mCount = 0
        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.MediaColumns.DATE_ADDED + " DESC"
        )

        if (videoCursor != null) if (videoCursor.moveToNext()) do {
            val parentName =
                File(videoCursor.getString(videoCursor.getColumnIndex(MediaStore.MediaColumns.DATA))).parentFile!!

            if (!tempFolderList.contains(parentName.name) && !parentName.name.contains("Internal Storage")) {
                tempFolderList.add(parentName.name)
                listFoldersVideos.add(
                    Folder(
                        parentName.name
                    )
                )
                if (!parentName.name.contains(Constants.APP_NAME_NEW)) {
                    mCount += 1
                }
            }
        } while (videoCursor.moveToNext())
        videoCursor?.close()
        if (mCount == listFoldersVideos.size) {
            if (Constants.pathApp.exists()) {
                listFoldersVideos.add(
                    0, Folder(
                        Constants.APP_NAME_NEW
                    )
                )
            }
        }
        Log.d("===>204294241241", "listFoldersVideos: " + listFoldersVideos)
        _folderVideoLists.value = listFoldersVideos
    }

    private fun loadingView(mViewLoading : View, isVisible : Boolean) {
        if(isVisible) {
            visible(mViewLoading)
        } else {
            gone(mViewLoading)
        }
    }
}