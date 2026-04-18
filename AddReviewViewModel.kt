package com.asma.cinetrack.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.model.Review
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class AddReviewUiState(
    val rating: Float = 0f,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val submissionComplete: Boolean = false
) {
    val isFormValid: Boolean get() = rating > 0f && comment.isNotBlank()
}

@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow(AddReviewUiState())
    val uiState = _uiState.asStateFlow()

    fun onRatingChange(newRating: Float) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun onCommentChange(newComment: String) {
        _uiState.update { it.copy(comment = newComment) }
    }

    fun submitReview() {
        if (!_uiState.value.isFormValid) return

        _uiState.update { it.copy(isSubmitting = true) }

        viewModelScope.launch {
            val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val newReview = Review(
                movieId = movieId,
                username = "Anonymous", // Placeholder username
                rating = _uiState.value.rating,
                comment = _uiState.value.comment,
                date = currentDate
            )

            movieRepository.addReview(newReview)
            _uiState.update { it.copy(isSubmitting = false, submissionComplete = true) }
        }
    }
}
