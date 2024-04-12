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
import com.lutech.videodownloader.R
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

                            val artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                                ?: context.getString(R.string.txt_unknown)

                            if (audioFile.exists() && audioFile.length() != 0L && Utils.getDurationByPath(audioFile.path) >= 1000) {
                                val path = audioFile.path
                                val name = audioFile.name.substringBeforeLast(".")
                                val sizePath = Utils.formatSizeFile(audioFile.length().toDouble())
                                val dateAdded = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                    .format(Date(audioFile.lastModified()))
                                val parentName = audioFile.parentFile?.name ?: "null"
                                val duration = DateUtils.formatElapsedTime(Utils.getDurationByPath(audioFile.path) / 1000)
                                val artist = artistName
                                listAudios.add(
                                    Audio(
                                        path,
                                        name,
                                        sizePath,
                                        dateAdded,
                                        parentName,
                                        duration,
                                        artist
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

                            if (!parentFile.name.contains(Constants.APP_NAME_NEW)) {
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
                        Constants.APP_NAME_NEW
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