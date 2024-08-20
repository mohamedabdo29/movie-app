package com.example.moviesapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val rating: Float,
    val category: MutableList<Category>,
    val duration: String,
    val author: String,
    val dateOfProduction: String,
    val numberOfWatch: Long,
    val image: String,
    val video: String,
    @SerializedName("__v")
    val v: Float,
) : Parcelable