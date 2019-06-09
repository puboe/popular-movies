package com.puboe.kotlin.moviedb.popularshows.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.puboe.kotlin.moviedb.popularshows.BuildConfig
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TvShowsService {

    @GET("tv/popular")
    fun getPopularTvShows(@Query("page") page: Int): Deferred<NetworkTvShows>

    companion object {

        fun create(): TvShowsService {
            return provideRetrofit().create(TvShowsService::class.java)
        }

        private fun provideApiKeyInterceptor() = Interceptor { chain ->
            val newRequest = chain.request().let { request ->
                val newUrl = request.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                    .build()
                request.newBuilder()
                    .url(newUrl)
                    .build()
            }
            chain.proceed(newRequest)
        }

        private fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        private fun provideHttpClient() = OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(provideApiKeyInterceptor())
            .build()

        private fun provideRetrofit() = Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIEDB_BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}