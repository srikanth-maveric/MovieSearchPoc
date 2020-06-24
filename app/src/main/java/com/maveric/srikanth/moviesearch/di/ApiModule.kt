package com.maveric.srikanth.moviesearch.di

import com.maveric.srikanth.moviesearch.model.network.MovieApiService
import com.maveric.srikanth.moviesearch.model.network.MoviesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    @Provides
    fun provideMoviesApi(): MoviesApi {
        return Retrofit.Builder()
            .baseUrl(MovieApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MoviesApi::class.java)
    }

    @Provides
    open fun provideMovieApiService(): MovieApiService{
        return MovieApiService()
    }

}