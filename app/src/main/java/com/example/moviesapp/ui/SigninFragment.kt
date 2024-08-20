package com.example.moviesapp.ui

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
import com.example.moviesapp.data.models.ErrorResponse
import com.example.moviesapp.data.models.User
import com.example.moviesapp.data.remote.BaseResponse
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentSigninBinding
import com.example.moviesapp.shared.enums.LoginSkipStateEnum
import com.example.moviesapp.shared.enums.LoginStateEnum
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        listener()
        initViews()

        return binding.root
    }

    private fun initViews() {

        (activity as MainActivity).bottomNavVisibility(false)


        if (SharedPreference.getLoginState() == LoginStateEnum.LoggedIn.value ||
            SharedPreference.getSkipLoginState() == LoginSkipStateEnum.SkipLoginState.value
        ) {
            findNavController().navigate(R.id.action_signin_to_home)
        }

    }

    private fun listener() {

        binding.navToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
        }

        binding.skipBtn.setOnClickListener {

            SharedPreference.saveSkipLoginState(LoginSkipStateEnum.SkipLoginState.value)
            findNavController().navigate(R.id.action_signin_to_home)
        }

        binding.signInB.setOnClickListener {
            if (validateTextField()) {
                binding.progressBar.visibility = View.VISIBLE

                signIp()
            }
        }
    }

    private fun validateTextField(): Boolean {
        val email = binding.emailTf.text.toString().trim()
        val password = binding.passwordTf.text.toString().trim()


        if (email.isEmpty()) {
            binding.emailTextLayout.error = "email cannot be empty"
        } else {
            binding.emailTextLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordTextLayout.error = "password cannot be empty"
        } else {
            binding.passwordTextLayout.error = null
        }


        if (password.isNotEmpty() && email.isNotEmpty()
        ) {
            return true
        } else {
            return false
        }
    }


    private fun signIp() {

        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.service.signIn(
                    User(
                        name = null,
                        email = binding.emailTf.text.toString(),
                        password = binding.passwordTf.text.toString(),
                        favouriteMovies = null,
                        id = null,
                        v = null,
                    ),
                )

                observe(response)


            } catch (e: UnknownHostException) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(
                    requireContext(),
                    "No internet connection ,Try again later.",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: SocketTimeoutException) {

                binding.progressBar.visibility = View.GONE

                Toast.makeText(
                    requireContext(),
                    "Request timed out. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()


            } catch (e: HttpException) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    private fun observe(response: Response<User>) {
        binding.progressBar.visibility = View.GONE


        if (response.isSuccessful) {

            val user = response.body()
            Log.i("user information", user.toString())

            user?.name?.let {
                SharedPreference.saveName(user.name)
            }
            user?.id?.let {
                SharedPreference.saveId(user.id)

            }
            SharedPreference.saveEmail(user?.email!!)

            SharedPreference.saveLoginState(LoginStateEnum.LoggedIn.value)

            findNavController().navigate(R.id.action_signin_to_home)
        } else {

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )
            Log.i("error message", errorResponse.message!!)
            Toast.makeText(
                requireContext(),
                errorResponse.message.toString(),
                Toast.LENGTH_LONG
            ).show()

        }


    }

}