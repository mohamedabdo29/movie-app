package com.example.moviesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.R
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.databinding.FragmentToShowProfileBinding
import com.example.moviesapp.shared.enums.LoginSkipStateEnum

class ToShowProfileFragment : Fragment() {

    lateinit var binding: FragmentToShowProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToShowProfileBinding.inflate(inflater, container, false)
        initView()
        listener()
        return binding.root
    }

    private fun listener() {
        binding.signInBtn.setOnClickListener {
            SharedPreference.saveSkipLoginState(LoginSkipStateEnum.NotSkipLoginState.value)
            findNavController().navigate(R.id.action_toShowProfileFragment_to_signupFragment)


        }

    }

    private fun initView() {


    }


}