package com.maveric.srikanth.moviesearch.model.network

import com.maveric.srikanth.moviesearch.model.dto.MovieDetailResponse
import com.maveric.srikanth.moviesearch.model.dto.MovieListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("/")
    fun getMovieList(
        @Query("apikey") apikey: String,
        @Query("s") s: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Single<MovieListResponse>


    @GET("/")
    fun getMovieDetail(
        @Query("apikey") apikey: String,
        @Query("i") id: String
    ): Single<MovieDetailResponse>
}