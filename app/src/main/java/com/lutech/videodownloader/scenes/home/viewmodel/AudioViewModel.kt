package com.lutech.videodownloader.scenes.home.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import android.text.format.DateUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.model.Folder
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
class AudioViewModel : ViewModel() {

    //get List Audio
    private var _listAllAudio = MutableLiveData<List<Audio>>()

    val listAllAudio: LiveData<List<Audio>>
        get() = _listAllAudio

    private var _folderMusicLists = MutableLiveData<ArrayList<Folder>>()

    val folderMusicLists: LiveData<ArrayList<Folder>>
        get() = _folderMusicLists

    private var _listTotalAudio = MutableLiveData<Int>()

    val listTotalAudio: LiveData<Int>
        get() = _listTotalAudio

    fun getListAudios(context: Context, mViewLoading : View) {
        viewModelScope.launch {
            loadingView(mViewLoading, true)
            val listAudios = ArrayList<Audio>()
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DATE_ADDED + " DESC"
            )
            withContext(Dispatchers.IO) {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            val audioFile =
                                File(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))

                            if (audioFile.exists() && audioFile.length() != 0L && Utils.getDurationByPath(audioFile.path) >= 1000) {
                                val path = audioFile.path
                                val name = audioFile.name.substringBeforeLast(".")
                                val sizePath = Utils.formatSizeFile(audioFile.length().toDouble())
                                val dateAdded = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                    .format(Date(audioFile.lastModified()))
                                val parentName = audioFile.parentFile?.name ?: "null"
                                val duration = DateUtils.formatElapsedTime(Utils.getDurationByPath(audioFile.path) / 1000)
                                listAudios.add(
                                    Audio(
                                        path,
                                        name,
                                        sizePath,
                                        dateAdded,
                                        parentName,
                                        duration
                                    )
                                )
                            }
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
                _listAllAudio.postValue(listAudios)
                _listTotalAudio.postValue(listAudios.size)
            }
            loadingView(mViewLoading, false)
        }
    }

    fun getListAudiosByFolder(fileDir: File) {
        val listAudio = arrayListOf<Audio>()
        val files = fileDir.listFiles()
        if(files != null) {
            for (i in files.indices) {
                try {

                    if (files[i].exists() && files[i].length() != 0L && Utils.getDurationByPath(files[i].path) >= 1000
                    ) {
                        val video = files[i]
                        val path = video.path
                        val name = video.name.substringBeforeLast(".")
                        val sizePath = Utils.formatSizeFile(video.length().toDouble())
                        val dateAdded = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                            .format(Date(video.lastModified()))
                        val parentName = video.parentFile?.name ?: "null"
                        val duration = DateUtils.formatElapsedTime(Utils.getDurationByPath(video.path) / 1000)
                        listAudio.add(
                            Audio(
                                path,
                                name,
                                sizePath,
                                dateAdded,
                                parentName,
                                duration
                            )
                        )
                    }

                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }

            listAudio.sortWith { track1, track2 ->
                val k: Long = File(track2.pathOfAudio).lastModified() - File(track1.pathOfAudio).lastModified()
                if (k > 0) {
                    1
                } else if (k == 0L) {
                    0
                } else {
                    -1
                }
            }
            _listAllAudio.value = listAudio
        } else {
            _listAllAudio.value = arrayListOf()
        }
    }

    fun getAllFoldersAudios(context: Context) {
        val tempFolderList = ArrayList<String>()
        val listFolderAudio = ArrayList<Folder>()
        var mCount = 0
        val audioCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.MediaColumns.DATE_ADDED + " DESC"
        )

        if (audioCursor != null) {
            if (audioCursor.moveToNext()) {
                do {
                    val audioPath =
                        audioCursor.getString(audioCursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                    val parentFile = File(audioPath).parentFile

                    if (parentFile != null) {
                        if (!tempFolderList.contains(parentFile.name) && !parentFile.name.contains("Internal Storage")) {
                            tempFolderList.add(parentFile.name)
                            val folder = Folder(
                                parentFile.name
                            )
                            listFolderAudio.add(folder)

                            if (!parentFile.name.contains(Constants.APP_NAME)) {
                                mCount += 1
                            }
                        }
                    }
                } while (audioCursor.moveToNext())
            }
            audioCursor.close()
        }

        if (mCount == listFolderAudio.size) {
            if (Constants.pathApp.exists()) {
                listFolderAudio.add(
                    0, Folder(
                        Constants.APP_NAME,
                    )
                )
            }
        }
        _folderMusicLists.value = listFolderAudio
    }

    private fun loadingView(mViewLoading : View, isVisible : Boolean) {
        if(isVisible) {
            visible(mViewLoading)
        } else {
            gone(mViewLoading)
        }
    }
}