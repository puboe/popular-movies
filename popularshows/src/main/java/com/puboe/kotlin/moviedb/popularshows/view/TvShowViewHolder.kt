package com.puboe.kotlin.moviedb.popularshows.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.puboe.kotlin.moviedb.popularshows.R
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow

class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val showName: TextView = itemView.findViewById(R.id.show_name)

    fun bind(tvShow: TvShow) {
        showName.text = tvShow.name
    }

    companion object {
        fun create(parent: ViewGroup): TvShowViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tvshow_item_view, parent, false)
            return TvShowViewHolder(view)
        }
    }
}