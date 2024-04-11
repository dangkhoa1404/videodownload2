package com.lutech.videodownloader.scenes.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFileInDeviceBinding
import com.lutech.videodownloader.databinding.ItemFileInDeviceType2Binding
import com.lutech.videodownloader.model.Video

class VideoAdapter(
    var mContext: Context,
    private val isTypeView1 : Int,
    var listVideos: List<Video>,
    var onItemVideoListener: OnItemVideoListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LIST_VIDEO_TYPE_1 = 1

    private val LIST_VIDEO_TYPE_2 = 2

    override fun getItemViewType(position: Int): Int {
        return if (isTypeView1 == 1) {
            LIST_VIDEO_TYPE_1
        } else {
            LIST_VIDEO_TYPE_2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LIST_VIDEO_TYPE_1 -> VideoViewType1ViewHolder(parent)
            LIST_VIDEO_TYPE_2 -> VideoViewType2ViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewType1ViewHolder -> {
                with(holder) {
                    with(listVideos[position]) {

                        Glide.with(mContext).asBitmap().load(this.pathOfVideo).error(R.drawable.ic_item_video_no_image).into(viewType1.imgPath)

                        viewType1.tvFileName.text = this.nameOfVideo

                        viewType1.tvParentFile.text = this.parentoOfVideo

                        viewType1.tvMemoryOfFile.text = this.memoryOfVideo

                        viewType1.tvDayAdded.text = this.dateAddedVideo

                        viewType1.tvDurationOfFile.text = this.durationOfVideo

                        viewType1.layoutItemVideo.setOnClickListener {
                            onItemVideoListener.onItemVideoClick(position)
                        }

                        viewType1.ivOption.setOnClickListener {
                            onItemVideoListener.onItemPosClick(position)
                        }
                    }
                }
            }
            is VideoViewType2ViewHolder -> {
                with(holder) {
                    with(listVideos[position]) {

                        Glide.with(mContext).asBitmap().load(this.pathOfVideo).error(R.drawable.ic_item_video_no_image).into(viewType2.imgPath)

                        viewType2.tvFileName.text = this.nameOfVideo

                        viewType2.tvParentFile.text = this.parentoOfVideo

                        viewType2.tvMemoryOfFile.text = this.memoryOfVideo

                        viewType2.tvDayAdded.text = this.dateAddedVideo

                        viewType2.tvDurationOfFile.text = this.durationOfVideo

                        viewType2.layoutItemVideo.setOnClickListener {
                            onItemVideoListener.onItemVideoClick(position)
                        }

                        viewType2.ivOption.setOnClickListener {
                            onItemVideoListener.onItemPosClick(position)
                        }
                    }
                }
            }
        }
    }

    inner class VideoViewType1ViewHolder private constructor(
        val viewType1: ItemFileInDeviceBinding
    ) : RecyclerView.ViewHolder(viewType1.root) {

        constructor(parent: ViewGroup) : this(
            ItemFileInDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class VideoViewType2ViewHolder private constructor(
        val viewType2: ItemFileInDeviceType2Binding
    ) : RecyclerView.ViewHolder(viewType2.root) {

        constructor(parent: ViewGroup) : this(
            ItemFileInDeviceType2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listVideos.size
    }

    interface OnItemVideoListener {
        fun onItemVideoClick(position: Int)
        fun onItemPosClick(position: Int)
    }
}