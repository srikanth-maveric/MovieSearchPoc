package com.maveric.srikanth.moviesearch.model

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieApiService {

    private val apiKey = "b9bd48a6"
    private val baseUrl = "http://www.omdbapi.com/?apikey=b9bd48a6"


    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviesApi::class.java)

    fun getMovies(movieTitle: String, pageNumber: Int): Single<MovieListResponse> {
        return api.getMovieList(apiKey, movieTitle, "movie", pageNumber)
    }

    fun getMovieDetail(movieId: String): Single<MovieDetailResponse> {
        return api.getMovieDetail(apiKey, movieId)
    }
}