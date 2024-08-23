package com.example.moviesapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.R
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.data.models.Favorite
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentFavoriteMoviesBinding
import com.example.moviesapp.shared.enums.LoginStateEnum
import com.example.moviesapp.ui.adapters.FavoriteMoviesAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class FavoriteMoviesFragment : Fragment() {
    lateinit var binding: FragmentFavoriteMoviesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    private fun initView() {

        if (SharedPreference.getLoginState() == LoginStateEnum.LoggedIn.value) {
            binding.progressBar.visibility = View.VISIBLE
            getFavorite()

        } else {
            findNavController().navigate(R.id.action_favoriteMoviesFragment_to_toShowFavoriteFragment)

        }

    }

    @SuppressLint("SetTextI18n")
    private fun getFavorite() {

        lifecycleScope.launch {


            try {
                val response = SharedPreference.getId()?.let {
                    RetrofitBuilder.service.getFavorite(
                        userId = it
                    )
                }

                if (response != null) {
                    observe(response)
                }


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
                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Server error"
                binding.progressBar.visibility = View.GONE

                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()


            } catch (e: Exception) {
                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = e.message.toString()
                binding.progressBar.visibility = View.GONE

                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun observe(response: Response<Favorite>) {
        binding.progressBar.visibility = View.GONE


        if (response.isSuccessful) {
            binding.moviesRv.visibility = View.VISIBLE
            binding.errorText.visibility = View.GONE
            val favoriteList = response.body()?.favouriteMovies
            binding.moviesRv.adapter =
                favoriteList?.let { FavoriteMoviesAdapter(it, requireContext()) }

        } else {
            binding.moviesRv.visibility = View.GONE
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
}