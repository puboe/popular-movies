package com.puboe.kotlin.moviedb.popularshows.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.puboe.kotlin.moviedb.popularshows.TestApplication

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}