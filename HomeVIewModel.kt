package com.asma.cinetrack.ui.theme.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.model.Movie
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val topRatedMovies: StateFlow<List<Movie>> = _topRatedMovies

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies

    private val _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedMovies: StateFlow<List<Movie>> = _recommendedMovies

    init {
        fetchPopularMovies()
        fetchTopRatedMovies()
        fetchUpcomingMovies()
        fetchRecommendedMovies()
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (i in 1..3) {
                    val response = movieRepository.getPopular(i)
                    movies.addAll(response.results)
                }
                _popularMovies.value = movies
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (i in 1..3) {
                    val response = movieRepository.getTopRatedMovies(i)
                    movies.addAll(response.results)
                }
                _topRatedMovies.value = movies
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (i in 1..3) {
                    val response = movieRepository.getUpcomingMovies(i)
                    movies.addAll(response.results)
                }
                _upcomingMovies.value = movies
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchRecommendedMovies() {
        viewModelScope.launch {
            try {
                // You might need a specific movie ID for recommendations
                // For now, let's just get popular movies as a placeholder
                val response = movieRepository.getPopular(1)
                _recommendedMovies.value = response.results
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
