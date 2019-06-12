package com.puboe.kotlin.moviedb.popularshows.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.puboe.kotlin.moviedb.core.entities.DataResult
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
import org.mockito.Mockito.*
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
    fun getPopularTvShows_withSuccess_shouldShowResults() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        `when`(repository.getPopularTvShows()).thenReturn(DataResult.Success(listOf(show1, show2)))

        viewModel.getPopularTvShows()
        testContext.triggerActions()

        verify(repository).getPopularTvShows()
        assertThat(LiveDataTestUtil.getValue(viewModel.error)).isNull()
        assertThat(LiveDataTestUtil.getValue(viewModel.loading)).isFalse()
        assertThat(LiveDataTestUtil.getValue(viewModel.shows)).containsExactly(show1, show2)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getPopularTvShows_withError_shouldShowError() = runBlockingTest {
        `when`(repository.getPopularTvShows()).thenReturn(DataResult.Error.NetworkError)

        viewModel.getPopularTvShows()
        testContext.triggerActions()

        verify(repository).getPopularTvShows()
        assertThat(LiveDataTestUtil.getValue(viewModel.error)).isEqualTo(DataResult.Error.NetworkError)
        assertThat(LiveDataTestUtil.getValue(viewModel.loading)).isFalse()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun requestNextPage_withSuccess_shouldShowResults() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        `when`(repository.getNextPage()).thenReturn(DataResult.Success(listOf(show1, show2)))

        viewModel.requestNextPage()
        testContext.triggerActions()

        verify(repository).getNextPage()
        assertThat(LiveDataTestUtil.getValue(viewModel.error)).isNull()
        assertThat(LiveDataTestUtil.getValue(viewModel.loading)).isFalse()
        assertThat(LiveDataTestUtil.getValue(viewModel.shows)).containsExactly(show1, show2)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun requestNextPage_withError_shouldShowError() = runBlockingTest {
        `when`(repository.getNextPage()).thenReturn(DataResult.Error.ServerError)

        viewModel.requestNextPage()
        testContext.triggerActions()

        verify(repository).getNextPage()
        assertThat(LiveDataTestUtil.getValue(viewModel.error)).isEqualTo(DataResult.Error.ServerError)
        assertThat(LiveDataTestUtil.getValue(viewModel.loading)).isFalse()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun listScrolled_overThreshold_shouldRequestPage() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        `when`(repository.getNextPage()).thenReturn(DataResult.Success(listOf(show1, show2)))

        viewModel.listScrolled(5, 15, 20)
        testContext.triggerActions()

        verify(repository).getNextPage()
        assertThat(LiveDataTestUtil.getValue(viewModel.error)).isNull()
        assertThat(LiveDataTestUtil.getValue(viewModel.loading)).isFalse()
        assertThat(LiveDataTestUtil.getValue(viewModel.shows)).containsExactly(show1, show2)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun listScrolled_underThreshold_shouldNotRequestPage() = runBlockingTest {
        viewModel.listScrolled(1, 1, 20)
        testContext.triggerActions()

        verify(repository, never()).getNextPage()
    }
}