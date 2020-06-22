package com.maveric.srikanth.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maveric.srikanth.moviesearch.model.network.MovieApiService
import com.maveric.srikanth.moviesearch.model.dto.MovieDetailResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MovieDetailViewModel : ViewModel() {

    private val movieApiService =
        MovieApiService()
    private val compositeDisposable = CompositeDisposable()

    val movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetailLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun fetchMovieDetail(movieId: String) {

        loading.value = true

        compositeDisposable.add(
            movieApiService.getMovieDetail(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieDetailResponse>() {
                    override fun onSuccess(movieListResponse: MovieDetailResponse) {
                        movieDetail.value = movieListResponse
                        movieDetailLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        movieDetailLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )


    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}