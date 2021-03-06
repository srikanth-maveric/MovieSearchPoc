package com.maveric.srikanth.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maveric.srikanth.moviesearch.di.DaggerViewModelComponent
import com.maveric.srikanth.moviesearch.model.dto.MovieListResponse
import com.maveric.srikanth.moviesearch.model.network.MovieApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieListViewModel(private val isFromUnitTest: Boolean = false) : ViewModel() {

    @Inject
    lateinit var movieApiService: MovieApiService

    private val compositeDisposable = CompositeDisposable()
    val movieListResponse = MutableLiveData<MovieListResponse>()
    val movieListLoadError = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        if (!isFromUnitTest) {
            DaggerViewModelComponent.create().inject(this)
        }
    }

    fun fetchMovieList(movieTitle: String, pageNumber: Int) {
        isLoading.value = true

        compositeDisposable.add(
            movieApiService.getMovies(movieTitle, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieListResponse>() {
                    override fun onSuccess(movieListResponse: MovieListResponse) {
                        this@MovieListViewModel.movieListResponse.value = movieListResponse
                        movieListLoadError.value = false
                        isLoading.value = false
                        if (movieListResponse.movieList.isNullOrEmpty()) {
                            movieListLoadError.value = true
                        }
                    }

                    override fun onError(e: Throwable) {
                        movieListLoadError.value = true
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