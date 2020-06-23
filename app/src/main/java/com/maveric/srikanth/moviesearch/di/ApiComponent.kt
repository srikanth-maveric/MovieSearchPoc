package com.maveric.srikanth.moviesearch.di

import com.maveric.srikanth.moviesearch.model.network.MovieApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: MovieApiService)
}