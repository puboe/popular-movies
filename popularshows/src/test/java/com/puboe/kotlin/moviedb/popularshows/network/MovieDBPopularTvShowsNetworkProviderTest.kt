package com.puboe.kotlin.moviedb.popularshows.network

import com.google.common.truth.Truth.assertThat
import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShow
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import com.puboe.kotlin.moviedb.popularshows.util.showCorrespondence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

class MovieDBPopularTvShowsNetworkProviderTest {

    @Mock
    private lateinit var service: TvShowsService
    private val mapper = MovieDBTvShowsMapper()
    private lateinit var networkProvider: MovieDBPopularTvShowsNetworkProvider

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        networkProvider = MovieDBPopularTvShowsNetworkProvider(mapper, service)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun requestData_shouldReturnSuccess() = runBlockingTest {
        val show1 = NetworkTvShow("name1", 10F, "overview1", "/poster1")
        val show2 = NetworkTvShow("name2", 9F, "overview2", "/poster2")
        val showsResponse = NetworkTvShows(1, 2, listOf(show1, show2))
        `when`(service.getPopularTvShows(ArgumentMatchers.anyInt())).thenReturn(showsResponse)

        val result = networkProvider.requestData(1)

        assertThat(result is DataResult.Success).isTrue()
        result as DataResult.Success
        assertThat(result.data.page).isEqualTo(1)
        assertThat(result.data.totalPages).isEqualTo(2)
        assertThat(result.data.shows).hasSize(2)
        assertThat(result.data.shows).comparingElementsUsing(showCorrespondence).containsExactly(show1, show2)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun requestData_withServerError_shouldReturnServerError() = runBlockingTest {
        val exception = HttpException(responseWithErrorCode(500))
        `when`(service.getPopularTvShows(ArgumentMatchers.anyInt())).thenThrow(exception)

        val result = networkProvider.requestData(1)

        assertThat(result is DataResult.Error.ServerError).isTrue()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun requestData_withClientError_shouldReturnClientError() = runBlockingTest {
        val exception = HttpException(responseWithErrorCode(400))
        `when`(service.getPopularTvShows(ArgumentMatchers.anyInt())).thenThrow(exception)

        val result = networkProvider.requestData(1)

        assertThat(result is DataResult.Error.ClientError).isTrue()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun requestData_withNetworkError_shouldReturnNetworkError() = runBlockingTest {
        val exception = RuntimeException()
        `when`(service.getPopularTvShows(ArgumentMatchers.anyInt())).thenThrow(exception)

        val result = networkProvider.requestData(1)

        assertThat(result is DataResult.Error.NetworkError).isTrue()
    }

    private fun responseWithErrorCode(code: Int): Response<PopularTvShows> {
        return Response.error<PopularTvShows>(code, mock(ResponseBody::class.java))
    }
}