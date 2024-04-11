package com.lutech.videodownloader.scenes.intro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.videodownloader.databinding.ItemIntroBinding
import com.lutech.videodownloader.model.Intro

class IntroSliderAdapter(
    val mContext: Context,
    private val introSlides: List<Intro>,
    var onItemIntroListener: OnItemIntroListener
) :
    RecyclerView.Adapter<IntroSliderAdapter.ViewHolder>() {

    inner class ViewHolder(val mIntroBinding: ItemIntroBinding) : RecyclerView.ViewHolder(mIntroBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(introSlides[position]){

                Glide.with(mContext).asBitmap().load(this.image).into(mIntroBinding.imgIntro)

                mIntroBinding.tvTitleIntro.setText(this.title)

                mIntroBinding.tvDesIntro.setText(this.description)

                mIntroBinding.tvSkipIntro.setOnClickListener {
                    onItemIntroListener.onItemIntroClick()
                }

            }
        }
    }

    override fun getItemCount(): Int = introSlides.size

    interface OnItemIntroListener {
        fun onItemIntroClick()
    }

}