package com.example.moviesapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//
//data class User(
//    val name: String? = null,
//    val email: String? = null,
//    val password: String? = null,
////    val age: String? = null,
//    @SerializedName("_id") val id: String? = null,
//    @SerializedName("__v") val v: Float? = null,
//    @SerializedName("favouriteMovies") var favouriteMovies: ArrayList<Movie> = arrayListOf(),
//
//    ) : Parcelable


data class User(

    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("favouriteMovies") val favouriteMovies: ArrayList<String>?,
    @SerializedName("_id") val id: String?,
    @SerializedName("__v") val v: Int?,
)


data class ErrorResponse(
    val message: String?,
    val error: String?
)