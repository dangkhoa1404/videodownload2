package com.lutech.videodownloader.utils

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lutech.videodownloader.R
import java.io.File
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToInt

object Utils {

//    fun onCreateDialog(
//        context: Context,
//        layout: Int,
//        isCanceledOnTouchOutside: Boolean = false
//    ): Dialog {
//        val dialog = Dialog(context)
//        dialog.setContentView(layout)
//        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        return dialog
//    }
//
//    fun onCreateBottomSheetDialog(
//        context: Context,
//        dialog_update_version: Int,
//        isCanceledOnTouchOutside: Boolean = true
//    ): Dialog {
//        val dialogRate =
//            BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
//        dialogRate.setContentView(dialog_update_version)
//        dialogRate.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
//        dialogRate.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialogRate.window!!.setGravity(Gravity.BOTTOM)
//        dialogRate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        return dialogRate
//    }

//    fun isInternetAvailable(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
//    }
//
//    fun createFileShareIntent(chooserTitle: String?, fileUri: Uri?): Intent? {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
//        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//        shareIntent.type = "*/*"
//        return Intent.createChooser(shareIntent, chooserTitle)
//    }
//
//    fun copyToClipboard(context: Context, url: String) {
//        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("URL", url)
//        clipboard.setPrimaryClip(clip)
//    }
//
//    fun openInPinterest(videoUrl: String, context: Context) {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("pinterest://video?url=$videoUrl"))
//        val packageManager = context.packageManager
//        if (intent.resolveActivity(packageManager) != null) {
//            context.startActivity(intent)
//        } else {
//            // Handle case where Pinterest app is not installed on the device
//        }
//    }
//
//    fun openInFacebook(videoUrl: String, context: Context) {
//        val facebookAppIntent = context.packageManager.getLaunchIntentForPackage("com.facebook.katana")
//        if (facebookAppIntent != null) {
//            val facebookVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
////            facebookVideoIntent.setPackage("com.facebook.katana")
//            context.startActivity(facebookVideoIntent)
//        } else {
//            // Chưa cài đặt Facebook app
//            val openInBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
//            context.startActivity(openInBrowserIntent)
//        }
//    }
//
//    fun openInTiktok(videoUrl: String, context: Context) {
//        val facebookAppIntent = context.packageManager.getLaunchIntentForPackage(Constants.PACKAGE_TIKTOK)
//        if (facebookAppIntent != null) {
//            val facebookVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
//            facebookVideoIntent.setPackage(Constants.PACKAGE_TIKTOK)
//            context.startActivity(facebookVideoIntent)
//        } else {
//            // Chưa cài đặt Facebook app
//            val openInBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
//            context.startActivity(openInBrowserIntent)
//        }
//    }
//
//    fun openInApp(videoUrl: String, context: Context, packageApp: String) {
//        val facebookAppIntent = context.packageManager.getLaunchIntentForPackage(packageApp)
//        if (facebookAppIntent != null) {
//            val facebookVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
//            facebookVideoIntent.setPackage(packageApp)
//            context.startActivity(facebookVideoIntent)
//        } else {
//            // Chưa cài đặt Facebook app
//            val openInBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
//            context.startActivity(openInBrowserIntent)
//        }
//    }

