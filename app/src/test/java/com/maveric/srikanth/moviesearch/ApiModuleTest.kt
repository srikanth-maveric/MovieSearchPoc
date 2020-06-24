package com.maveric.srikanth.moviesearch

import com.maveric.srikanth.moviesearch.di.ApiModule
import com.maveric.srikanth.moviesearch.model.network.MovieApiService

class ApiModuleTest(private val mockService: MovieApiService): ApiModule() {

    override fun provideMovieApiService(): MovieApiService {
        return mockService
    }
}