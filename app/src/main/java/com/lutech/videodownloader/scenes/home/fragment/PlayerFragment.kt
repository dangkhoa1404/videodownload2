package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.FragmentPlayerBinding
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.PlayerFragmentViewPager
import com.lutech.videodownloader.scenes.home.viewmodel.AudioViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.HomeViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.VideoViewModel
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.sharedPreference

class PlayerFragment : Fragment() {

    private var mContext : Context? = null

    private lateinit var mPlayerBinding : FragmentPlayerBinding

    private lateinit var mHomeVM: HomeViewModel

    private lateinit var mVideoVM: VideoViewModel

    private lateinit var mAudioVM: AudioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mPlayerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return mPlayerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mContext as HomeActivity).mHomeVM.let {
            mHomeVM = it
        }
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {
        (mContext as HomeActivity).mVideoVM.let {
            mVideoVM = it
        }

        (mContext as HomeActivity).mAudioVM.let {
            mAudioVM = it
        }
    }

    private fun initView() {
        setVisibleCurrentView(true)
        setImgChangeViewRCV()
        mPlayerBinding.vp2Player.apply {
            adapter = PlayerFragmentViewPager(this@PlayerFragment)
            isUserInputEnabled = false
            offscreenPageLimit = 2
        }

        mVideoVM.listAllVideo.observe(viewLifecycleOwner) {
            mPlayerBinding.tvTotalVideo.text = it.size.toString()
        }

        mAudioVM.listTotalAudio.observe(viewLifecycleOwner) {
            mPlayerBinding.tvTotalMusic.text = it.toString()
        }

        mHomeVM.newViewRCV.observe(viewLifecycleOwner) {
            setImgChangeViewRCV()
        }
    }

    private fun handleEvent() {
        mPlayerBinding.apply {
            llListVideo.setOnClickListener {
                setVisibleCurrentView(true)
                vp2Player.setCurrentItem(0, false)
            }

            llListMusic.setOnClickListener {
                setVisibleCurrentView(false)
                vp2Player.setCurrentItem(1, false)
            }

            imgSortListData.setOnClickListener {
                if(!Utils.isClickRecently()) {
                    if(mContext!!.sharedPreference.viewType == 1) {
                        mContext!!.sharedPreference.viewType = 2
                    } else {
                        mContext!!.sharedPreference.viewType = 1
                    }
                    mHomeVM.setLayoutForRCV(mContext!!.sharedPreference.viewType)
                } else {
                    Toast.makeText(mContext, getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setImgChangeViewRCV() {
        Glide.with(mContext!!).load(if(mContext!!.sharedPreference.viewType == 1)
            R.drawable.ic_sort_list_2
        else
            R.drawable.ic_sort_list_1
        ).into(mPlayerBinding.imgSortListData)
    }
    private fun setVisibleCurrentView(isVisibleVideoView : Boolean) {
        mPlayerBinding.apply {
            isVisibleVideoView.let {
                llListVideo.isSelected = it

                cTotalFileVideo.isSelected = it

                imgListVideos.setColorFilter(
                    ContextCompat.getColor(
                        mContext!!,
                        if(it) R.color.color_white else R.color.color_black
                    )
                )

                tvTitleVideo.setTextColor(
                    ContextCompat.getColor(
                        mContext!!,
                        if(it) R.color.color_white else R.color.color_black
                    )
                )

                tvTotalVideo.setTextColor(
                    ContextCompat.getColor(
                        mContext!!,
                        if(it) R.color.color_item_primary else R.color.color_white
                    )
                )

                llListMusic.isSelected = !it

                cTotalFileMusic.isSelected = !it

                imgListMusics.setColorFilter(
                    ContextCompat.getColor(
                        mContext!!,
                        if(it) R.color.color_black else R.color.color_white
                    )
                )

                tvTitleAudio.setTextColor(
                    ContextCompat.getColor(
                        mContext!!,
                        if(it) R.color.color_black else R.color.color_white
                    )
                )

                tvTotalMusic.setTextColor(
                    ContextCompat.getColor(
                        mContext!!,
                        if(it) R.color.color_white else R.color.color_item_primary
                    )
                )
            }
        }
    }
}