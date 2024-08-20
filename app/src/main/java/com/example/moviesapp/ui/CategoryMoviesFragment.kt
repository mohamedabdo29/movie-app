package com.example.moviesapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.moviesapp.data.models.Category
import com.example.moviesapp.data.models.Movie
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentCategoryMoviesBinding

import com.example.moviesapp.ui.adapters.MovieByCategoryAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class CategoryMoviesFragment : Fragment() {

    private lateinit var binding: FragmentCategoryMoviesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryMoviesBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val categoryId = CategoryMoviesFragmentArgs.fromBundle(requireArguments()).categoryId
        val name = CategoryMoviesFragmentArgs.fromBundle(requireArguments()).categoryName

        binding.progressBar.visibility = View.VISIBLE
        Log.i("mohamed", categoryId)
        val toolBar = binding.toolBar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolBar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = name
        toolBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        getMovies(categoryId)

    }

    @SuppressLint("SetTextI18n")
    private fun getMovies(categoryId: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.service.getMoviesByCategory(categoryId)
                observe(response)
            } catch (e: UnknownHostException) {
                binding.progressBar.visibility = View.GONE

                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "No internet connection ,Try again later."

                Toast.makeText(
                    requireContext(),
                    "No internet connection ,Try again later.",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: SocketTimeoutException) {

                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Request timed out. Please try again."

                binding.progressBar.visibility = View.GONE

                Toast.makeText(
                    requireContext(),
                    "Request timed out. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()


            } catch (e: HttpException) {
                binding.progressBar.visibility = View.GONE
                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Server error"

                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE

                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = e.message.toString()

                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }
        }
    }


    private fun observe(response: Response<MutableList<Movie>>) {
        binding.progressBar.visibility = View.GONE


        if (response.isSuccessful) {
            binding.moviesRv.visibility = View.VISIBLE
            binding.errorText.visibility = View.GONE
            val movieList = response.body()

            binding.moviesRv.adapter = movieList?.let {
                MovieByCategoryAdapter(it, requireContext())
            }

        } else {
            binding.moviesRv.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = response.message().toString()

            Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()

        }


    }

}