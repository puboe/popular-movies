package com.puboe.kotlin.moviedb.popularshows.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.puboe.kotlin.moviedb.popularshows.R
import dagger.android.AndroidInjection

class PopularTvShowsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_tv_shows)
    }
}
