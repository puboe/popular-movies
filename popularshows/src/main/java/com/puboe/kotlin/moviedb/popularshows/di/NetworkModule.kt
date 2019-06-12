package com.puboe.kotlin.moviedb.popularshows.di

import com.puboe.kotlin.moviedb.core.entities.DataResult
import com.puboe.kotlin.moviedb.core.provider.DataMapper
import com.puboe.kotlin.moviedb.core.provider.DataProvider
import com.puboe.kotlin.moviedb.popularshows.BuildConfig
import com.puboe.kotlin.moviedb.popularshows.entities.PopularTvShows
import com.puboe.kotlin.moviedb.popularshows.network.MovieDBPopularTvShowsNetworkProvider
import com.puboe.kotlin.moviedb.popularshows.network.MovieDBTvShowsMapper
import com.puboe.kotlin.moviedb.popularshows.network.TvShowsService
import com.puboe.kotlin.moviedb.popularshows.network.response.NetworkTvShows
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideApiKeyInterceptor(): Interceptor =
        Interceptor { chain ->
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

    @Provides
    @Singleton
    @JvmStatic
    internal fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @JvmStatic
    internal fun providesHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @JvmStatic
    internal fun providesRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIEDB_API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @JvmStatic
    internal fun providesPopularTvShowsService(retrofit: Retrofit): TvShowsService =
        retrofit.create(TvShowsService::class.java)

    @Provides
    @Singleton
    @JvmStatic
    internal fun providesDataMapper(): DataMapper<NetworkTvShows, PopularTvShows> = MovieDBTvShowsMapper()

    @Provides
    @Singleton
    @JvmStatic
    internal fun providesDataProvider(
        mapper: @JvmSuppressWildcards DataMapper<NetworkTvShows, PopularTvShows>,
        service: TvShowsService
    ): DataProvider<Int, DataResult<PopularTvShows>> {
        return MovieDBPopularTvShowsNetworkProvider(mapper, service)
    }
}