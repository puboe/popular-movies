package com.puboe.kotlin.moviedb.popularshows.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Decoration for list's cells.
 */
class TvShowsDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        val totalSpanCount = getTotalSpanCount(parent)
        val spanSize = getItemSpanSize(parent, position)
        if (totalSpanCount == spanSize) {
            return
        }

        outRect.top = if (isInTheFirstRow(position, totalSpanCount)) spacing else spacing / 2
        outRect.left = spacing
        outRect.right = spacing
        outRect.bottom = spacing / 2
    }

    private fun isInTheFirstRow(position: Int, spanCount: Int): Boolean {
        return position < spanCount
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? StaggeredGridLayoutManager)?.spanCount ?: 1
    }

    private fun getItemSpanSize(parent: RecyclerView, position: Int): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? GridLayoutManager)?.spanSizeLookup?.getSpanSize(position) ?: 1
    }
}