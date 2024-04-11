package com.lutech.videodownloader.scenes.home.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.FragmentDownloadsBinding
import com.lutech.videodownloader.scenes.home.adapter.DownloadsFragmentViewPager
import com.google.android.material.tabs.TabLayout

class DownloadsFragment : Fragment() {

    private var mContext: Context? = null

    private lateinit var mDownloadsBinding: FragmentDownloadsBinding

    private val tabIcons = intArrayOf(
        R.drawable.ic_check_new,
        R.drawable.ic_progress_new
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mDownloadsBinding = FragmentDownloadsBinding.inflate(inflater, container, false)
        return mDownloadsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        handleEvent()
    }

    private fun initData() {}

    private fun initView() {
        mDownloadsBinding.apply {
            tlDownload.apply {
                addTab(mDownloadsBinding.tlDownload.newTab().setText(R.string.txt_completed)
                    .setIcon(tabIcons[0]))
                addTab(
                    mDownloadsBinding.tlDownload.newTab().setText(R.string.txt_in_progress)
                        .setIcon(tabIcons[1]))

                getTabAt(0)!!.icon!!.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(mContext!!, R.color.color_black), PorterDuff.Mode.SRC_IN
                )
                getTabAt(1)!!.icon!!.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(mContext!!, R.color.color_text_hint), PorterDuff.Mode.SRC_IN
                )
            }

            vpDownload.apply {
                adapter = DownloadsFragmentViewPager(this@DownloadsFragment)
                isUserInputEnabled = true
                offscreenPageLimit = 2
            }

        }
    }

    private fun handleEvent() {
        mDownloadsBinding.apply {
            tlDownload.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        vpDownload.currentItem = tab.position
                        tab.icon!!.colorFilter = PorterDuffColorFilter(
                            ContextCompat.getColor(mContext!!, R.color.color_black),
                            PorterDuff.Mode.SRC_IN
                        )
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        tab.icon!!.colorFilter = PorterDuffColorFilter(
                            ContextCompat.getColor(mContext!!, R.color.color_text_hint),
                            PorterDuff.Mode.SRC_IN
                        )
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            vpDownload.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tlDownload.selectTab(tlDownload.getTabAt(position))
                }
            })
        }
    }
}