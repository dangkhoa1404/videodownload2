package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.DialogListFolderBinding
import com.lutech.videodownloader.databinding.FragmentVideoBinding
import com.lutech.videodownloader.model.Folder
import com.lutech.videodownloader.model.Video
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.FolderAdapter
import com.lutech.videodownloader.scenes.home.adapter.VideoAdapter
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.VideoViewModel
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.sharedPreference
import com.lutech.videodownloader.utils.visible
import java.io.File

class VideoFragment : Fragment() {

    private var mContext : Context? = null

    private lateinit var mVideoBinding : FragmentVideoBinding

    private lateinit var mHomeVM: HomeViewModel

    private lateinit var mVideoVM: VideoViewModel

    private var mListVideo: MutableList<Video> = mutableListOf()

    private var mIsListAllVideo : Boolean = true

    private var mListFolderVideoDialog : BottomSheetDialog? = null

    private var mFolderAdapter : FolderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mVideoBinding = FragmentVideoBinding.inflate(inflater, container, false)
        return mVideoBinding.root
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
        (mContext as HomeActivity).mVideoVM.let {
            mVideoVM = it
        }
        setListenerVM()
    }

    private fun initView() {}

    private fun handleEvent() {
        mVideoBinding.llListFolder.setOnClickListener {
            if(mListFolderVideoDialog != null) {
                mListFolderVideoDialog!!.show()
            }
        }
    }

    private fun setListenerVM() {
        mHomeVM.isVideoGranted.observe(viewLifecycleOwner) {
            if (it) {
                Log.d("===>204924", "all video: ")
                mVideoVM.getListVideos(mContext!!, mVideoBinding.sflLoadingData.root)
                mVideoVM.getAllFoldersVideos(mContext!!)
            } else {
                visible(mVideoBinding.llVideoNotFound.root)
            }
        }

        mVideoVM.listAllVideo.observe(viewLifecycleOwner) {
            mListVideo.run {
                clear()
                addAll(it)

                if(mIsListAllVideo) {
                    mVideoBinding.tvAmountOfVideos.text = it.size.toString()

                    var totalMemory = 0L

                    for(i in it.indices) {
                        totalMemory += File(it[i].pathOfVideo).length()
                    }
                    mVideoBinding.tvAmountOfMemory.text = Utils.formatSizeFile(totalMemory.toDouble())
                }
            }
            setListVideoView()
        }

        mVideoVM.folderVideoLists.observe(viewLifecycleOwner) {
            mVideoBinding.tvTotalFolders.text = it.size.toString()

            it.add(0, Folder("All"))

            setViewListFolderVideoDialog(it)
        }
    }

    private fun setListVideoView() {
        if(mListVideo.isEmpty()) {
            visible(mVideoBinding.llVideoNotFound.root)
        } else {
            gone(mVideoBinding.llVideoNotFound.root)
            mVideoBinding.rcvListVideos.apply {
                layoutManager = if(mContext!!.sharedPreference.viewType == 1)
                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL ,false)
                else
                    GridLayoutManager(mContext, 2)

                adapter = VideoAdapter(mContext!!, mContext!!.sharedPreference.viewType, mListVideo, object : VideoAdapter.OnItemVideoListener {
                        override fun onItemVideoClick(position: Int) {

                        }

                        override fun onItemPosClick(position: Int) {

                        }
                    })
            }
        }
    }

    private fun setViewListFolderVideoDialog(mListFolderVideo : List<Folder>) {
        val mDialogFolderBinding = DialogListFolderBinding.inflate(layoutInflater)

        mListFolderVideoDialog = BottomSheetDialog(mContext!!, R.style.BottomSheetDialogTheme)

        mListFolderVideoDialog!!.apply {
            setContentView(mDialogFolderBinding.root)
            behavior.maxHeight = resources.displayMetrics.heightPixels / 2
            behavior.peekHeight = resources.displayMetrics.heightPixels / 2
        }

        mFolderAdapter = FolderAdapter(mContext!!, mListFolderVideo, object : FolderAdapter.OnItemFolderListener {
            override fun onItemFolderClick(position: Int) {
                val mOldPos = mFolderAdapter!!.mPosCheckCurrentFolder

                mFolderAdapter!!.mPosCheckCurrentFolder = position

                if(mOldPos != mFolderAdapter!!.mPosCheckCurrentFolder) {
                    mFolderAdapter!!.notifyItemChanged(mOldPos)
                    mFolderAdapter!!.notifyItemChanged(mFolderAdapter!!.mPosCheckCurrentFolder)
                }

                mListFolderVideoDialog!!.dismiss()
            }
        })

        mDialogFolderBinding.rcvListFolder.apply {
            layoutManager = FlexboxLayoutManager(mContext).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = mFolderAdapter

        }
    }
}