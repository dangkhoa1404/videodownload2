package com.lutech.videodownloader.scenes.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFileInDeviceBinding
import com.lutech.videodownloader.databinding.ItemFileInDeviceType2Binding
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.utils.gone

class AudioAdapter(
    var mContext: Context,
    val isTypeViewMusic1 : Int,
    var listMusic: List<Audio>,
    var onItemVideoListener: OnItemMusicListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LIST_MUSIC_TYPE_1 = 1

    private val LIST_MUSIC_TYPE_2 = 2

    override fun getItemViewType(position: Int): Int {
        return if (isTypeViewMusic1 == 1) {
            LIST_MUSIC_TYPE_1
        } else {
            LIST_MUSIC_TYPE_2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LIST_MUSIC_TYPE_1 -> MusicViewType1ViewHolder(parent)
            LIST_MUSIC_TYPE_2 -> MusicViewType2ViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MusicViewType1ViewHolder -> {
                with(holder) {
                    with(listMusic[position]) {
                        gone(viewType1.imgPlayFile)

                        Glide.with(mContext).load(R.drawable.ic_item_music).into(viewType1.imgPath)

                        viewType1.tvFileName.text = this.nameOfAudio

                        viewType1.tvParentFile.text = this.parentOfAudio

                        viewType1.tvMemoryOfFile.text = this.memoryOfAudio

                        viewType1.tvDayAdded.text = this.dateAddedAudio

                        viewType1.tvDurationOfFile.text = this.durationOfAudio

                        viewType1.layoutItemVideo.setOnClickListener {
                            onItemVideoListener.onItemMusicClick(position)
                        }

                        viewType1.ivOption.setOnClickListener {
                            onItemVideoListener.onItemPosClick(position)
                        }
                    }
                }
            }
            is MusicViewType2ViewHolder -> {
                with(holder) {
                    with(listMusic[position]) {
                        gone(viewType2.imgPlayFile)

                        Glide.with(mContext).load(R.drawable.ic_item_music).into(viewType2.imgPath)

                        viewType2.tvFileName.text = this.nameOfAudio

                        viewType2.tvParentFile.text = this.parentOfAudio

                        viewType2.tvMemoryOfFile.text = this.memoryOfAudio
                        viewType2.tvDayAdded.text = this.dateAddedAudio

                        viewType2.tvDurationOfFile.text = this.durationOfAudio

                        viewType2.layoutItemVideo.setOnClickListener {
                            onItemVideoListener.onItemMusicClick(position)
                        }

                        viewType2.ivOption.setOnClickListener {
                            onItemVideoListener.onItemPosClick(position)
                        }
                    }
                }
            }
        }
    }

    inner class MusicViewType1ViewHolder private constructor(
        val viewType1: ItemFileInDeviceBinding
    ) : RecyclerView.ViewHolder(viewType1.root) {

        constructor(parent: ViewGroup) : this(
            ItemFileInDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class MusicViewType2ViewHolder private constructor(
        val viewType2: ItemFileInDeviceType2Binding
    ) : RecyclerView.ViewHolder(viewType2.root) {

        constructor(parent: ViewGroup) : this(
            ItemFileInDeviceType2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    interface OnItemMusicListener {
        fun onItemMusicClick(position: Int)
        fun onItemPosClick(position: Int)
    }
}