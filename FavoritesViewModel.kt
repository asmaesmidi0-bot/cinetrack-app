package com.asma.cinetrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.model.FavoriteMovie
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = movieRepository.getFavoriteMovies()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            movieRepository.removeFavoriteMovie(movieId)
        }
    }
}
