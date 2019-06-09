package com.puboe.kotlin.moviedb.popularshows.network

import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TvShowsService {

    @GET("/tv/popular")
    fun getPopularTvShows(@Query("page") page: Int, @Query("api_key") apiKey: String): Deferred<NetworkTvShows>

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): TvShowsService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TvShowsService::class.java)
        }
    }
}