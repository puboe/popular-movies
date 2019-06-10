package com.puboe.kotlin.moviedb.popularshows.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.puboe.kotlin.moviedb.popularshows.R
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_popular_tv_shows.*
import javax.inject.Inject

class PopularTvShowsActivity : AppCompatActivity() {

    private val adapter = TvShowsAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PopularTvShowsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PopularTvShowsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_tv_shows)

        initView()

        if (savedInstanceState == null) {
            viewModel.getPopularTvShows()
        }
    }

    private fun initView() {
        // Add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        show_list.addItemDecoration(decoration)

        initAdapter()

        viewModel.loading.observe(this, Observer { showLoading(it) })
        viewModel.error.observe(this, Observer { showError(it) })
    }

    private fun initAdapter() {
        show_list.adapter = adapter
        viewModel.shows.observe(this, Observer<List<TvShow>> {
            Log.d("Activity", "list: ${it?.size}")
            adapter.submitList(it)
        })
    }

    private fun showLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String?) {
        message?.let {
            Snackbar.make(shows_container, it, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry)) {
                    viewModel.requestNextPage()
                }.show()
        }
    }

    private fun setupScrollListener() {
        val layoutManager = show_list.layoutManager as LinearLayoutManager
        show_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

}
