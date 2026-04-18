package com.asma.cinetrack.viewmodel

import android.util.Log
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

    private val _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedMovies: StateFlow<List<Movie>> = _recommendedMovies

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val topRatedMovies: StateFlow<List<Movie>> = _topRatedMovies

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies

    init {
        fetchRecommendedMovies()
        fetchPopularMovies()
        fetchTopRatedMovies()
        fetchUpcomingMovies()
    }

    fun fetchRecommendedMovies() {
        viewModelScope.launch {
            try {
                val response = movieRepository.getNowPlaying()
                _recommendedMovies.value = response.results
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching recommended movies", e)
            }
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (i in 1..5) {
                    val response = movieRepository.getPopular(i)
                    movies.addAll(response.results)
                }
                _popularMovies.value = movies
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching popular movies", e)
            }
        }
    }

    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (i in 1..5) {
                    val response = movieRepository.getTopRatedMovies(i)
                    movies.addAll(response.results)
                }
                _topRatedMovies.value = movies
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching top rated movies", e)
            }
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            try {
                val movies = mutableListOf<Movie>()
                for (i in 1..5) {
                    val response = movieRepository.getUpcomingMovies(i)
                    movies.addAll(response.results)
                }
                _upcomingMovies.value = movies
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching upcoming movies", e)
            }
        }
    }
}
