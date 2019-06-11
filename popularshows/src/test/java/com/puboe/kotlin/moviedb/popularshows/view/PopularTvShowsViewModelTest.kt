package com.puboe.kotlin.moviedb.popularshows.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import com.puboe.kotlin.moviedb.popularshows.util.LiveDataTestUtil
import com.puboe.kotlin.moviedb.popularshows.util.ViewModelScopeMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PopularTvShowsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // A CoroutineContext that can be controlled from tests
    private val testContext = TestCoroutineContext()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @get:Rule
    var coroutinesMainDispatcherRule = ViewModelScopeMainDispatcherRule(testContext)

    private lateinit var viewModel: PopularTvShowsViewModel

    @Mock
    private lateinit var repository: TvShowsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = PopularTvShowsViewModel(repository)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        val showsResponse = PopularTvShows(1, 2, listOf(show1, show2))
        `when`(repository.getPopularTvShows(1)).thenReturn(DataResult.Success(showsResponse))

        viewModel.getPopularTvShows()
        testContext.triggerActions()

        verify(repository).getPopularTvShows(1)
        assertThat(LiveDataTestUtil.getValue(viewModel.error)).isNull()
        assertThat(LiveDataTestUtil.getValue(viewModel.loading)).isFalse()
        assertThat(LiveDataTestUtil.getValue(viewModel.shows)).containsExactly(show1, show2)
    }
}