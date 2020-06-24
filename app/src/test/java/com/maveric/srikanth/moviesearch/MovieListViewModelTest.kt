package com.maveric.srikanth.moviesearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.maveric.srikanth.moviesearch.di.DaggerViewModelComponent
import com.maveric.srikanth.moviesearch.model.dto.MovieListResponse
import com.maveric.srikanth.moviesearch.model.dto.Movies
import com.maveric.srikanth.moviesearch.model.network.MovieApiService
import com.maveric.srikanth.moviesearch.viewmodel.MovieListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class MovieListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var movieApiService: MovieApiService

    var movieListViewModel = MovieListViewModel(true)

    private val testMovieTitle = "Movie Title"
    private val testPageNumber = 1

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent.builder()
            .apiModule(ApiModuleTest(movieApiService))
            .build()
            .inject(movieListViewModel)
    }

    @Test
    fun fetchMovieListSuccess() {
        val movieItem = Movies("Test", 1, "test", "test", "test")
        val movieList = listOf(movieItem)
        val movieListResponse = MovieListResponse(movieList, 1, true)
        val testSingle = Single.just(movieListResponse)
        Mockito.`when`(movieApiService.getMovies(testMovieTitle, testPageNumber))
            .thenReturn(testSingle)

        movieListViewModel.fetchMovieList(testMovieTitle, testPageNumber)
        val actualValue = movieListViewModel.movieListResponse.value?.movieList?.size

        actualValue?.let {
            Assert.assertEquals(1, it)
        }
    }

    @Test
    fun fetchMovieListFailure(){
        val testSingle = Single.error<MovieListResponse>(Throwable())
        Mockito.`when`(movieApiService.getMovies(testMovieTitle, testPageNumber))
            .thenReturn(testSingle)

        movieListViewModel.fetchMovieList(testMovieTitle, testPageNumber)

        Assert.assertNull(movieListViewModel.movieListResponse.value)
    }
}