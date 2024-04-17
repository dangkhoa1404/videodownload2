package com.lutech.videodownloader.scenes.playaudio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.R
import com.lutech.videodownloader.databinding.ItemFilePlayBinding
import com.lutech.videodownloader.model.Audio
import com.lutech.videodownloader.scenes.playaudio.activity.PlayAudioActivity
import com.lutech.videodownloader.utils.Utils
import com.lutech.videodownloader.utils.setColorForImageView
import com.lutech.videodownloader.utils.setColorForTextView

class PlayAudioAdapter(
    var mContext: Context,
    var listAudioPlay: List<Audio>,
    var onItemAudioPlayListener: OnItemAudioPlayListener
) : RecyclerView.Adapter<PlayAudioAdapter.ViewHolder>() {

//    var mPosCurrentPlayAudio = 0

    inner class ViewHolder(val mPlayBinding: ItemFilePlayBinding) :
        RecyclerView.ViewHolder(mPlayBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFilePlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listAudioPlay[position]) {

                Glide.with(mContext).asBitmap().load(this.pathOfAudio)
                    .placeholder(R.drawable.ic_item_music).error(R.drawable.ic_item_music)
                    .into(mPlayBinding.imgPath)

                mPlayBinding.tvFileName.text = this.nameOfAudio

                mPlayBinding.tvMemoryFile.text = this.memoryOfAudio

                mPlayBinding.tvDurationOfFile.text = this.durationOfAudio

                val isCurrentSoundPlaying = (mContext as PlayAudioActivity).mPosOfPathAudio == position

                mPlayBinding.tvFileName.setColorForTextView(mContext, if(isCurrentSoundPlaying) R.color.color_item_primary else R.color.color_black)

                mPlayBinding.tvMemoryFile.setColorForTextView(mContext, if(isCurrentSoundPlaying) R.color.color_item_primary else R.color.color_black)

                mPlayBinding.ivListPlayChange.setColorForImageView(mContext, if(isCurrentSoundPlaying) R.color.color_item_primary else R.color.color_black)

                mPlayBinding.vCurrentAudioPlay.isVisible = isCurrentSoundPlaying

                mPlayBinding.tvDurationOfFile.isVisible = !isCurrentSoundPlaying

                mPlayBinding.ivDelete.isVisible = listAudioPlay.size != 1

                mPlayBinding.clItemLayout.setOnClickListener {
                    if (!Utils.isClickRecently(1000)) {
                        onItemAudioPlayListener.onItemAudioPlayClick(position)
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                    }
                }

                mPlayBinding.ivDelete.setOnClickListener {
                    if (!Utils.isClickRecently(1000)) {
                        onItemAudioPlayListener.onItemDeleteClick(position)
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.txt_please_wait), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listAudioPlay.size
    }

    interface OnItemAudioPlayListener {
        fun onItemAudioPlayClick(position: Int)
        fun onItemDeleteClick(position: Int)
    }
}