    fun setIsDownloadWithWIFIOnlySharePreference(context: Context, isDownloadWithWIFIOnly: Boolean) {
        val prefs = context.getSharedPreferences("InitAppPref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("isDownloadWithWIFIOnly", isDownloadWithWIFIOnly)
        editor.apply()
    }

    fun getIsDownloadWithWIFIOnlySharePreference(context: Context): Boolean {
        val prefs = context.getSharedPreferences("InitAppPref", Context.MODE_PRIVATE)
        return prefs.getBoolean("isDownloadWithWIFIOnly", false)
    }

    fun setIsBlockAdsSharePreference(context: Context, isBlockAds: Boolean) {
        val prefs = context.getSharedPreferences("InitAppPref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("isBlockAds", isBlockAds)
        editor.apply()
    }

    fun getIsBlockAdsSharePreference(context: Context): Boolean {
        val prefs = context.getSharedPreferences("InitAppPref", Context.MODE_PRIVATE)
        return prefs.getBoolean("isBlockAds", false)
    }

    fun setIsSaveGallerySharePreference(context: Context, isSaveGallery: Boolean) {
        val prefs = context.getSharedPreferences("InitAppPref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("isSaveGallery", isSaveGallery)
        editor.apply()
    }

    fun getIsSaveGallerySharePreference(context: Context): Boolean {
        val prefs = context.getSharedPreferences("InitAppPref", Context.MODE_PRIVATE)
        return prefs.getBoolean("isSaveGallery", false)
    }

    fun checkStatusInternet(context: Context) : Int {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return if (wifi!!.isConnectedOrConnecting) {
    //            Toast.makeText(context, "Wifi", Toast.LENGTH_LONG).show()
            1
        } else if (mobile!!.isConnectedOrConnecting) {

    //            Toast.makeText(context, "Mobile 3G ", Toast.LENGTH_LONG).show()
            2

        } else {
            0
    //            Toast.makeText(context, "No Network ", Toast.LENGTH_LONG).show()
        }

    }

    //New Ver 1
//    fun formatDurationFile(durationFile: Long): String {
//        return String.format("%02d:%02d", durationFile / 60000, durationFile % 60000 / 1000)
//    }

    fun formatSizeFile(sizeFile: Double): String {
        val totalMemory = sizeFile / 1024.0
        return if(totalMemory > (1024.0.pow(2.0))) {
            "${DecimalFormat("#.#").format(sizeFile / (1024.0.pow(3.0))).toDouble()} GB"
        } else {
            if(totalMemory > 1024.0) {
                "${DecimalFormat("#.#").format(sizeFile / (1024.0.pow(2.0))).toDouble()} MB"
            } else {
                "${DecimalFormat("#.#").format(sizeFile / (1024.0)).toDouble()} KB"
            }
        }
    }

    fun getDurationByPath(path: String): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(path)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            retriever.release()
            duration?.toLong() ?: "0".toLong()
        } catch (e: Exception) {
            retriever.release()
            "0".toLong()
        }
    }

    fun getDownloadLocation(): File {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val mDLDir = File(downloadsDir, Constants.APP_NAME_NEW)
        if (!mDLDir.exists()) mDLDir.mkdir()
        return mDLDir
    }

//    fun openFolder(context : Context) {
//        val path = Environment.getExternalStorageDirectory().toString() + "/" + "Downloads" + "/"
//        val uri = Uri.parse(path)
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.setDataAndType(uri, "*/*")
//        context.startActivity(intent)
//    }

    fun formatSizeOfMemory(size: Long): String {
        var newSizeOfMemory = size.toDouble()

        newSizeOfMemory /= (1024.0 * 1024.0 * 1024.0)

        val number3digits:Double = (newSizeOfMemory * 10.0).roundToInt() / 10.0

        val resultBuffer = StringBuilder(number3digits.toString())

        resultBuffer.append(" GB")

        return resultBuffer.toString()
    }

    fun shareSingleFile(pathSource: String, context: Context) {
        val fileToShare = File(pathSource)
        val fileUri = FileProvider.getUriForFile(context, "com.lutech.videodownloader.provider", fileToShare)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.type = "*/*"
        context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
    }

    fun shareLocalVideo(path: String, context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            val shareMessage = "${context.getString(R.string.txt_let_s_download_video)}\n$path"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.txt_something_went_wrong), Toast.LENGTH_SHORT).show()
        }
    }

    private var mLastClickTime: Long = 0L

    fun isClickRecently(delayTime: Long = 2000): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < delayTime) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    fun goToCHPlay(context: Context) {
        val appPackageName: String =
            context.packageName // getPackageName() from Context or Activity object
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }



}