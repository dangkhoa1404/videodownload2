package com.lutech.videodownloader.scenes.playvideo.base

import com.google.android.exoplayer2.extractor.Extractor
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor

class Mp4ExtractorFactory : ExtractorsFactory {

    override fun createExtractors(): Array<Extractor> {
        return arrayOf(
            Mp4Extractor()
        )
    }
}