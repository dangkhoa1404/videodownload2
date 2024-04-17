package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.lutech.videodownloader.database.ListAudio
import com.lutech.videodownloader.databinding.DialogListFolderBinding
import com.lutech.videodownloader.databinding.FragmentMusicBinding
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.model.Folder
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.AudioAdapter
import com.lutech.videodownloader.scenes.home.adapter.FolderAdapter
import com.lutech.videodownloader.scenes.home.viewmodel.AudioViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.playaudio.activity.PlayAudioActivity
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

class MusicFragment : Fragment() {

    private var mContext : Context? = null

    private lateinit var mMusicBinding : FragmentMusicBinding

    private lateinit var mHomeVM: HomeViewModel

    private lateinit var mAudioVM: AudioViewModel

    private var mListAudio: MutableList<Audio> = mutableListOf()

    private var mIsListAllAudio : Boolean = true

    private var mListFolderAudioDialog : BottomSheetDialog? = null

    private var mFolderAdapter : FolderAdapter? = null

    private var mAudioAdapter : AudioAdapter? = null

    private var mNameFolder : String = Constants.ALL_FILE

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

        mMusicBinding.llFolderFilter.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                gone(mMusicBinding.clFilter)
                withContext(Dispatchers.IO) {
                    mNameFolder = Constants.ALL_FILE
                    if(mFolderAdapter!!.mPosCheckCurrentFolder != 0) {
                        mAudioAdapter!!.filterListAudio(mListAudio)
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

        mHomeVM.isAudioGranted.observe(viewLifecycleOwner) {
            if (it) {
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
            setListAudioView()
        }

        mAudioVM.folderMusicLists.observe(viewLifecycleOwner) {
            mMusicBinding.tvTotalFolders.text = it.size.toString()

            it.add(0, Folder("All"))

            setViewListFolderAudioDialog(it)
        }

        mHomeVM.newViewRCV.observe(viewLifecycleOwner) {
            setListAudioView()
        }
    }

    private fun setListAudioView() {
        if(mListAudio.isEmpty()) {
            visible(mMusicBinding.llMusicNotFound.root)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                gone(mMusicBinding.llMusicNotFound.root)

                withContext(Dispatchers.IO) {
                    mAudioAdapter =
                        AudioAdapter(
                            mContext!!,
                            mContext!!.sharedPreference.viewType,
                            if (mNameFolder == Constants.ALL_FILE) mListAudio else mListAudio.filter { it.parentOfAudio == mNameFolder},
                            object : AudioAdapter.OnItemMusicListener {
                                override fun onItemMusicClick(position: Int) {
                                    lifecycleScope.launch(Dispatchers.Main) {
                                        withContext(Dispatchers.IO) {
                                            if(ListAudio.mListAudio.isNotEmpty()) {
                                                ListAudio.mListAudio.clear()
                                            }
                                            ListAudio.mListAudio.addAll(if (mNameFolder == Constants.ALL_FILE) mListAudio else mListAudio.filter { it.parentOfAudio == mNameFolder})
                                        }
                                        startActivity(Intent(mContext, PlayAudioActivity::class.java).apply {
                                            putExtra(Constants.POS_AUDIO, position)
                                        })
                                    }
                                }
                                override fun onItemPosClick(position: Int) {

                                }
                            })
                }

                mMusicBinding.rcvListMusics.apply {
                    layoutManager = if(mContext!!.sharedPreference.viewType == 1)
                        LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL ,false)
                    else
                        GridLayoutManager(mContext, 2)

                    adapter = mAudioAdapter

                }
            }
        }
    }

    private fun setViewListFolderAudioDialog(mListFolderAudio : List<Folder>) {
        lifecycleScope.launch(Dispatchers.Main) {
            val mDialogFolderBinding = DialogListFolderBinding.inflate(layoutInflater)

            mListFolderAudioDialog = BottomSheetDialog(mContext!!, R.style.BottomSheetDialogTheme)
            mListFolderAudioDialog!!.setContentView(mDialogFolderBinding.root)
            withContext(Dispatchers.IO) {
                mFolderAdapter = FolderAdapter(mContext!!, mListFolderAudio, object : FolderAdapter.OnItemFolderListener {
                    override fun onItemFolderClick(position: Int) {
                        val mOldPos = mFolderAdapter!!.mPosCheckCurrentFolder

                        mFolderAdapter!!.mPosCheckCurrentFolder = position

                        if(mOldPos != mFolderAdapter!!.mPosCheckCurrentFolder) {
                            mFolderAdapter!!.notifyItemChanged(mOldPos)
                            mFolderAdapter!!.notifyItemChanged(mFolderAdapter!!.mPosCheckCurrentFolder)

                            if(mFolderAdapter!!.mPosCheckCurrentFolder == 0) {
                                gone(mMusicBinding.clFilter)
                                mNameFolder = Constants.ALL_FILE
                                mAudioAdapter!!.filterListAudio(mListAudio)
                            } else {
                                mMusicBinding.tvNameFolder.text = mListFolderAudio[mFolderAdapter!!.mPosCheckCurrentFolder].folderName
                                visible(mMusicBinding.clFilter)
                                mNameFolder = mListFolderAudio[mFolderAdapter!!.mPosCheckCurrentFolder].folderName
                                mAudioAdapter!!.filterListAudio(mListAudio.filter { it.parentOfAudio ==  mListFolderAudio[mFolderAdapter!!.mPosCheckCurrentFolder].folderName})
                            }
                        }

                        mListFolderAudioDialog!!.dismiss()
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

//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                behavior.maxHeight = resources.displayMetrics.heightPixels / 2
//                behavior.peekHeight = resources.displayMetrics.heightPixels / 2