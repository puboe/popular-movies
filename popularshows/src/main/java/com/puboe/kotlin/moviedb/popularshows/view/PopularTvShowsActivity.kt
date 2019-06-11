package com.puboe.kotlin.moviedb.popularshows.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.R
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_popular_tv_shows.*
import javax.inject.Inject

class PopularTvShowsActivity : AppCompatActivity() {

    private val adapter = TvShowsAdapter()
    private var errorSnackbar: Snackbar? = null

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.popular_tv_shows_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.refresh -> {
                show_list.scrollToPosition(0)
                viewModel.getPopularTvShows()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        show_list.addItemDecoration(TvShowsDecoration(resources.getDimensionPixelSize(R.dimen.decoration_spacing)))

        (show_list.layoutManager as StaggeredGridLayoutManager).gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_NONE

        initAdapter()
        setupScrollListener()

        viewModel.loading.observe(this, Observer { showLoading(it) })
        viewModel.error.observe(this, Observer { showError(it) })
    }

    private fun initAdapter() {
        show_list.adapter = adapter
        viewModel.shows.observe(this, Observer<List<TvShow>> {
            Log.d("Activity", "list: ${it.size}")
            adapter.submitList(ArrayList(it))  // submitList needs to receive a different list each time.
        })
    }

    private fun showLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(error: DataResult.Error?) {
        if (error == null) {
            errorSnackbar?.dismiss()
            return
        }
        errorSnackbar = when (error) {
            !is DataResult.Error.ClientError -> {
                Snackbar.make(shows_container, getErrorMessage(error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry)) {
                        viewModel.requestNextPage()
                    }
            }
            else -> Snackbar.make(shows_container, getErrorMessage(error), Snackbar.LENGTH_LONG)
        }
        errorSnackbar?.show()
    }

    private fun getErrorMessage(error: DataResult.Error): String {
        return getString(
            when (error) {
                is DataResult.Error.ServerError -> R.string.server_error
                is DataResult.Error.NetworkError -> R.string.network_error
                is DataResult.Error.ClientError -> R.string.client_error
            }
        )
    }

    private fun setupScrollListener() {
        val layoutManager = show_list.layoutManager as StaggeredGridLayoutManager
        show_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPositions(null)

                viewModel.listScrolled(visibleItemCount, lastVisibleItem[0], totalItemCount)
            }
        })
    }
}
