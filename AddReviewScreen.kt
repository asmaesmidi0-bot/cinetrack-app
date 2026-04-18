package com.asma.cinetrack.ui.addreview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.asma.cinetrack.viewmodel.AddReviewUiState
import com.asma.cinetrack.viewmodel.AddReviewViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(viewModel: AddReviewViewModel, onBack: () -> Unit, onSubmissionComplete: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    var showValidationError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val shakeController = remember { Animatable(0f) }

    fun triggerShake() {
        coroutineScope.launch {
            shakeController.animateTo(10f, animationSpec = tween(50))
            shakeController.animateTo(-10f, animationSpec = tween(50))
            shakeController.animateTo(10f, animationSpec = tween(50))
            shakeController.animateTo(0f, animationSpec = tween(50))
        }
    }

    LaunchedEffect(uiState.submissionComplete) {
        if (uiState.submissionComplete) {
            onSubmissionComplete()
        }
    }

    Scaffold(
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
                Text("Add Review", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RatingInput(rating = uiState.rating, onRatingChange = viewModel::onRatingChange, showError = showValidationError && uiState.rating == 0f)
            Spacer(modifier = Modifier.height(24.dp))
            CommentInput(comment = uiState.comment, onCommentChange = viewModel::onCommentChange, showError = showValidationError && uiState.comment.isBlank())
            Spacer(modifier = Modifier.weight(1f))
            SubmitButton(
                uiState = uiState,
                modifier = Modifier.graphicsLayer { translationX = shakeController.value },
                onClick = {
                    if (uiState.isFormValid) {
                        viewModel.submitReview()
                    } else {
                        showValidationError = true
                        triggerShake()
                    }
                }
            )
        }
    }
}

@Composable
private fun RatingInput(rating: Float, onRatingChange: (Float) -> Unit, showError: Boolean) {
    val animatedColor = animateColorAsState(
        targetValue = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        label = "RatingStarColor"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Your Rating", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { star ->
                val isSelected = star <= rating
                val starColor = if (isSelected) animatedColor.value else MaterialTheme.colorScheme.surfaceVariant
                val interactionSource = remember { MutableInteractionSource() }
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Star",
                    tint = starColor,
                    modifier = Modifier
                        .size(48.dp)
                        .pressAndScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onRatingChange(star.toFloat()) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentInput(comment: String, onCommentChange: (String) -> Unit, showError: Boolean) {
    OutlinedTextField(
        value = comment,
        onValueChange = onCommentChange,
        label = { Text("Your Review") },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        placeholder = { Text("Share your thoughts to help the community.") },
        isError = showError
    )
}

@Composable
private fun SubmitButton(uiState: AddReviewUiState, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !uiState.isSubmitting,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (uiState.isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (uiState.isFormValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Text("Submit Review")
    }
}

private fun Modifier.pressAndScale(interactionSource: MutableInteractionSource): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(0.9f, tween(100))
        } else {
            scale.animateTo(1f, tween(150))
        }
    }

    this.graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}
