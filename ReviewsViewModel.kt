package com.asma.cinetrack.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.model.Review
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ReviewsUiState(
    val averageRating: Float = 0f,
    val totalReviews: Int = 0,
    val reviews: List<Review> = emptyList()
)

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    val uiState: StateFlow<ReviewsUiState> = movieRepository.getReviewsForMovie(movieId)
        .map { reviews ->
            val totalReviews = reviews.size
            val averageRating = if (totalReviews > 0) {
                reviews.sumOf { it.rating.toDouble() }.toFloat() / totalReviews
            } else {
                0f
            }
            ReviewsUiState(averageRating, totalReviews, reviews)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReviewsUiState()
        )
}
