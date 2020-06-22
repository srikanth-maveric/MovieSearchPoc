package com.maveric.srikanth.moviesearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.maveric.srikanth.moviesearch.R
import com.maveric.srikanth.moviesearch.util.GridItemDecoration
import com.maveric.srikanth.moviesearch.view.adapter.MovieListAdapter
import com.maveric.srikanth.moviesearch.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private lateinit var viewModel: MovieListViewModel
    private val movieListAdapter = MovieListAdapter(arrayListOf())
    private var mSearchText = "Marvel"

    companion object {
        private var initialPageCount = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        viewModel.fetchMovieList(mSearchText, initialPageCount)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        movieRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridItemDecoration(20, 2))
            adapter = movieListAdapter
        }
        movieSearchView.setIconifiedByDefault(false)
        movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mSearchText = it
                    viewModel.fetchMovieList(it, initialPageCount)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
        movieSearchView.setQuery(mSearchText, false)
    }

    private fun observeViewModel() {
        viewModel.movieList.observe(viewLifecycleOwner, Observer { movieList ->
            movieList?.let {
                movieRecyclerView.visibility = View.VISIBLE
                movieListAdapter.updateMovieList(movieList)
            }
        })

        viewModel.movieListLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                errorTextView.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                listProgressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
                if (it) {
                    movieRecyclerView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
            }
        })
    }
}