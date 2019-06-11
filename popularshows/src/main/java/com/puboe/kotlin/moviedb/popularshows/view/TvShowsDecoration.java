package com.puboe.kotlin.moviedb.popularshows.view;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class TvShowsDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    public TvShowsDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull final Rect outRect, @NonNull final View view, @NonNull final RecyclerView parent,
                               @NonNull final RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);

        final int totalSpanCount = getTotalSpanCount(parent);
        final int spanSize = getItemSpanSize(parent, position);
        if (totalSpanCount == spanSize) {
            return;
        }

        outRect.top = isInTheFirstRow(position, totalSpanCount) ? spacing : spacing / 2;
        outRect.left = spacing;//isFirstInRow(position, totalSpanCount) ? spacing : spacing / 2;
        outRect.right = spacing;//isLastInRow(position, totalSpanCount) ? spacing : spacing / 2;
        outRect.bottom = spacing / 2;
    }

    private boolean isInTheFirstRow(int position, int spanCount) {
        return position < spanCount;
    }

    private boolean isFirstInRow(int position, int spanCount) {
        return position % spanCount == 0;
    }

    private boolean isLastInRow(int position, int spanCount) {
        return isFirstInRow(position + 1, spanCount);
    }

    private int getTotalSpanCount(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof StaggeredGridLayoutManager
                ? ((StaggeredGridLayoutManager) layoutManager).getSpanCount()
                : 1;
    }

    private int getItemSpanSize(RecyclerView parent, int position) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanSizeLookup().getSpanSize(position)
                : 1;
    }
}