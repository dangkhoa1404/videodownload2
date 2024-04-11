package com.lutech.videodownloader.scenes.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFileInDeviceBinding
import com.lutech.videodownloader.model.Video

class CompleteAdapter(
    var mContext: Context,
    var listDownloadedVideo: List<Video>,
    var onItemCompleteDownloadedListener: OnItemCompleteDownloadedListener
) : RecyclerView.Adapter<CompleteAdapter.ViewHolder>() {

    inner class ViewHolder(val mCompleteBinding: ItemFileInDeviceBinding) :
        RecyclerView.ViewHolder(mCompleteBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFileInDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listDownloadedVideo[position]) {

                Glide.with(mContext).asBitmap().load(this.pathOfVideo).error(R.drawable.ic_item_video_no_image).into(mCompleteBinding.imgPath)

                mCompleteBinding.tvFileName.text = this.nameOfVideo

                mCompleteBinding.tvParentFile.text = this.parentoOfVideo

                mCompleteBinding.tvMemoryOfFile.text = this.memoryOfVideo

                mCompleteBinding.tvDayAdded.text = this.dateAddedVideo

                mCompleteBinding.tvDurationOfFile.text = this.durationOfVideo

                mCompleteBinding.layoutItemVideo.setOnClickListener {
                    onItemCompleteDownloadedListener.onItemCompleteClick(position)
                }

                mCompleteBinding.ivOption.setOnClickListener {
                    onItemCompleteDownloadedListener.onItemPosClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listDownloadedVideo.size
    }

    interface OnItemCompleteDownloadedListener {
        fun onItemCompleteClick(position: Int)
        fun onItemPosClick(position: Int)
    }
}