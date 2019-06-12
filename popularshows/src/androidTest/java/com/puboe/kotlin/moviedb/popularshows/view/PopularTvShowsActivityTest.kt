package com.puboe.kotlin.moviedb.popularshows.view

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.puboe.kotlin.moviedb.popularshows.R
import com.puboe.kotlin.moviedb.popularshows.TestApplication
import com.puboe.kotlin.moviedb.popularshows.mocks.components.DaggerTestApplicationComponent
import com.puboe.kotlin.moviedb.popularshows.mocks.modules.EntitiesTestModule
import com.puboe.kotlin.moviedb.popularshows.mocks.network.MovieDBFakeRepository
import com.puboe.kotlin.moviedb.popularshows.util.withRecyclerView
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PopularTvShowsActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(PopularTvShowsActivity::class.java, true, false)

    private val repository = MovieDBFakeRepository()

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as TestApplication

        DaggerTestApplicationComponent
            .builder()
            .entitiesModule(EntitiesTestModule(repository))
            .create(app)
            .inject(app)
    }

    @After
    fun tearDown() {
        repository.reset()
    }

    @Test
    fun getPopularTvShows_withNetworkError_shouldShowError() {
        repository.setReturnError("network")

        activityRule.launchActivity(Intent())

        onView(withText(R.string.network_error)).check(matches(isDisplayed()))
        onView(withText(R.string.retry)).check(matches(isDisplayed()))
    }

    @Test
    fun getPopularTvShows_withServerError_shouldShowError() {
        repository.setReturnError("server")

        activityRule.launchActivity(Intent())

        onView(withText(R.string.server_error)).check(matches(isDisplayed()))
        onView(withText(R.string.retry)).check(matches(isDisplayed()))
    }

    @Test
    fun getPopularTvShows_withClientError_shouldShowError() {
        repository.setReturnError("client")

        activityRule.launchActivity(Intent())

        onView(withText(R.string.client_error)).check(matches(isDisplayed()))
        onView(withText(R.string.retry)).check(doesNotExist())
    }

    @Test
    fun getPopularTvShows_shouldShowCorrectItems() {
        activityRule.launchActivity(Intent())

        checkNameOnPosition(0, "24 season 0")
        checkNameOnPosition(2, "24 season 2")
    }

    @Test
    fun getPopularTvShows_withErrorAndRetry_shouldShowCorrectItems() {
        repository.setReturnError("network")

        activityRule.launchActivity(Intent())

        onView(withText(R.string.retry))
            .check(matches(isDisplayed()))
            .perform(click())

        checkNameOnPosition(0, "24 season 0")
        checkNameOnPosition(2, "24 season 2")
    }

    @Test
    fun getPopularTvShows_afterScroll_shouldShowCorrectItems() {
        activityRule.launchActivity(Intent())

        onView(withId(R.id.show_list))
            .perform(scrollToPosition<TvShowViewHolder>(8))

        checkNameOnPosition(8, "24 season 8")
    }

    @Test
    fun requestNextPage_afterPagination_shouldShowCorrectItems() {
        activityRule.launchActivity(Intent())

        // Trigger pagination.
        onView(withId(R.id.show_list))
            .perform(scrollToPosition<TvShowViewHolder>(18))

        checkNameOnPosition(18, "24 season 18")

        // Scroll to a position on the next page.
        onView(withId(R.id.show_list))
            .perform(scrollToPosition<TvShowViewHolder>(25))

        checkNameOnPosition(25, "24 season 25")
    }

    private fun checkNameOnPosition(position: Int, expectedName: String) {
        onView(withRecyclerView(R.id.show_list).atPositionOnView(position, R.id.show_name))
            .check(matches(withText(expectedName)))
    }
}