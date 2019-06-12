package com.puboe.kotlin.moviedb.popularshows.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.puboe.kotlin.moviedb.popularshows.BuildConfig
import com.puboe.kotlin.moviedb.popularshows.R
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow

class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val showName: TextView = itemView.findViewById(R.id.show_name)
    private val showOverview: TextView = itemView.findViewById(R.id.show_overview)
    private val showRating: TextView = itemView.findViewById(R.id.show_rating)
    private val showPoster: ImageView = itemView.findViewById(R.id.show_poster)

    fun bind(tvShow: TvShow) {
        showName.text = tvShow.name
        showOverview.text = tvShow.overview
        showRating.text = tvShow.rating.toString()

        Glide.with(itemView)
            .load(Uri.parse(String.format(BuildConfig.MOVIEDB_IMAGE_BASE_URL, DEFAULT_IMAGE_SIZE, tvShow.poster)))
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
            .placeholder(ColorDrawable(Color.LTGRAY))
            .centerCrop()
            .into(showPoster)
    }

    companion object {
        fun create(parent: ViewGroup): TvShowViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tvshow_item_view, parent, false)
            return TvShowViewHolder(view)
        }

        const val DEFAULT_IMAGE_SIZE = "w185"
        const val CROSSFADE_DURATION = 150
    }
}