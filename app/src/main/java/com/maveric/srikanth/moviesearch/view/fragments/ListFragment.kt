package com.maveric.srikanth.moviesearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.maveric.srikanth.moviesearch.R
import com.maveric.srikanth.moviesearch.util.GridItemDecoration
import com.maveric.srikanth.moviesearch.util.PaginationScrollListener
import com.maveric.srikanth.moviesearch.view.adapter.MovieListAdapter
import com.maveric.srikanth.moviesearch.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private lateinit var mViewModel: MovieListViewModel
    private val mMovieListAdapter = MovieListAdapter(arrayListOf())
    private var mSearchText = INITIAL_SEARCH_TEXT
    private var mPageCount = INITIAL_PAGE_COUNT
    private var mIsAllItemsFetched = false

    private companion object {
        const val INITIAL_PAGE_COUNT = 1
        const val GRID_SIZE = 2
        const val INITIAL_SEARCH_TEXT = "Marvel"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        mViewModel.fetchMovieList(mSearchText, INITIAL_PAGE_COUNT)
        initViews()
        observeViewModel()
    }

    /*
    * Initialise Views
    * */
    private fun initViews() {
        initRecyclerView()
        initSearchView()
    }

    private fun initRecyclerView() {
        movieRecyclerView.apply {
            layoutManager = GridLayoutManager(context, GRID_SIZE)
            addItemDecoration(GridItemDecoration(20, GRID_SIZE))
            adapter = mMovieListAdapter

            addOnScrollListener(object :
                PaginationScrollListener(layoutManager as GridLayoutManager) {
                override fun loadMoreItems() {
                    mViewModel.fetchMovieList(mSearchText, ++mPageCount)
                }

                override fun isAllItemsFetched() = mIsAllItemsFetched

                override fun isLoading() = mViewModel.loading.value ?: false

            })
        }
    }

    private fun initSearchView() {
        movieSearchView.setBackgroundResource(R.drawable.round_searchview_bg)
        movieSearchView.setIconifiedByDefault(false)
        movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mMovieListAdapter.clearMovieList()
                    mSearchText = it
                    mViewModel.fetchMovieList(it, INITIAL_PAGE_COUNT)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
        movieSearchView.setQuery(mSearchText, false)
    }


    /*
    * Observing LiveData members in ViewModel and handing respective UI changes
    * */
    private fun observeViewModel() {
        mViewModel.movieListResponse.observe(viewLifecycleOwner, Observer { movieListResponse ->
            movieListResponse?.let {
                mMovieListAdapter.updateMovieList(movieListResponse.movieList)
                mIsAllItemsFetched = mMovieListAdapter.itemCount >= movieListResponse.totalResults
            }
        })

        mViewModel.movieListLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                errorTextView.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        })

        mViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                if (isLoading) {
                    listProgressBar.visibility = View.VISIBLE
                    errorTextView.visibility = View.GONE
                } else {
                    listProgressBar.visibility = View.INVISIBLE
                }
            }
        })
    }
}