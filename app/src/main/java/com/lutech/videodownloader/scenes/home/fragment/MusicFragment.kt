package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.DialogListFolderBinding
import com.lutech.videodownloader.databinding.FragmentMusicBinding
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.model.Folder
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.AudioAdapter
import com.lutech.videodownloader.scenes.home.adapter.FolderAdapter
import com.lutech.videodownloader.scenes.home.viewmodel.AudioViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.gone
import com.lutech.videodownloader.utils.sharedPreference
import com.lutech.videodownloader.utils.visible
import java.io.File

class MusicFragment : Fragment() {

    private var mContext : Context? = null

    private lateinit var mMusicBinding : FragmentMusicBinding

    private lateinit var mHomeVM: HomeViewModel

    private lateinit var mAudioVM: AudioViewModel

    private var mListAudio: MutableList<Audio> = mutableListOf()

    private var mIsListAllAudio : Boolean = true

    private var mListFolderAudioDialog : BottomSheetDialog? = null

    private var mFolderAdapter : FolderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mMusicBinding = FragmentMusicBinding.inflate(inflater, container, false)
        return mMusicBinding.root
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
        (mContext as HomeActivity).mAudioVM.let {
            mAudioVM = it
        }
        setListenerVM()
    }

    private fun initView() {}

    private fun handleEvent() {
        mMusicBinding.llListFolder.setOnClickListener {
            if(mListFolderAudioDialog != null) {
                mListFolderAudioDialog!!.show()
            }
        }
    }

    private fun setListenerVM() {

        mHomeVM.isAudioGranted.observe(viewLifecycleOwner) {
            if (it) {
                Log.d("===>204924", "all audio: ")
                mAudioVM.getListAudios(mContext!!, mMusicBinding.sflLoadingData.root)
                mAudioVM.getAllFoldersAudios(mContext!!)
            } else {
                visible(mMusicBinding.llMusicNotFound.root)
            }
        }

        mAudioVM.listAllAudio.observe(
            viewLifecycleOwner
        ) {
            mListAudio.run {
                clear()
                addAll(it)

                if(mIsListAllAudio) {
                    mMusicBinding.tvAmountOfMusics.text = it.size.toString()

                    var totalMemory = 0L

                    for(i in it.indices) {
                        totalMemory += File(it[i].pathOfAudio).length()
                    }
                    mMusicBinding.tvAmountOfMemory.text = Utils.formatSizeFile(totalMemory.toDouble())
                }
            }
            setListVideoView()
        }

        mAudioVM.folderMusicLists.observe(viewLifecycleOwner) {
            mMusicBinding.tvTotalFolders.text = it.size.toString()

            it.add(0, Folder("All"))

            setViewListFolderAudioDialog(it)
        }
    }

    private fun setListVideoView() {
        if(mListAudio.isEmpty()) {
            visible(mMusicBinding.llMusicNotFound.root)
        } else {
            gone(mMusicBinding.llMusicNotFound.root)
            mMusicBinding.rcvListMusics.apply {
                layoutManager = if(mContext!!.sharedPreference.viewType == 1)
                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL ,false)
                else
                    GridLayoutManager(mContext, 2)

                adapter =
                    AudioAdapter(mContext!!, mContext!!.sharedPreference.viewType, mListAudio, object : AudioAdapter.OnItemMusicListener {
                        override fun onItemMusicClick(position: Int) {

                        }

                        override fun onItemPosClick(position: Int) {

                        }
                    })
            }
        }

    }

    private fun setViewListFolderAudioDialog(mListFolderAudio : List<Folder>) {
        val mDialogFolderBinding = DialogListFolderBinding.inflate(layoutInflater)

        mListFolderAudioDialog = BottomSheetDialog(mContext!!, R.style.BottomSheetDialogTheme)
        mListFolderAudioDialog!!.apply {
            setContentView(mDialogFolderBinding.root)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.maxHeight = resources.displayMetrics.heightPixels / 2
            behavior.peekHeight = resources.displayMetrics.heightPixels / 2
        }

        mFolderAdapter = FolderAdapter(mContext!!, mListFolderAudio, object : FolderAdapter.OnItemFolderListener {
            override fun onItemFolderClick(position: Int) {
                val mOldPos = mFolderAdapter!!.mPosCheckCurrentFolder

                mFolderAdapter!!.mPosCheckCurrentFolder = position

                if(mOldPos != mFolderAdapter!!.mPosCheckCurrentFolder) {
                    mFolderAdapter!!.notifyItemChanged(mOldPos)
                    mFolderAdapter!!.notifyItemChanged(mFolderAdapter!!.mPosCheckCurrentFolder)
                }

                mListFolderAudioDialog!!.dismiss()
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