package io.dhruv.weatherwise.data.network

import io.dhruv.movieapp.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("week")
    suspend fun getMovies(
        @Query("language") language: String = "en-US",
        @Query("api_key") apikey: String
    ): MovieResponse
}