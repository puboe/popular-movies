package com.puboe.kotlin.moviedb.popularshows.network

import com.google.common.truth.Truth.assertThat
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShowsPage
import com.puboe.kotlin.moviedb.popularshows.entities.TvShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MovieDBRepositoryTest {

    @Mock
    private lateinit var networkProvider: DataProvider<Int, DataResult<PopularTvShowsPage>>

    private lateinit var repository: MovieDBRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = MovieDBRepository(networkProvider)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getPopularTvShows_shouldRequestDataFromProvider() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        val popularTvShows = PopularTvShowsPage(1, 10, listOf(show1, show2))
        `when`(networkProvider.requestData(1)).thenReturn(DataResult.Success(popularTvShows))

        val result = repository.getPopularTvShows()

        verify(networkProvider).requestData(1)
        assertThat(result is DataResult.Success).isTrue()
        result as DataResult.Success
        assertThat(result.data).hasSize(2)
        assertThat(result.data).containsExactly(show1, show2)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getPopularTvShows_withError_shouldRequestDataFromProvider() = runBlockingTest {
        `when`(networkProvider.requestData(1)).thenReturn(DataResult.Error.NetworkError)

        val result = repository.getPopularTvShows()

        verify(networkProvider).requestData(1)
        assertThat(result is DataResult.Error.NetworkError).isTrue()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getTwoPages_withEnoughPagesLeft_shouldRequestDataFromProviderTwiceAndReturnAllShows() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        val show3 = TvShow("name3", 8F, "overview3", "/poster3")
        val show4 = TvShow("name4", 7F, "overview4", "/poster4")
        val popularTvShows = PopularTvShowsPage(1, 10, listOf(show1, show2))
        val popularTvShows2 = PopularTvShowsPage(2, 10, listOf(show3, show4))
        `when`(networkProvider.requestData(1)).thenReturn(DataResult.Success(popularTvShows))
        `when`(networkProvider.requestData(2)).thenReturn(DataResult.Success(popularTvShows2))

        val firstResult = repository.getNextPage()

        verify(networkProvider).requestData(1)
        assertThat(firstResult is DataResult.Success).isTrue()
        firstResult as DataResult.Success
        assertThat(firstResult.data).hasSize(2)
        assertThat(firstResult.data).containsExactly(show1, show2)

        val secondResult = repository.getNextPage()

        verify(networkProvider).requestData(2)
        assertThat(secondResult is DataResult.Success).isTrue()
        secondResult as DataResult.Success
        assertThat(secondResult.data).hasSize(4)
        assertThat(secondResult.data).containsExactly(show1, show2, show3, show4)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getTwoPages_withNotEnoughPagesLeft_shouldRequestDataFromProviderOnce() = runBlockingTest {
        val show1 = TvShow("name1", 10F, "overview1", "/poster1")
        val show2 = TvShow("name2", 9F, "overview2", "/poster2")
        val popularTvShows = PopularTvShowsPage(1, 1, listOf(show1, show2))
        `when`(networkProvider.requestData(1)).thenReturn(DataResult.Success(popularTvShows))

        val firstResult = repository.getNextPage()

        verify(networkProvider).requestData(1)
        assertThat(firstResult is DataResult.Success).isTrue()
        firstResult as DataResult.Success
        assertThat(firstResult.data).hasSize(2)
        assertThat(firstResult.data).containsExactly(show1, show2)

        val secondResult = repository.getNextPage()

        verify(networkProvider, never()).requestData(2)
        assertThat(secondResult is DataResult.Ignore).isTrue()
    }
}