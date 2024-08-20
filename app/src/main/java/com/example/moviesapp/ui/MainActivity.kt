package com.example.moviesapp.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.moviesapp.R
import com.example.moviesapp.data.local.SharedPreference
import com.example.moviesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }

    private fun initView() {
        SharedPreference.initialize(applicationContext)
        bottomNavController()

    }


    private fun bottomNavController() {
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        var navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    fun bottomNavVisibility(visible: Boolean) {

        when (visible) {

            true -> binding.bottomNav.visibility = View.VISIBLE
            false -> binding.bottomNav.visibility = View.GONE

        }

    }
}