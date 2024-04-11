package com.lutech.videodownloader.model

import java.io.File

data class VideoFile(
    var videoType: Int,
    var url: String? = null,
    var file: File? = null,
    var videoDuration: Long,
    var isDownloadFinish: Boolean,
    var isBookMarked: Boolean = false
)
