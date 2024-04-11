package com.lutech.videodownloader.utils

import android.os.Environment
import com.lutech.videodownloader.model.VideoFile
import java.io.File

object Constants {

    const val email_feedback = "teammarketing@lutech.ltd"

//    const val APP_NAME = "video_downloader"

    const val KEY_APP_OPEN = "keyappopen"
    const val APP_PACKAGE_DOWNLOAD = "app_package_download"
    const val LINK_DOWNLOAD = "link_download"
    const val PACKAGE_INSTAGRAM = "com.instagram.android"
    const val PACKAGE_FACEBOOK = "com.facebook.katana"
    const val PACKAGE_TIKTOK = "com.ss.android.ugc.trill"
    const val PACKAGE_YOUTUBE = "com.google.android.youtube"
    const val PACKAGE_PINTEREST = "com.pinterest"
    const val PACKAGE_WEBSITE = ""
    var isHasPermission = false

    lateinit var videoUrl: String

    var listVideoDownloaded: ArrayList<VideoFile>? = null

    var packageAppName = ""

    var videoLinkUrl = ""

    var isDownloadWithWIFIOnly = false

    var isBlockAds = false

    var isSaveGallery = true

    const val KEY_LANG = "keyLang"

    const val IS_SET_LANG = "isSetLang"

    //New App - Ver 1
    const val CODE_REQUEST_STORAGE = 100

    const val APP_NAME_NEW: String = "LuVideoDownloader"

    val pathApp = File(
        Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS, APP_NAME_NEW
    )

    const val YOUTUBE_DL: String = "YOUTUBE_DL"

    const val KEY_FLAG: String = "KEY_FLAG"

    const val FLAG_BY_COUNTRY: String = "FLAG_BY_COUNTRY"

    const val VOICE_CODE = 101

    const val VIEW_TYPE: String = "VIEW_TYPE"

    const val IS_FROM_HOME_ACTIVITY: String = "IS_FROM_MAIN_ACTIVITY"

    const val ALL_FILE: String = "All"
}