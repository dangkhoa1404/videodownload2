package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lutech.videodownloader.databinding.FragmentInprogressBinding
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.InProgressAdapter
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.InProgressViewModel
import com.lutech.videodownloader.scenes.watchvideo.activity.WatchVideoActivity
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.visible

class InprogressFragment : Fragment() {

    private lateinit var mInProgressBinding: FragmentInprogressBinding

    private var mContext: Context? = null

    private lateinit var mHomeVM: HomeViewModel

    private lateinit var mInProgressVM: InProgressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mInProgressBinding = FragmentInprogressBinding.inflate(inflater, container, false)
        return mInProgressBinding.root
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
        mInProgressVM = ViewModelProvider(this)[InProgressViewModel::class.java]
    }

    private fun initView() {
        setListVideo()
    }

    private fun setListVideo() {
        mHomeVM.isVideoIsProgressGranted.observe(viewLifecycleOwner) {
            if (it) {
                mInProgressVM.getListGlobalVideo(mInProgressBinding.sflLoadingData.root)
            } else {
                visible(mInProgressBinding.llVideoNotFound.root)
            }
        }

        mInProgressVM.listAllVideoInProgress.observe(viewLifecycleOwner) {
            mInProgressBinding.rcvListVideoInProgress.adapter = InProgressAdapter(mContext!!, it, object : InProgressAdapter.OnItemVideoPlayListener {
                override fun onItemVideoClick(position: Int) {
                    startActivity(Intent(mContext, WatchVideoActivity::class.java).apply {
                        putExtra(Constants.PATH_VIDEO_IN_PROGRESS, it[position].pathOfVideo)
                        putExtra(Constants.NAME_VIDEO_IN_PROGRESS, it[position].nameOfVideo)
                    })
                }
            })
        }
    }

    private fun handleEvent() {

    }

}