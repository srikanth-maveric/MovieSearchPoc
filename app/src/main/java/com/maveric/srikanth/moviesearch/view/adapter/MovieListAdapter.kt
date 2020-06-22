package com.maveric.srikanth.moviesearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.maveric.srikanth.moviesearch.R
import com.maveric.srikanth.moviesearch.model.Movies
import com.maveric.srikanth.moviesearch.util.getProgressDrawable
import com.maveric.srikanth.moviesearch.util.loadImage
import com.maveric.srikanth.moviesearch.view.fragments.ListFragmentDirections
import kotlinx.android.synthetic.main.view_movie_item.view.*

class MovieListAdapter(private val movieList: ArrayList<Movies>) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.itemView.movieItemTitle.text = movieList[position].title
        holder.itemView.movieItemImageView.loadImage(
            movieList[position].poster,
            getProgressDrawable(holder.itemView.context)
        )
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
            action.movieId = movieList[position].imdbID
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun updateMovieList(updatedMovieList: List<Movies>) {
        //movieList.clear()
        movieList.addAll(updatedMovieList)
        notifyDataSetChanged()
    }

    fun clearMovieList(){
        movieList.clear()
        notifyDataSetChanged()
    }
}