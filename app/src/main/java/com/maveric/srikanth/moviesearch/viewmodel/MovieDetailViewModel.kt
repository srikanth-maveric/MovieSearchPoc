package com.maveric.srikanth.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maveric.srikanth.moviesearch.di.DaggerViewModelComponent
import com.maveric.srikanth.moviesearch.model.dto.MovieDetailResponse
import com.maveric.srikanth.moviesearch.model.network.MovieApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailViewModel : ViewModel() {

    @Inject
    lateinit var movieApiService: MovieApiService

    private val compositeDisposable = CompositeDisposable()
    val movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetailLoadError = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        DaggerViewModelComponent.create().inject(this)
    }

    fun fetchMovieDetail(movieId: String) {
        isLoading.value = true

        compositeDisposable.add(
            movieApiService.getMovieDetail(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieDetailResponse>() {
                    override fun onSuccess(movieListResponse: MovieDetailResponse) {
                        movieDetail.value = movieListResponse
                        movieDetailLoadError.value = false
                        isLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        movieDetailLoadError.value = true
                        isLoading.value = false
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