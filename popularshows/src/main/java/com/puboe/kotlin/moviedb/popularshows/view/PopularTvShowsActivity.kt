package com.puboe.kotlin.moviedb.popularshows.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.puboe.kotlin.moviedb.popularshows.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class PopularTvShowsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PopularTvShowsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PopularTvShowsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_tv_shows)
    }
}
