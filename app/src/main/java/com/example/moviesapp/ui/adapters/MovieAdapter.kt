package com.example.moviesapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.models.Movie
import com.example.moviesapp.databinding.MovieItemBinding
import com.example.moviesapp.ui.CategoryMoviesFragmentDirections
import com.example.moviesapp.ui.HomeFragmentDirections

class MovieAdapter(private val moviesList: MutableList<Movie>, private val context: Context) :
    RecyclerView.Adapter<MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieName.text = moviesList[position].name

        holder.date.text = moviesList[position].dateOfProduction
        holder.rating.rating = moviesList[position].rating.div(2)
        Glide
            .with(context)
            .load(
                moviesList[position].image
            )
            .placeholder(R.drawable.image_placeholder)
            .into(holder.image)


        holder.itemView.setOnClickListener {

            val action =
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(moviesList[position])
            it.findNavController().navigate(action)
        }


    }


}

class MovieViewHolder(movieItem: MovieItemBinding) : RecyclerView.ViewHolder(movieItem.root) {
    val image = movieItem.movieImage
    val movieName = movieItem.movieName
    val rating = movieItem.ratingBar
    val date = movieItem.movieDate
}
