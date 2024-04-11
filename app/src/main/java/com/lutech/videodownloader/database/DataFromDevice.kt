package com.lutech.videodownloader.database

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.model.Folder
import com.lutech.videodownloader.model.Video
import java.io.File

@SuppressLint("Range")
class DataFromDevice(private val context: Context) {

//    private val endCodeAudio = arrayListOf("mp3", "wav", "wma", "flac", "aac", "ogg", "aiff", "m4a", "amr")
//
//    private val endCodeVideo = arrayListOf("mp4","mkv", "avi", "mov", "wmv")
//
//    fun getAllAudios(): ArrayList<Audio> {
//        val listAudios = ArrayList<Audio>()
//        val cursor = context.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            null,
//            null,
//            null,
//            MediaStore.Audio.Media.DATE_ADDED + " DESC"
//        )
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    val file =
//                        File(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
//
//                    if (file.exists() && file.length() != 0L && Utils.getDurationByPath(file.path) >= 1000) {
//                        listAudios.add(
//                            Audio(
//                                file.name.substringBeforeLast("."),
//                                Utils.getDurationByPath(file.path),
//                                file.path,
//                                file.length().toDouble()
//                            )
//                        )
//                    }
//                } while (cursor.moveToNext())
//            }
//            cursor.close()
//        }
//        return listAudios
//    }
//
//    fun getAllFoldersAudios(): ArrayList<Folder> {
//        val tempFolderList = ArrayList<String>()
//        val listFolderAudio = ArrayList<Folder>()
//        var mCount = 0
//        val audioCursor = context.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            null,
//            null,
//            null,
//            MediaStore.MediaColumns.DATE_ADDED + " DESC"
//        )
//
//        if (audioCursor != null) {
//            if (audioCursor.moveToNext()) {
//                do {
//                    val audioPath =
//                        audioCursor.getString(audioCursor.getColumnIndex(MediaStore.MediaColumns.DATA))
//                    val parentFile = File(audioPath).parentFile
//
//                    if (parentFile != null) {
//                        if (!tempFolderList.contains(parentFile.name) && !parentFile.name.contains("Internal Storage")) {
//                            tempFolderList.add(parentFile.name)
//                            val folder = Folder(
//                                parentFile.name
//                            )
//                            listFolderAudio.add(folder)
//
//                            if (!parentFile.name.contains(Constants.APP_NAME)) {
//                                mCount += 1
//                            }
//                        }
//                    }
//                } while (audioCursor.moveToNext())
//            }
//            audioCursor.close()
//        }
//
//        if (mCount == listFolderAudio.size) {
//            if (Constants.pathApp.exists()) {
//                listFolderAudio.add(
//                    0, Folder(
//                        Constants.APP_NAME,
//                    )
//                )
//            }
//        }
//
//        return listFolderAudio
//    }
//
//    fun getListAudioByFolder(fileDir: File): ArrayList<Audio> {
//        val listStudio = ArrayList<Audio>()
//        val files = fileDir.listFiles() ?: return listStudio
//        for (i in files.indices) {
//            try {
//                val p = files[i].path
//                if(endCodeAudio.contains(p.substringAfterLast("."))
//                    && files[i].length() != 0L
//                    && Utils.getDurationByPath(p) >= 1000) {
//                    val name = files[i].name
//                    listStudio.add(
//                        Audio(
//                            name, Utils.getDurationByPath(p), p, files[i].length().toDouble()
//                        )
//                    )
//                }
//
//            } catch (e: IllegalArgumentException) {
//                e.printStackTrace()
//            } catch (e: RuntimeException) {
//                e.printStackTrace()
//            }
//        }
//
//        listStudio.sortWith { track1, track2 ->
//            val k: Long = File(track2.path).lastModified() - File(track1.path).lastModified()
//            if (k > 0) {
//                1
//            } else if (k == 0L) {
//                0
//            } else {
//                -1
//            }
//        }
//        return listStudio
//    }
//
//    fun getListVideoByFolder(fileDir: File): ArrayList<Video> {
//        val listStudio = ArrayList<Video>()
//        val files = fileDir.listFiles() ?: return listStudio
//        for (i in files.indices) {
//            try {
//                val p = files[i].path
//
//                if (endCodeVideo.contains(p.substringAfterLast(".")) && files[i].length() != 0L && Utils.getDurationByPath(
//                        p
//                    ) >= 1000
//                ) {
//                    val name = files[i].name
//                    listStudio.add(
//                        Video(
//                            name,
//                            Utils.getDurationByPath(p),
//                            files[i].length().toDouble(),
//                            p,
//                        )
//                    )
//                }
//
//            } catch (e: IllegalArgumentException) {
//                e.printStackTrace()
//            } catch (e: RuntimeException) {
//                e.printStackTrace()
//            }
//        }
//
//        listStudio.sortWith { track1, track2 ->
//            val k: Long = File(track2.path).lastModified() - File(track1.path).lastModified()
//            if (k > 0) {
//                1
//            } else if (k == 0L) {
//                0
//            } else {
//                -1
//            }
//        }
//        return listStudio
//    }
//
//    fun getAllVideo(): ArrayList<Video> {
//        val filesList: ArrayList<Video> = ArrayList()
//        val projection = arrayOf(MediaStore.Video.Media.DATA)
//        val videoCursor = context.contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//            null,
//            null,
//            null,
//            MediaStore.MediaColumns.DATE_ADDED + " DESC"
//        )
//        if (videoCursor != null) {
//            while (videoCursor.moveToNext()) {
////                val dataColumnIndex = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA)
//                val videoPath =
//                    videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATA))
//                if (File(videoPath).exists() && File(videoPath).length() != 0L && Utils.getDurationByPath(
//                        videoPath
//                    ) >= 1000
//                ) {
//                    val file = File(videoPath)
//                    filesList.add(
//                        Video(
//                            file.name,
//                            Utils.getDurationByPath(videoPath),
//                            File(videoPath).length().toDouble(),
//                            videoPath,
//                        )
//                    )
//                }
//            }
//            videoCursor.close()
//        }
//        return filesList
//    }
//
//    fun getAllFoldersVideos(): ArrayList<Folder> {
//        val tempFolderList = ArrayList<String>()
//        val listFoldersVideos = ArrayList<Folder>()
//        var mCount = 0
//        val videoCursor = context.contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//            null,
//            null,
//            null,
//            MediaStore.MediaColumns.DATE_ADDED + " DESC"
//        )
//
//        if (videoCursor != null) if (videoCursor.moveToNext()) do {
//            val parentName =
//                File(videoCursor.getString(videoCursor.getColumnIndex(MediaStore.MediaColumns.DATA))).parentFile!!
//
//            if (!tempFolderList.contains(parentName.name) && !parentName.name.contains("Internal Storage")) {
//                tempFolderList.add(parentName.name)
//                listFoldersVideos.add(
//                    Folder(
//                        parentName.name
//                    )
//                )
//                if (!parentName.name.contains(Constants.APP_NAME)) {
//                    mCount += 1
//                }
//            }
//        } while (videoCursor.moveToNext())
//        videoCursor?.close()
//        if (mCount == listFoldersVideos.size) {
//            if (Constants.pathApp.exists()) {
//                listFoldersVideos.add(
//                    0, Folder(
//                        Constants.APP_NAME
//                    )
//                )
//            }
//        }
//        return listFoldersVideos
//    }
}