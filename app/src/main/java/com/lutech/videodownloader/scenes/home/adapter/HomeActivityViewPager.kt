package com.lutech.videodownloader.scenes.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lutech.videodownloader.scenes.home.fragment.DownloadsFragment
import com.lutech.videodownloader.scenes.home.fragment.PlayerFragment
import com.lutech.videodownloader.scenes.home.fragment.DownloadFragment

class HomeActivityViewPager(fragmentActivity: FragmentActivity) :FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DownloadFragment()
            1 -> DownloadFragment()
            2 -> DownloadsFragment()
            else -> PlayerFragment()
        }
    }

}