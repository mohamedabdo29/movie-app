package com.example.moviesapp.ui.fragments

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
import com.example.moviesapp.data.remote.RetrofitBuilder
import com.example.moviesapp.databinding.FragmentSignupBinding
import com.example.moviesapp.shared.enums.LoginStateEnum
import com.google.gson.Gson

import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        listener()
        initViews()

        return binding.root
    }

    private fun initViews() {
        (activity as MainActivity).bottomNavVisibility(false)
    }


    private fun listener() {
        binding.navToSignIn.setOnClickListener {

            findNavController().navigate(R.id.action_signupFragment_to_signinFragment)
        }
        binding.signUpB.setOnClickListener {


            if (validateTextField()) {

                binding.progressBar.visibility = View.VISIBLE

                signUp()

            }


        }
    }

    private fun validateTextField(): Boolean {
        val name = binding.nameTf.text.toString().trim()
        val email = binding.emailTf.text.toString().trim()
        val phone = binding.phoneTf.text.toString().trim()
        val address = binding.addressTf.text.toString().trim()
        val password = binding.passwordTf.text.toString().trim()
        val confirmPassword = binding.conformPassTf.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameTextLayout.error = "name cannot be empty"
        } else {
            binding.nameTextLayout.error = null
        }

        if (email.isEmpty()) {
            binding.emailTextLayout.error = "email cannot be empty"
        } else {
            binding.emailTextLayout.error = null
        }

        if (address.isEmpty()) {
            binding.addressTextLayout.error = "address cannot be empty"
        } else {
            binding.addressTextLayout.error = null
        }

        if (phone.isEmpty()) {
            binding.phoneTextLayout.error = "phone cannot be empty"
        } else {
            binding.phoneTextLayout.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordTextLayout.error = "confirm password cannot be empty"
        } else {
            binding.confirmPasswordTextLayout.error = null
        }
        if (password.isEmpty()) {
            binding.passwordTextLayout.error = "password cannot be empty"
        } else {
            binding.passwordTextLayout.error = null
        }

        if (name.isNotEmpty() && address.isNotEmpty() && password.isNotEmpty() &&
            confirmPassword.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()
        ) {
            return true
        } else {
            return false
        }
    }


    private fun signUp() {

        lifecycleScope.launch {


            try {
                val response = RetrofitBuilder.service.signUp(
                    User(
                        name = binding.nameTf.text.toString(),
                        email = binding.emailTf.text.toString(),
                        password = binding.passwordTf.text.toString(),
                        favouriteMovies = arrayListOf(),
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

            SharedPreference.saveEmail(user?.email!!)


            user?.name?.let {
                SharedPreference.saveName(user.name)
            }
            user?.id?.let {
                SharedPreference.saveId(user.id)
            }
            SharedPreference.saveLoginState(LoginStateEnum.LoggedIn.value)

            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
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