package com.example.moviesapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.data.models.Movie
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.ui.adapters.MovieAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

        (activity as MainActivity).bottomNavVisibility(true)

        binding.progressBar.visibility = View.VISIBLE


        getMovies()

    }

    @SuppressLint("SetTextI18n")
    private fun getMovies() {
        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.service.getAllMovies()
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

                binding.progressBar.visibility = View.GONE

                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Request timed out. Please try again."

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


                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG)
                    .show()
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
                MovieAdapter(it, requireContext())

            }

        } else {
            binding.moviesRv.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = response.message().toString()
            Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()

        }
    }


}