package com.example.moviesapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.models.Movie
import com.example.moviesapp.databinding.CategoryMovieItemBinding

import com.example.moviesapp.ui.fragments.CategoryMoviesFragmentDirections

class MovieByCategoryAdapter(
    private val moviesList: MutableList<Movie>,
    private val context: Context
) :
    RecyclerView.Adapter<CategoryMovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMovieViewHolder {
        val binding =
            CategoryMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryMovieViewHolder(binding)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: CategoryMovieViewHolder, position: Int) {
        holder.movieName.text = moviesList[position].name
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
                CategoryMoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(
                    moviesList[position]
                )
            it.findNavController().navigate(action)

        }


    }


}

class CategoryMovieViewHolder(movieItem: CategoryMovieItemBinding) :
    RecyclerView.ViewHolder(movieItem.root) {
    val image = movieItem.movieImage
    val movieName = movieItem.movieName
    val rating = movieItem.ratingBar
}

