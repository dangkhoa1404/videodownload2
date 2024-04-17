package com.lutech.videodownloader.scenes.home.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    private var _newViewRCV = MutableLiveData<Int>()

    val newViewRCV: LiveData<Int>
        get() = _newViewRCV

    fun setLayoutForRCV(isVerticalView : Int) {
        _newViewRCV.value = isVerticalView
    }

    //list video in progress
    private var _isVideoIsProgressGranted = MutableLiveData<Boolean>()

    val isVideoIsProgressGranted: LiveData<Boolean>
        get() = _isVideoIsProgressGranted

    fun checkAllowVideoInProgress(isGranted: Boolean) {
        _isVideoIsProgressGranted.value = isGranted
    }
}