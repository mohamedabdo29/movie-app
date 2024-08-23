package com.example.moviesapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.R
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.databinding.FragmentProfileBinding
import com.example.moviesapp.shared.enums.LoginSkipStateEnum
import com.example.moviesapp.shared.enums.LoginStateEnum

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {


        if (SharedPreference.getLoginState() != LoginStateEnum.LoggedIn.value) {

            findNavController().navigate(R.id.action_profileFragment_to_toShowProfileFragment)
        }

        val name = SharedPreference.getName()
        if (name.isNullOrEmpty()) {
            binding.name.text = "User Name:       "
        } else {
            binding.name.text = "User Name: $name"
        }
        binding.email.text = "Email: ${SharedPreference.getEmail()}"
        binding.signOutBtn.setOnClickListener {


            SharedPreference.saveLoginState(LoginStateEnum.SessionExpired.value)
            SharedPreference.saveSkipLoginState(LoginSkipStateEnum.NotSkipLoginState.value)
            findNavController().navigate(R.id.action_profileFragment_to_signupFragment)


        }


    }


}