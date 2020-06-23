package com.maveric.srikanth.moviesearch.model.network

import com.maveric.srikanth.moviesearch.di.DaggerApiComponent
import com.maveric.srikanth.moviesearch.model.dto.MovieDetailResponse
import com.maveric.srikanth.moviesearch.model.dto.MovieListResponse
import io.reactivex.Single
import javax.inject.Inject

class MovieApiService {

    companion object {
        const val API_KEY = "b9bd48a6"
        const val BASE_URL = "http://www.omdbapi.com/"
        const val TYPE = "movie"
    }

    @Inject
    lateinit var api: MoviesApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getMovies(movieTitle: String, pageNumber: Int): Single<MovieListResponse> {
        return api.getMovieList(
            API_KEY, movieTitle,
            TYPE, pageNumber
        )
    }

    fun getMovieDetail(movieId: String): Single<MovieDetailResponse> {
        return api.getMovieDetail(API_KEY, movieId)
    }
}