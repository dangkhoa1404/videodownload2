package com.lutech.videodownloader.scenes.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFileInProgressBinding
import com.lutech.videodownloader.model.VideoInProgress
import com.lutech.videodownloader.utils.Utils

class InProgressAdapter(
    var mContext: Context,
    var listVideoInProgress: List<VideoInProgress>,
    var onItemAudioPlayListener: OnItemVideoPlayListener
) : RecyclerView.Adapter<InProgressAdapter.ViewHolder>() {

    inner class ViewHolder(val mPlayBinding: ItemFileInProgressBinding) :
        RecyclerView.ViewHolder(mPlayBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFileInProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listVideoInProgress[position]) {

                Glide.with(mContext)
                    .asBitmap()
                    .load(this.pathOfVideo)
                    .placeholder(R.drawable.ic_item_video_no_image)
                    .error(R.drawable.ic_item_video_no_image)
                    .into(mPlayBinding.imgPath)

                mPlayBinding.tvFileName.text = this.nameOfVideo

                mPlayBinding.tvMemoryOfFile.text = this.memoryOfVideo

                mPlayBinding.tvDurationOfFile.text = this.durationOfVideo

                mPlayBinding.clItemLayout.setOnClickListener {
                    if (!Utils.isClickRecently(1000)) {
                        onItemAudioPlayListener.onItemVideoClick(position)
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listVideoInProgress.size
    }

    interface OnItemVideoPlayListener {
        fun onItemVideoClick(position: Int)
    }
}