package com.maveric.srikanth.moviesearch.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("Search") val movieList: List<Movies>?,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("Response") val response: Boolean
)

data class Movies(
    @SerializedName("Title") val title : String,
    @SerializedName("Year") val year : Int,
    @SerializedName("imdbID") val imdbID : String,
    @SerializedName("Type") val type : String,
    @SerializedName("Poster") val poster : String
)