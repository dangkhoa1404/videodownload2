package com.lutech.videodownloader.scenes.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lutech.videodownloader.scenes.home.fragment.CompleteFragment
import com.lutech.videodownloader.scenes.home.fragment.InprogressFragment

class DownloadsFragmentViewPager(fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CompleteFragment()
            else -> InprogressFragment()
        }
    }
}