package com.example.moviesapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.data.models.AddFavorite
import com.example.moviesapp.data.models.ErrorResponse
import com.example.moviesapp.data.models.Favorite
import com.example.moviesapp.data.models.Movie
import com.example.moviesapp.data.models.MovieId
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentMovieDetailBinding
import com.example.moviesapp.shared.enums.LoginStateEnum
import com.example.moviesapp.ui.adapters.CategoryAdapter
import com.example.moviesapp.ui.adapters.FavoriteMoviesAdapter
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class MovieDetailFragment : Fragment() {


    private lateinit var binding: FragmentMovieDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        initView(requireContext())
        listener()
        return binding.root
    }

    private fun listener() {


        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).movie

        binding.favoriteIcon.setOnClickListener {

            if (SharedPreference.getLoginState() == LoginStateEnum.LoggedIn.value) {
                addFavorite(movie)

            } else {
                Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_LONG).show()
            }


        }
        binding.playMovie.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(movie.video)
            startActivity(intent)

        }
    }

    private fun initView(context: Context) {


        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).movie


        if (SharedPreference.getLoginState() == LoginStateEnum.LoggedIn.value) {
            binding.progressBar.visibility = View.VISIBLE
            getFavorite(movie)

        } else {
            binding.nestedSv.visibility = View.VISIBLE
            binding.errorText.visibility = View.GONE


        }

        Glide
            .with(context)
            .load(
                movie.image
            )
            .placeholder(R.drawable.image_placeholder)
            .into(binding.movieImage)


        val adapter = CategoryAdapter(movie.category, requireContext())
        adapter.viewType = CategoryAdapter.VIEW_TYPE_TWO
        binding.categoryRv.adapter = adapter

        binding.movieDate.text = movie.dateOfProduction
        binding.movieWatched.text = movie.numberOfWatch.toString()
        binding.duration.text = movie.duration
        binding.description.text = movie.description
        toolBar(movie)

    }

    private fun toolBar(movie: Movie) {
        val toolBar = binding.toolBar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolBar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = movie.name
        toolBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getFavorite(movie: Movie) {

        lifecycleScope.launch {


            try {
                val response = SharedPreference.getId()?.let {
                    RetrofitBuilder.service.getFavorite(
                        userId = it
                    )
                }

                if (response != null) {
                    observeGetFavorite(response, movie, requireContext())
                }


            } catch (e: UnknownHostException) {
                binding.progressBar.visibility = View.GONE

                binding.nestedSv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE

                binding.errorText.text = "No internet connection ,Try again later."

                Toast.makeText(
                    requireContext(),
                    "No internet connection ,Try again later.",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: SocketTimeoutException) {

                binding.progressBar.visibility = View.GONE
                binding.nestedSv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Request timed out. Please try again."
                Toast.makeText(
                    requireContext(),
                    "Request timed out. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()


            } catch (e: HttpException) {
                binding.nestedSv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Server error"
                binding.progressBar.visibility = View.GONE

                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()


            } catch (e: Exception) {
                binding.nestedSv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = e.message.toString()
                binding.progressBar.visibility = View.GONE

                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun observeGetFavorite(response: Response<Favorite>, movie: Movie, context: Context) {

        binding.progressBar.visibility = View.GONE


        if (response.isSuccessful) {

            val favoriteList = response.body()?.favouriteMovies

            var isFavorite = false
            if (favoriteList != null) {
                for (favoriteMovie in favoriteList)

                    if (movie.id == favoriteMovie.id) {
                        isFavorite = true
                        break

                    }
            }
            if (isFavorite) {
                binding.favoriteIcon.setColorFilter(
                    Color.parseColor("#FF1111"), // Red color
                    PorterDuff.Mode.SRC_IN
                )
            }
            binding.nestedSv.visibility = View.VISIBLE
            binding.errorText.visibility = View.GONE
        } else {
            binding.nestedSv.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = response.message().toString()

            Log.i("error messagge", response.message().toString())
            Toast.makeText(
                requireContext(),
                response.message().toString(),
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun addFavorite(movie: Movie) {
        lifecycleScope.launch {

            SharedPreference.getId()
                ?.let { it ->
                    try {
                        val response = RetrofitBuilder.service.addFavorite(
                            it, MovieId(movieId = movie.id)
                        )


                        observe(response)
                    } catch (e: SocketTimeoutException) {

                        Toast.makeText(
                            requireContext(),
                            "Request timed out. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()

                    } catch (e: Exception) {
                        Log.i("error messagge", e.message.toString())
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

                    }
                }
        }
    }

    private fun observe(response: Response<AddFavorite>) {


        if (response.isSuccessful) {
            binding.favoriteIcon.setColorFilter(
                Color.parseColor("#FF1111"), // Red color
                PorterDuff.Mode.SRC_IN
            )
            Toast.makeText(
                requireContext(),
                "Successfully added to Favorite Movie",
                Toast.LENGTH_LONG
            ).show()

        } else {

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )
            Log.i("error messagge", errorResponse.error!!)
            Toast.makeText(
                requireContext(),
                errorResponse.error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}