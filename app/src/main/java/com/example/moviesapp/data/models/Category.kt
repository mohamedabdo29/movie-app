package com.example.moviesapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val image: String,
    @SerializedName("__v")
    val v: Long,
) : Parcelable