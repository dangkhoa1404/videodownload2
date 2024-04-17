package com.lutech.videodownloader.scenes.playvideo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFileVideoPlayBinding
import com.lutech.videodownloader.model.Video
import com.lutech.videodownloader.scenes.playvideo.activity.PlayVideoActivity
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.setColorForImageView
import com.lutech.videodownloader.utils.setColorForTextView

class PlayVideoAdapter(
    var mContext: Context,
    var listVideoPlay: List<Video>,
    var onItemVideoPlayListener: OnItemVideoPlayListener
) : RecyclerView.Adapter<PlayVideoAdapter.ViewHolder>() {

    inner class ViewHolder(val mPlayBinding: ItemFileVideoPlayBinding) :
        RecyclerView.ViewHolder(mPlayBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFileVideoPlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listVideoPlay[position]) {

                Glide.with(mContext).asBitmap().load(this.pathOfVideo)
                    .placeholder(R.drawable.ic_item_video_no_image).error(R.drawable.ic_item_video_no_image)
                    .into(mPlayBinding.imgPath)

                mPlayBinding.tvFileName.text = this.nameOfVideo

                mPlayBinding.tvMemoryFile.text = this.memoryOfVideo

                mPlayBinding.tvDurationOfFile.text = this.durationOfVideo

                val isCurrentSoundPlaying = (mContext as PlayVideoActivity).mPosCurrentVideo == position

                mPlayBinding.tvFileName.setColorForTextView(mContext, if(isCurrentSoundPlaying) R.color.color_item_primary else R.color.color_white)

                mPlayBinding.tvMemoryFile.setColorForTextView(mContext, if(isCurrentSoundPlaying) R.color.color_item_primary else R.color.color_white)

                mPlayBinding.ivListPlayChange.setColorForImageView(mContext, if(isCurrentSoundPlaying) R.color.color_item_primary else R.color.color_white)

                mPlayBinding.vCurrentAudioPlay.isVisible = isCurrentSoundPlaying

                mPlayBinding.tvDurationOfFile.isVisible = !isCurrentSoundPlaying

                mPlayBinding.ivDelete.isVisible = listVideoPlay.size != 1

                mPlayBinding.clItemLayout.setOnClickListener {
                    if (!Utils.isClickRecently(1000)) {
                        onItemVideoPlayListener.onItemVideoPlayClick(position)
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                    }
                }

                mPlayBinding.ivDelete.setOnClickListener {
                    if (!Utils.isClickRecently(1000)) {
                        onItemVideoPlayListener.onItemDeleteClick(position)
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listVideoPlay.size
    }

    interface OnItemVideoPlayListener {
        fun onItemVideoPlayClick(position: Int)
        fun onItemDeleteClick(position: Int)
    }
}