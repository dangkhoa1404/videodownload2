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
class HomeViewModel : ViewModel() {

    //check Video Permission
    private var _isVideoGranted = MutableLiveData<Boolean>()

    val isVideoGranted: LiveData<Boolean>
        get() = _isVideoGranted

    fun checkAllowVideoPermission(isVideoPerGranted: Boolean) {
        _isVideoGranted.value = isVideoPerGranted
    }

    // Check Audio Permission
    private var _isAudioGranted = MutableLiveData<Boolean>()

    val isAudioGranted: LiveData<Boolean>
        get() = _isAudioGranted

    fun checkAllowAudioPermission(isAudioPerGranted: Boolean) {
        _isAudioGranted.value = isAudioPerGranted
    }
}