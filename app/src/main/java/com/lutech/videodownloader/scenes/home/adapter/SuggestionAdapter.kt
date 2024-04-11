package com.lutech.videodownloader.scenes.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lutech.videodownloader.R
import com.lutech.videodownloader.webservice.Result

class SuggestionAdapter (var suggestionListener: SuggestionListener?) :
    RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    private var resultList: List<Result>? = ArrayList()

    fun resultList(resultList: List<Result>?) {
        this.resultList = resultList
        if (this.resultList == null) {
            this.resultList = ArrayList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestionAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_suggestion, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuggestionAdapter.ViewHolder, position: Int) {
        val result: Result = resultList!![position]
        if (result.key != null) {
            holder.txtTitle.text = result.key
            val strSubTitle: String = "Search for \"" + result.key + "\""
            holder.txtSubTitle.text = strSubTitle
            holder.itemView.setOnClickListener {
                if (suggestionListener != null) {
                    suggestionListener!!.onSuggestion(result.key)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return resultList!!.size
    }

    inner class ViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView
        var txtSubTitle: TextView

        init {
            txtTitle = itemView.findViewById(R.id.tvTitleSearch)
            txtSubTitle = itemView.findViewById(R.id.tvDesTitleSearch)
        }
    }

    interface SuggestionListener {
        fun onSuggestion(str: String?)
    }
}