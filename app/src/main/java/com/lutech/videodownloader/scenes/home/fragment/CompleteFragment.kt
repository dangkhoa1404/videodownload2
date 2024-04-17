package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lutech.videodownloader.database.ListVideo
import com.lutech.videodownloader.databinding.FragmentCompleteBinding
import com.lutech.videodownloader.model.Video
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.CompleteAdapter
import com.lutech.videodownloader.scenes.home.viewmodel.CompleteViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.playvideo.activity.PlayVideoActivity
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompleteFragment : Fragment() {

    private lateinit var mCompleteBinding: FragmentCompleteBinding

    private var mContext: Context? = null

    private lateinit var mHomeVM: HomeViewModel

    private lateinit var mCompleteVM: CompleteViewModel

    private var mListVideoDownloaded: MutableList<Video> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mCompleteBinding = FragmentCompleteBinding.inflate(inflater, container, false)
        return mCompleteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {
        (mContext as HomeActivity).mHomeVM.let {
            mHomeVM = it
        }
        mCompleteVM = ViewModelProvider(this)[CompleteViewModel::class.java]
        setListenerVM()
    }

    private fun initView() {}

    private fun handleEvent() {}

    private fun setListVideoView() {
        if(mListVideoDownloaded.isEmpty()) {
            visible(mCompleteBinding.llVideoNotFound.root)
        } else {
            gone(mCompleteBinding.llVideoNotFound.root)
            mCompleteBinding.rcvListAudioDownloaded.adapter =
                CompleteAdapter(mContext!!, mListVideoDownloaded, object : CompleteAdapter.OnItemCompleteDownloadedListener {
                    override fun onItemCompleteClick(position: Int) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                if(ListVideo.mListVideo.isNotEmpty()) {
                                    ListVideo.mListVideo.clear()
                                }
                                ListVideo.mListVideo.addAll(mListVideoDownloaded)
                            }
                            startActivity(Intent(mContext, PlayVideoActivity::class.java).apply {
                                putExtra(Constants.POS_VIDEO, position)
                            })
                        }
                    }

                    override fun onItemPosClick(position: Int) {

                    }
                })
        }
    }

    private fun setListenerVM() {
        mHomeVM.isVideoGranted.observe(viewLifecycleOwner) {
            if (it) {
                Log.d("===>204924", "complete video: ")
                mCompleteVM.getListVideoByFolder(Constants.pathApp, mCompleteBinding.sflLoadingData.root)
            } else {
                Log.d("===>204924", "not granted: ")
                visible(mCompleteBinding.llVideoNotFound.root)
            }
        }

        mCompleteVM.listVideoByFolder.observe(
            viewLifecycleOwner
        ) {
            mListVideoDownloaded.run {
                clear()
                addAll(it)
            }
            setListVideoView()
        }
    }

    override fun onDestroy() {
        lifecycleScope.cancel()
        super.onDestroy()
    }
}