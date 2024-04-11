package com.lutech.videodownloader.scenes.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.FragmentInprogressBinding

class InprogressFragment : Fragment() {

    private lateinit var mInProgressBinding: FragmentInprogressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_inprogress, container, false)
    }
}