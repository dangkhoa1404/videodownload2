package com.lutech.videodownloader.scenes.language.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lutech.videodownloader.databinding.ItemLanguageBinding
import com.lutech.videodownloader.scenes.language.activity.LanguageActivity
import com.lutech.videodownloader.scenes.language.model.Country

class LanguageAdapter(
    var mContext: Context,
    var countries: ArrayList<Country>,
    var onItemLanguageListener: OnItemLanguageListener
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    inner class ViewHolder(val mLanguageBinding: ItemLanguageBinding) : RecyclerView.ViewHolder(mLanguageBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(countries[position]){
                mLanguageBinding.imgIconFlag.setImageResource(this.icon)

                mLanguageBinding.txtNameCountry.text = this.name

                if (mContext is LanguageActivity) {
                    mLanguageBinding.rbCheck.isChecked = (mContext as LanguageActivity).mPosCheck == position
                }
                mLanguageBinding.layoutItemLanguage.setOnClickListener {
                    onItemLanguageListener.onItemLanguageClick(position)
                }

                mLanguageBinding.rbCheck.setOnClickListener {
                    onItemLanguageListener.onItemLanguageClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    interface OnItemLanguageListener {
        fun onItemLanguageClick(position: Int)
    }
}