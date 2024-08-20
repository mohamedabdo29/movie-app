package com.example.moviesapp.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.moviesapp.shared.Constants
import com.example.moviesapp.shared.enums.LoginSkipStateEnum
import com.example.moviesapp.shared.enums.LoginStateEnum

@SuppressLint("StaticFieldLeak")
object SharedPreference {

    private lateinit var context: Context

    fun initialize(context: Context) {

        this.context = context
    }

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(
            "MoviesApp",
            Context.MODE_PRIVATE
        )

    fun saveLoginState(value: String) {

        val editor = sharedPreferences.edit()
        editor.putString(Constants.LOGIN_STATE, value).commit()

    }

    fun getLoginState() =
        sharedPreferences.getString(Constants.LOGIN_STATE, LoginStateEnum.Other.value)

    fun saveSkipLoginState(value: String) {

        val editor = sharedPreferences.edit()
        editor.putString(Constants.SKIP_LOGIN, value).commit()

    }

    fun getSkipLoginState() =
        sharedPreferences.getString(
            Constants.SKIP_LOGIN,
            LoginSkipStateEnum.NotSkipLoginState.value
        )


    fun saveName(value: String) {

        val editor = sharedPreferences.edit()
        editor.putString(Constants.NAME, value).commit()
    }

    fun getName() =
        sharedPreferences.getString(Constants.NAME, "")

    fun saveId(value: String) {

        val editor = sharedPreferences.edit()
        editor.putString(Constants.ID, value).commit()
    }

    fun getId() =
        sharedPreferences.getString(Constants.ID, "")

    fun saveEmail(value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.EMAIL, value).commit()
    }

    fun getEmail() =
        sharedPreferences.getString(Constants.EMAIL, "")
}
