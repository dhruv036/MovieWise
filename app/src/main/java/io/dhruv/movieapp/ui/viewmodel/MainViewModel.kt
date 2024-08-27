package io.dhruv.movieapp.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dhruv.movieapp.AppContext
import io.dhruv.movieapp.BuildConfig
import io.dhruv.movieapp.utils.NetworkConnectivityObserver
import io.dhruv.movieapp.data.model.MovieResponse
import io.dhruv.movieapp.data.repository.MovieRepository
import io.dhruv.movieapp.utils.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class MainViewModel : ViewModel() {
    private val repo = MovieRepository()
    private val _searchValue = MutableStateFlow("")
    val searchValue = _searchValue.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _movies = MutableStateFlow<MovieResponse>(MovieResponse())

    init {
        getMovies()
    }

    fun getMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            val connectivityObserver = NetworkConnectivityObserver(AppContext.context!!)
            val isNetworkAvailable = connectivityObserver.observeConnectivityAsFlow().first() == ConnectionState.Available
            if (isNetworkAvailable) {
                Log.e("TAG", "getMovies: 1", )
                _isSearching.update { true }
                launch {
                    repo.data.collect {
                        _movies.value = it
                        _isSearching.update { false }
                    }
                }
                try {
                    repo.getMovies(BuildConfig.MY_API_KEY)
                }catch (e: Exception){
                    if (e is SocketTimeoutException){
                        showToast("Slow Internet please try again")
                    }
                    e.printStackTrace()
                }
            }else{
                showToast("No Internet Connection")
            }
        }
    }
    val movies: StateFlow<MovieResponse> = searchValue
        .debounce(800L)
        .onEach { _isSearching.update { true } }
        .combine(_movies) { text, movies ->
            if (text.isBlank()) {
                movies
            } else {
                val filteredResults = movies.results.filter {
                    it.doesMatchSearchQuery(text)
                }
                movies.copy(results = filteredResults) // Create a new MovieResponse with filtered results
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _movies.value
        )


    fun onSearchTextChange(text: String) {
        _searchValue.value = text
    }
}

suspend fun showToast(message: String) {
    withContext(Dispatchers.Main){
        Toast.makeText(AppContext.context, message, Toast.LENGTH_SHORT).show()
    }
}