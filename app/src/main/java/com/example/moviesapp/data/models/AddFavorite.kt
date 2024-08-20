package com.example.moviesapp.data.models

import com.google.gson.annotations.SerializedName

data class AddFavorite(


    @SerializedName("_id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("age") var age: Int? = null,
    @SerializedName("__v") var v: Int? = null

)

data class MovieId(
    val movieId: String?,
)
