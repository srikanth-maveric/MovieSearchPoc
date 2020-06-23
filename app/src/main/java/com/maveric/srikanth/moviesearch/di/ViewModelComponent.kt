package com.maveric.srikanth.moviesearch.di

import com.maveric.srikanth.moviesearch.viewmodel.MovieDetailViewModel
import com.maveric.srikanth.moviesearch.viewmodel.MovieListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ViewModelComponent {

    fun inject(movieListViewModel: MovieListViewModel)
    fun inject(movieDetailViewModel: MovieDetailViewModel)
}