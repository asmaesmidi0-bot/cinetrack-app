package com.asma.cinetrack.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.model.Genre
import com.asma.cinetrack.data.model.Movie
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedGenre = MutableStateFlow<Genre?>(null)
    val selectedGenre: StateFlow<Genre?> = _selectedGenre

    init {
        fetchGenres()
        fetchMovies()
    }

    private fun fetchMovies(genreId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                 val movies = mutableListOf<Movie>()
                if (genreId != null) {
                    val response = movieRepository.getMoviesByGenre(genreId)
                    movies.addAll(response.results)
                } else {
                    for (page in 1..5) {
                        val response = movieRepository.getPopular(page)
                        movies.addAll(response.results)
                    }
                }
                _movies.value = movies
            } catch (e: Exception) {
                Log.e("DiscoverViewModel", "Error fetching movies", e)
            }
            _isLoading.value = false
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            try {
                val response = movieRepository.getGenres()
                _genres.value = response.genres
            } catch (e: Exception) {
                Log.e("DiscoverViewModel", "Error fetching genres", e)
            }
        }
    }

    fun searchMovies(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = movieRepository.searchMovies(query)
                _movies.value = response.results
            } catch (e: Exception) {
                Log.e("DiscoverViewModel", "Error searching movies", e)
            }
            _isLoading.value = false
        }
    }

    fun onGenreSelected(genre: Genre) {
        _selectedGenre.value = genre
        fetchMovies(genre.id)
    }
}
