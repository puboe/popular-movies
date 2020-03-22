package com.puboe.kotlin.moviedb.popularshows.view

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow

class TvShowsAdapter : PagedListAdapter<TvShow, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TvShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvShow = getItem(position)
        if (tvShow != null) {
            (holder as TvShowViewHolder).bind(tvShow)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean =
                oldItem == newItem
        }
    }
}