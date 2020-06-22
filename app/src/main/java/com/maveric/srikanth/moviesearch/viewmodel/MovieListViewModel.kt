package com.maveric.srikanth.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maveric.srikanth.moviesearch.model.MovieApiService
import com.maveric.srikanth.moviesearch.model.MovieListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MovieListViewModel : ViewModel() {
    private val movieApiService = MovieApiService()
    private val compositeDisposable = CompositeDisposable()

    val movieListResponse = MutableLiveData<MovieListResponse>()
    val movieListLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun fetchMovieList(movieTitle: String, pageNumber: Int) {
        loading.value = true

        compositeDisposable.add(
            movieApiService.getMovies(movieTitle, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieListResponse>() {
                    override fun onSuccess(movieListResponse: MovieListResponse) {
                        this@MovieListViewModel.movieListResponse.value = movieListResponse
                        movieListLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        movieListLoadError.value = true
                        loading.value =false
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