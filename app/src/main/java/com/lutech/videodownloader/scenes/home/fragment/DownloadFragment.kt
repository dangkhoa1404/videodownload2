package com.lutech.videodownloader.scenes.home.fragment

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.lutech.videodownloader.BuildConfig
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.FragmentAppBinding
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.Utils.checkStatusInternet
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.sharedPreference
import com.bumptech.glide.Glide
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.intro.activity.IntroActivity
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.YoutubeDLResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File


class DownloadFragment : Fragment() {

    private var downloading = false

    private val compositeDisposable = CompositeDisposable()

    private var mContext: Context? = null

    private lateinit var downloadBinding : FragmentAppBinding

    private var isRunningVoice = false

    private val callback: (progress: Float, etaInSeconds: Long, line: String) -> Unit = { progress, etaInSeconds, line ->
        requireActivity().runOnUiThread {
            if(progress != -1F) {
                Log.d("===>0249204", "yes: ")
                (mContext as HomeActivity).createNotification(progress, line)
            } else {
                Log.d("===>0249204", "no: ")
            }
        }
    }

    private val processId = "MyDlProcess"

    private val clipboardText: String
        get() {
            val clipboard = (mContext as HomeActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                return clip.getItemAt(0).text.toString()
            }
            return ""
        }

    private var mPathURL: String = "https://www.google.com/"

    private lateinit var mDownloadVideoVM: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        downloadBinding = FragmentAppBinding.inflate(inflater, container, false)
        return downloadBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initViews()
        handleEvent()
    }

    private fun initData() {

        (mContext as HomeActivity).mHomeVM.let {
            mDownloadVideoVM = it
        }

//        downloadBinding.webView.apply {
//            webViewClient = WebViewClient()
//            apply {
//                when {
//                    URLUtil.isValidUrl(mPathURL) -> loadUrl(mPathURL)
//                    mPathURL.contains(".com", ignoreCase = true) -> loadUrl(mPathURL)
//                    else -> loadUrl("https://www.google.com/search?q=$mPathURL")
//                }
//            }
//            webChromeClient = object : WebChromeClient() {
//                override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
//                    super.onReceivedIcon(view, icon)
//                    try {
//                        Glide.with(mContext!!).asBitmap().load(icon).into(downloadBinding.ivIconWebSite)
//                    } catch (e: Exception) {
//                        Glide.with(mContext!!).asBitmap().load(R.drawable.ic_bottom_nav_home).into(downloadBinding.ivIconWebSite)
//                    }
//                }
//            }
//        }

    }

    private fun initViews() {
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)) {
            if (!isStoragePermissionGranted()) {
                Toast.makeText(mContext, "grant storage permission and retry", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(mContext, "grant storage permission success", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleEvent() {
        downloadBinding.apply {
            btnDownload.setOnClickListener {
                if (editTextLink.text.toString() != ""){
                    if (checkStatusInternet(mContext!!) == 1) {
                        startDownload()
                    } else if (checkStatusInternet(mContext!!) == 2 && (Constants.isDownloadWithWIFIOnly == true)) {
                        Log.d("btn_start_download", "day 1")
                        Toast.makeText(mContext, getText(R.string.txt_please_turn_off_option_download_with_wifi_only), Toast.LENGTH_SHORT).show()

                    } else if (checkStatusInternet(mContext!!) == 2 && (Constants.isDownloadWithWIFIOnly == false)) {
                        Log.d("btn_start_download", "day 2")
                        startDownload()
                    } else if (checkStatusInternet(mContext!!) == 0) {
                    Toast.makeText(mContext, getText(R.string.txt_no_internet), Toast.LENGTH_SHORT).show()

                    }
                }else {
                    editTextLink.error = getString(R.string.txt_enter_valid_url)
                }

            }

            btnParse.setOnClickListener {
                if(!clipboardText.contains("https://")) {
                    Toast.makeText(mContext, getText(R.string.txt_empty_clipboard), Toast.LENGTH_SHORT).show()
                } else {
                    if(editTextLink.text.isNotEmpty()) {
                        Toast.makeText(mContext, getText(R.string.txt_already_has_link), Toast.LENGTH_SHORT).show()
                    } else {
                        editTextLink.setText(clipboardText)
                    }
                }
            }

            (mContext as HomeActivity).onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        gone(frameLayoutWebsite)
                    }
                }
            })

            clHowToDownload.setOnClickListener {
                startActivity(Intent(mContext, IntroActivity::class.java).apply {
                    putExtra(Constants.IS_FROM_HOME_ACTIVITY, true)
                })
            }
        }
    }

    private fun startDownload() {
        if (downloading) {
            Toast.makeText(mContext, R.string.txt_cannot_start_download_a_download_is_already_in_progress, Toast.LENGTH_SHORT).show()
            return
        }

        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)) {
            if (!isStoragePermissionGranted()) {
                Toast.makeText(mContext, getString(R.string.txt_grant_storage_permission_and_retry), Toast.LENGTH_LONG).show()
                return
            }
        }

        val url = downloadBinding.editTextLink.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(url)) {
            downloadBinding.editTextLink.error = getString(R.string.url_error)
            return
        }

        val request = YoutubeDLRequest(url)
        val youtubeDLDir: File = Utils.getDownloadLocation()
        val config = File(youtubeDLDir, "config.txt")

        if (config.exists()) {
            request.addOption("--config-location", config.absolutePath)
        } else {
            request.addOption("--no-mtime")
            request.addOption("--downloader", "libaria2c.so")
            request.addOption("--external-downloader-args", "aria2c:\"--summary-interval=1\"")
            request.addOption("-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best")
            request.addOption("-o", youtubeDLDir.absolutePath + "/%(title)s.%(ext)s")
        }

        downloading = true

        val disposable = Observable.fromCallable { YoutubeDL.getInstance().execute(request, processId, callback) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _: YoutubeDLResponse ->

                Log.d("===>0248294", "download = success")

                downloading = false

                downloadBinding.editTextLink.text.clear()

                (mContext as HomeActivity).setSuccessDownloadNotification(getString(R.string.download_complete))

                mDownloadVideoVM.checkAllowVideoPermission(true)

            }) { e: Throwable ->
                if (BuildConfig.DEBUG) Log.e(
                    "===>09092402", java.lang.String.valueOf(R.string.txt_fail_to_download), e
                )

                Log.d("===>0248294", "download = fail")

                downloading = false

                downloadBinding.editTextLink.text.clear()

                (mContext as HomeActivity).setSuccessDownloadNotification(getString(R.string.download_failed))
            }
        compositeDisposable.add(disposable)
    }

    private fun memoryStorageOfDevice() {
        // Fetching internal memory information
        val iPath: File = Environment.getDataDirectory()

        val iStat = StatFs(iPath.path)

        val iBlockSize = iStat.blockSizeLong

        val iAvailableBlocks = iStat.availableBlocksLong

        val iTotalBlocks = iStat.blockCountLong

        val iUsedBlocks = (iTotalBlocks - iAvailableBlocks)

        val iAvailableSpace = Utils.formatSizeOfMemory(iAvailableBlocks * iBlockSize)
        val iUsedSpace = Utils.formatSizeOfMemory(iUsedBlocks * iBlockSize)
        val iTotalSpace = Utils.formatSizeOfMemory(iTotalBlocks * iBlockSize)

        Log.d("===>0248294", "iAvailableSpace: " + iAvailableSpace)
        Log.d("===>0248294", "iUsedSpace: " + iUsedSpace)
        Log.d("===>0248294", "iTotalSpace: " + iTotalSpace)
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions((mContext as HomeActivity), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else {
            true
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
