package io.dhruv.movieapp.data.repository

import io.dhruv.movieapp.data.model.MovieResponse
import io.dhruv.weatherwise.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieRepository {

    private var _data = MutableStateFlow<MovieResponse>(MovieResponse())
    val data: StateFlow<MovieResponse> get() = _data

    suspend fun getMovies(apiKey: String) {
        val apiService = RetrofitInstance.api
        apiService.getMovies(apikey = apiKey).apply {
            _data.value = this
        }
    }
}