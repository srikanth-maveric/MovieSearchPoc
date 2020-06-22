package com.maveric.srikanth.moviesearch.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieApiService {

    private companion object {
        const val API_KEY = "b9bd48a6"
        const val BASE_URL = "http://www.omdbapi.com/"
        const val TYPE = "movie"
    }


    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviesApi::class.java)

    fun getMovies(movieTitle: String, pageNumber: Int): Single<MovieListResponse> {
        return api.getMovieList(API_KEY, movieTitle, TYPE, pageNumber)
    }

    fun getMovieDetail(movieId: String): Single<MovieDetailResponse> {
        return api.getMovieDetail(API_KEY, movieId)
    }
}