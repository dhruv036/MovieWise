package io.dhruv.movieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dhruv.movieapp.BuildConfig
import io.dhruv.movieapp.data.model.MovieResponse
import io.dhruv.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            _isSearching.update { true }
                launch {
                    repo.data.collect {
                        _movies.value = it
                        _isSearching.update { false }

                    }
                }
            repo.getMovies(BuildConfig.MY_API_KEY)
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