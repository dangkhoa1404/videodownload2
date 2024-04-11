package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.FragmentPlayerBinding
import com.lutech.videodownloader.scenes.home.activity.HomeActivity
import com.lutech.videodownloader.scenes.home.adapter.PlayerFragmentViewPager
import com.lutech.videodownloader.scenes.home.viewmodel.AudioViewModel
import com.lutech.videodownloader.scenes.home.viewmodel.VideoViewModel

class PlayerFragment : Fragment() {

    private var mContext : Context? = null

    private lateinit var mPlayerBinding : FragmentPlayerBinding

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

        mVideoVM.listAllVideo.observe(viewLifecycleOwner) {
            mPlayerBinding.tvTotalVideo.text = it.size.toString()
        }

        mAudioVM.listTotalAudio.observe(viewLifecycleOwner) {
            mPlayerBinding.tvTotalMusic.text = it.toString()
        }

    }

    private fun initView() {
        setVisibleCurrentView(true)

        mPlayerBinding.vp2Player.apply {
            adapter = PlayerFragmentViewPager(this@PlayerFragment)
            isUserInputEnabled = false
            offscreenPageLimit = 2
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

            }
        }
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