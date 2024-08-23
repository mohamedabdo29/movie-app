package com.example.moviesapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.R
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.databinding.FragmentToShowFavoriteBinding
import com.example.moviesapp.shared.enums.LoginSkipStateEnum


class ToShowFavoriteFragment : Fragment() {

    lateinit var binding: FragmentToShowFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToShowFavoriteBinding.inflate(inflater, container, false)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        (activity as MainActivity).bottomNavVisibility(true)


    }

    private fun listener() {
        binding.signUpB.setOnClickListener {
            SharedPreference.saveSkipLoginState(LoginSkipStateEnum.NotSkipLoginState.value)
            findNavController().navigate(R.id.action_toShowFavoriteFragment_to_signupFragment)
        }
    }


}