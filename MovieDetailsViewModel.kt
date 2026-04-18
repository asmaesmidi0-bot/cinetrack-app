package com.asma.cinetrack.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.model.Movie
import com.asma.cinetrack.data.model.MovieDetails
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails: StateFlow<MovieDetails?> = _movieDetails

    private val _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedMovies: StateFlow<List<Movie>> = _recommendedMovies

    private val _videoId = MutableStateFlow<String?>(null)
    val videoId: StateFlow<String?> = _videoId

    val isFavorite: StateFlow<Boolean> = movieRepository.isFavoriteMovie(movieId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        fetchMovieDetails()
        fetchRecommendedMovies()
        fetchVideoId()
    }

    private fun fetchMovieDetails() {
        viewModelScope.launch {
            try {
                val details = movieRepository.getMovieDetails(movieId)
                _movieDetails.value = details
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel", "Error fetching movie details", e)
            }
        }
    }

    private fun fetchRecommendedMovies() {
        viewModelScope.launch {
            try {
                val response = movieRepository.getRecommendedMovies(movieId)
                _recommendedMovies.value = response.results
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel", "Error fetching recommended movies", e)
            }
        }
    }

    private fun fetchVideoId() {
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovieVideos(movieId)
                _videoId.value = response.results.firstOrNull()?.key
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel", "Error fetching video id", e)
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val details = movieDetails.value
            if (details != null) {
                val movie = Movie(
                    id = details.id,
                    title = details.title,
                    overview = details.overview,
                    poster_path = details.poster_path,
                    backdrop_path = details.backdrop_path,
                    release_date = details.release_date,
                    vote_average = details.vote_average,
                    genre_ids = details.genres.map { it.id }
                )
                if (isFavorite.value) {
                    movieRepository.removeFavoriteMovie(movie.id)
                } else {
                    movieRepository.addFavoriteMovie(movie)
                }
            }
        }
    }
}
