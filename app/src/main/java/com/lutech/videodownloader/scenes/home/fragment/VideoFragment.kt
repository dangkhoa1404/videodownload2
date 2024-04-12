package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.sharedPreference
import com.lutech.videodownloader.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var mVideoAdapter : VideoAdapter? = null

    private var mNameFolder : String = Constants.ALL_FILE

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

        mVideoBinding.llFolderFilter.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                gone(mVideoBinding.clFilter)
                withContext(Dispatchers.IO) {
                    mNameFolder = Constants.ALL_FILE
                    if(mFolderAdapter!!.mPosCheckCurrentFolder != 0) {
                        mVideoAdapter!!.filterListVideo(mListVideo)
                    }
                }
                val mOldPos = mFolderAdapter!!.mPosCheckCurrentFolder
                mFolderAdapter!!.mPosCheckCurrentFolder = 0
                mFolderAdapter!!.notifyItemChanged(mOldPos)
                mFolderAdapter!!.notifyItemChanged(mFolderAdapter!!.mPosCheckCurrentFolder)
            }
        }
    }

    private fun setListenerVM() {
        mHomeVM.isVideoGranted.observe(viewLifecycleOwner) {
            if (it) {
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
            setTypeViewOfRCV()
            setListVideoView()
        }

        mVideoVM.folderVideoLists.observe(viewLifecycleOwner) {
            mVideoBinding.tvTotalFolders.text = it.size.toString()

            it.add(0, Folder(Constants.ALL_FILE))

            setViewListFolderVideoDialog(it)
        }

        mHomeVM.newViewRCV.observe(viewLifecycleOwner) {
            setTypeViewOfRCV()
            setListVideoView()
        }
    }

    private fun setTypeViewOfRCV() {
        mVideoBinding.rcvListVideos.layoutManager = if(mContext!!.sharedPreference.viewType == 1)
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL ,false)
        else
            GridLayoutManager(mContext, 2)
    }

    private fun setListVideoView() {
        if(mListVideo.isEmpty()) {
            visible(mVideoBinding.llVideoNotFound.root)
        } else {
            gone(mVideoBinding.llVideoNotFound.root)

            mVideoAdapter =
                VideoAdapter(
                    mContext!!,
                    mContext!!.sharedPreference.viewType,
                    if (mNameFolder == Constants.ALL_FILE) mListVideo else mListVideo.filter { it.parentoOfVideo == mNameFolder},
                    object : VideoAdapter.OnItemVideoListener {
                        override fun onItemVideoClick(position: Int) {

                        }
                        override fun onItemPosClick(position: Int) {

                        }
                    })

            mVideoBinding.rcvListVideos.adapter = mVideoAdapter
        }
    }

    private fun setViewListFolderVideoDialog(mListFolderVideo : List<Folder>) {
        lifecycleScope.launch(Dispatchers.Main) {
            val mDialogFolderBinding = DialogListFolderBinding.inflate(layoutInflater)

            mListFolderVideoDialog = BottomSheetDialog(mContext!!, R.style.BottomSheetDialogTheme)

            mListFolderVideoDialog!!.setContentView(mDialogFolderBinding.root)

            withContext(Dispatchers.IO) {
                mFolderAdapter = FolderAdapter(mContext!!, mListFolderVideo, object : FolderAdapter.OnItemFolderListener {
                    override fun onItemFolderClick(position: Int) {

                        val mOldPos = mFolderAdapter!!.mPosCheckCurrentFolder

                        mFolderAdapter!!.mPosCheckCurrentFolder = position

                        if(mOldPos != mFolderAdapter!!.mPosCheckCurrentFolder) {
                            mFolderAdapter!!.notifyItemChanged(mOldPos)
                            mFolderAdapter!!.notifyItemChanged(mFolderAdapter!!.mPosCheckCurrentFolder)

                            if(mFolderAdapter!!.mPosCheckCurrentFolder == 0) {
                                gone(mVideoBinding.clFilter)
                                mNameFolder = Constants.ALL_FILE
                                mVideoAdapter!!.filterListVideo(mListVideo)
                            } else {
                                mVideoBinding.tvNameFolder.text = mListFolderVideo[mFolderAdapter!!.mPosCheckCurrentFolder].folderName
                                visible(mVideoBinding.clFilter)
                                mNameFolder = mListFolderVideo[mFolderAdapter!!.mPosCheckCurrentFolder].folderName
                                mVideoAdapter!!.filterListVideo(mListVideo.filter { it.parentoOfVideo ==  mListFolderVideo[mFolderAdapter!!.mPosCheckCurrentFolder].folderName})
                            }
                        }

                        mListFolderVideoDialog!!.dismiss()
                    }
                })
            }
            mDialogFolderBinding.rcvListFolder.apply {
                layoutManager = FlexboxLayoutManager(mContext).apply {
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
                adapter = mFolderAdapter
                layoutParams.height = resources.displayMetrics.heightPixels / 2
            }
        }
    }

    override fun onDestroy() {
        lifecycleScope.cancel()
        super.onDestroy()
    }
}