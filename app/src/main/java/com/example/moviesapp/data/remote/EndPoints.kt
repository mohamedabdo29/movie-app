package com.example.moviesapp.data.remote

import com.example.moviesapp.data.models.AddFavorite
import com.example.moviesapp.data.models.Category
import com.example.moviesapp.data.models.Favorite
import com.example.moviesapp.data.models.Movie
import com.example.moviesapp.data.models.MovieId
import com.example.moviesapp.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EndPoints {

    @POST("signup")
    suspend fun signUp(@Body user: User): Response<User>

    @POST("signin")
    suspend fun signIn(@Body user: User): Response<User>

    @GET("movies")
    suspend fun getAllMovies(): Response<MutableList<Movie>>

    @GET("categories")
    suspend fun getAllCategories(): Response<MutableList<Category>>

    @GET("categories/{categoryId}/movies")
    suspend fun getMoviesByCategory(@Path("categoryId") id: String): Response<MutableList<Movie>>


    @POST("users/{userId}/favourit")
    suspend fun addFavorite(
        @Path("userId") userId: String, @Body movieId: MovieId
    ): Response<AddFavorite>


    @GET("users/{userId}/favourit")
    suspend fun getFavorite(@Path("userId") userId: String): Response<Favorite>
}

