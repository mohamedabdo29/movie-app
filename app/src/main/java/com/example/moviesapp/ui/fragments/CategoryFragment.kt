package com.example.moviesapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.example.moviesapp.data.models.Category
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentCategoryBinding
import com.example.moviesapp.ui.adapters.CategoryAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.progressBar.visibility = View.VISIBLE

        getCategory()
    }

    @SuppressLint("SetTextI18n")
    private fun getCategory() {
        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.service.getAllCategories()
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
                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Server error."


                binding.progressBar.visibility = View.GONE

                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()


            } catch (e: Exception) {
                binding.moviesRv.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = e.message

                binding.progressBar.visibility = View.GONE

                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observe(response: Response<MutableList<Category>>) {
        binding.progressBar.visibility = View.GONE

        if (response.isSuccessful) {
            binding.moviesRv.visibility = View.VISIBLE
            binding.errorText.visibility = View.GONE

            val categoryList = response.body()


            val adapter = categoryList?.let { CategoryAdapter(it, requireContext()) }

            adapter?.viewType = CategoryAdapter.VIEW_TYPE_ONE

            binding.moviesRv.adapter = adapter
        } else {
            binding.moviesRv.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = response.message().toString()

            Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()

        }

    }


}