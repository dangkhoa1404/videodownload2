package com.lutech.videodownloader.scenes.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFileInDeviceBinding
import com.lutech.videodownloader.databinding.ItemFolderBinding
import com.lutech.videodownloader.model.Folder

class FolderAdapter(
    var mContext: Context,
    var listDownloadedVideo: List<Folder>,
    var onItemFolderListener: OnItemFolderListener
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    var mPosCheckCurrentFolder = 0

    inner class ViewHolder(val mFolderBinding: ItemFolderBinding) :
        RecyclerView.ViewHolder(mFolderBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listDownloadedVideo[position]) {

                mFolderBinding.layoutItemFolder.isSelected = mPosCheckCurrentFolder == position

                mFolderBinding.tvFolderName.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        if(mPosCheckCurrentFolder == position) R.color.color_white else R.color.color_black
                    )
                )

                mFolderBinding.tvFolderName.text = this.folderName

                mFolderBinding.layoutItemFolder.setOnClickListener {
                    onItemFolderListener.onItemFolderClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listDownloadedVideo.size
    }

    interface OnItemFolderListener {
        fun onItemFolderClick(position: Int)
    }
}