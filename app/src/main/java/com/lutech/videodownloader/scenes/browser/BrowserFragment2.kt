package com.lutech.videodownloader.scenes.browser

import androidx.fragment.app.Fragment

class BrowserFragment2(private var urlNew: String) : Fragment() {
//    private var url: String? = null
//    private var isShowDialogMore = false
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_browser2, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initData()
//        handleEvent()
//    }
//
//    private fun initData() {
//        myWebView.webViewClient = WebViewClient()
//        myWebView.settings.javaScriptEnabled = true
//        myWebView.apply {
//            when {
//                URLUtil.isValidUrl(urlNew) -> loadUrl(urlNew)
//                urlNew.contains(".com", ignoreCase = true) -> loadUrl(urlNew)
//                else -> loadUrl("https://www.google.com/search?q=$urlNew")
//            }
//        }
//        myWebView.webChromeClient = object : WebChromeClient() {
//            //for setting icon to our search bar
//            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
//                super.onReceivedIcon(view, icon)
//                try {
//                    ivIconWebSite.setImageBitmap(icon)
//
//                } catch (e: Exception) {
//                }
//            }
//
//
//        }
//        myWebView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                // do your stuff here
//                edSearch.setText(myWebView.url)
//                Log.d("link", ": "+edSearch.text+"---myWebView.url"+myWebView.url)
//
//            }
//        }
//
//    }
//
//
//    @SuppressLint("ClickableViewAccessibility")
//    private fun handleEvent() {
//
//        btnGotoDownloadActivity.setOnClickListener {
////            val myIntent = Intent(requireContext(), DownloadVideoActivity::class.java)
////            Constants.packageAppName = Constants.PACKAGE_WEBSITE
////            myIntent.putExtra(Constants.APP_PACKAGE_DOWNLOAD, Constants.PACKAGE_WEBSITE)
////            myIntent.putExtra("link", edSearch.text.toString())
////
////            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
////            val clip = ClipData.newPlainText("link", edSearch.text.toString())
////            clipboard.setPrimaryClip(clip)
////            startActivity(myIntent)
//        }
//
//        refreshBtn.setOnClickListener {
//            myWebView.loadUrl( "javascript:window.location.reload( true )" );
//        }
//
//        edSearch.setOnKeyListener(object : View.OnKeyListener {
//            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
//                // If the event is a key-down event on the "enter" button
//                if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    if (checkForInternet(requireContext())) {
//                        url = edSearch.text.toString()
//                        if (edSearch.text != null) {
//                            urlNew = edSearch.text.toString()
//                        }
//                        myWebView.apply {
//                            when {
//                                Patterns.WEB_URL.matcher(urlNew).matches() -> loadUrl("https://$urlNew")
//                                urlNew.contains(".com", ignoreCase = true) -> loadUrl(urlNew)
//                                else -> loadUrl("https://www.google.com/search?q=$urlNew")
//                            }
//                        }
//                        urlNew = edSearch.text.toString()
//                        hideKeyboard()
//
//
//                    } else {
//                        Toasty.error(requireContext(), getString(R.string.txt_no_internet), Toast.LENGTH_LONG).show()
//                    }
//                    return true
//                }
//                return false
//            }
//        })
//        btnMoreFragmentBrowser.setOnClickListener {
//            if (!isShowDialogMore) {
//                dialogMoreBrowserFragment.visibility = View.VISIBLE
//            } else {
//                dialogMoreBrowserFragment.visibility = View.GONE
//            }
//            isShowDialogMore = !isShowDialogMore
//        }
//
//        llShareLink.setOnClickListener {
//            shareLink()
//            hideDialogMoreFragmentBrowser()
//        }
//        llCopyLink.setOnClickListener {
//            copyLink()
//            hideDialogMoreFragmentBrowser()
//        }
//        llShareWithFriends.setOnClickListener {
//            handleShareApp()
//            hideDialogMoreFragmentBrowser()
//        }
//        llQRScan.setOnClickListener {
//            openQRScan()
//            hideDialogMoreFragmentBrowser()
//        }
//        llSetting.setOnClickListener {
//            startActivity(Intent(requireContext(), SettingActivity::class.java))
//            hideDialogMoreFragmentBrowser()
//        }
//        btnGotoHowtoDownload.setOnClickListener {
//            startActivity(Intent(requireContext(), IntroActivity::class.java))
//            hideDialogMoreFragmentBrowser()
//        }
//
//        wholeFragmentBrowser.setOnTouchListener(View.OnTouchListener { view: View?, motionEvent: MotionEvent? ->
//            dialogMoreBrowserFragment.visibility = View.GONE
//            isShowDialogMore = false
//            false
//        })
//        frameLayoutWebsite.setOnTouchListener(View.OnTouchListener { view: View?, motionEvent: MotionEvent? ->
//            dialogMoreBrowserFragment.visibility = View.GONE
//            isShowDialogMore = false
//            false
//        })
//    }
//
//    private fun hideDialogMoreFragmentBrowser() {
//        isShowDialogMore = false
//        dialogMoreBrowserFragment.visibility = View.GONE
//    }
//
//
//
//
//    private fun handleShareApp() {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app")
//        shareIntent.putExtra(
//            Intent.EXTRA_TEXT, " https://play.google.com/store/apps/details?id=" + activity?.packageName
//        )
//        val intent = Intent(requireContext(), ShareAppReceiver::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        val pi = PendingIntent.getBroadcast(
//            requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT.or(PendingIntent.FLAG_MUTABLE)
//        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            startActivity(Intent.createChooser(shareIntent, "choose one", pi.intentSender))
//        }
//
//    }
//
//    private fun hideKeyboard() {
//
//        val imm: InputMethodManager? = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
//    }
//
//
//    fun checkForInternet(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val network = connectivityManager.activeNetwork ?: return false
//            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
//
//            return when {
//                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                else -> false
//            }
//        } else {
//            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo ?: return false
//            @Suppress("DEPRECATION") return networkInfo.isConnected
//        }
//    }
//
//    private fun shareLink() {
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain"
//        intent.putExtra(Intent.EXTRA_SUBJECT, edSearch.text.toString())
//        intent.putExtra(Intent.EXTRA_TEXT, edSearch.text.toString())
//        startActivity(Intent.createChooser(intent, edSearch.text.toString()))
//    }
//
//    private fun copyLink() {
//        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("link", edSearch.text.toString())
//        clipboard.setPrimaryClip(clip)
//        Toasty.success(requireContext(), getString(R.string.txt_copied_to_clipbroad), Toast.LENGTH_LONG).show()
//    }
//
//    private fun openQRScan() {
//        val packageName = "com.lutech.qrcode.scanner"
//        try {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
//        } catch (anfe: ActivityNotFoundException) {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
//        }
//    }
}