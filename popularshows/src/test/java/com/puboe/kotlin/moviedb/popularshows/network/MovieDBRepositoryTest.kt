package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.entities.TvShowsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MovieDBRepositoryTest {

    @Mock
    private lateinit var networkProvider: DataProvider<Int, DataResult<PopularTvShows>>

    private lateinit var repository: TvShowsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = MovieDBRepository(networkProvider)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getPopularTvShows_shouldRequestDataFromProvider() = runBlockingTest {
        repository.getPopularTvShows(1)

        verify(networkProvider).requestData(1)
    }
}