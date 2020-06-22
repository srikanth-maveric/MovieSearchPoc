package com.maveric.srikanth.moviesearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maveric.srikanth.moviesearch.R
import com.maveric.srikanth.moviesearch.model.MovieDetailResponse
import com.maveric.srikanth.moviesearch.util.getProgressDrawable
import com.maveric.srikanth.moviesearch.util.loadImage
import com.maveric.srikanth.moviesearch.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.fragment_movie_detail.*


class DetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
            viewModel.fetchMovieDetail(DetailFragmentArgs.fromBundle(it).movieId)
        }
        observeViewModel()
    }


    /*
    * Observing LiveData members in ViewModel and handing respective UI changes
    * */
    private fun observeViewModel() {
        viewModel.movieDetail.observe(viewLifecycleOwner, Observer { movieDetailResponse ->
            movieDetailResponse?.let {
                updateViews(it)
            }
        })
    }

    private fun updateViews(response: MovieDetailResponse) {
        header_image.loadImage(
            response.poster, getProgressDrawable(requireContext())
        )
        movieDescTitle.text = response.title
        movieDescSubTitle.text = response.year
        tv_genre.text = response.genre
        tv_duration.text = response.runtime
        tv_rating.text = response.imdbRating
        tv_synopsis.text = response.plot
        tv_score.text = response.metascore
        tv_reviews.text = response.imdbRating
        tv_popularity.text = response.imdbVotes
        tv_director.text = response.director
        tv_writer.text = response.writer
        tv_actor.text = response.actors
    }
}