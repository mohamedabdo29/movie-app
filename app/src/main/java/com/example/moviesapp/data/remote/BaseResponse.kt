package com.example.moviesapp.data.remote

import com.example.moviesapp.data.models.User

data class BaseResponse<T>(
    val data: T?,
    val message: String?,
)
