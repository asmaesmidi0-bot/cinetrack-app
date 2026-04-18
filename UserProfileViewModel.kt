package com.asma.cinetrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate network delay
            delay(1000)
            // In a real app, you would fetch the user profile from a repository
            _userProfile.value = UserProfile(
                id = userId,
                name = "Asma Z.",
                bio = "Android developer and movie enthusiast. I love to build beautiful and functional apps.",
                photoUrl = "",
                favoriteMovies = emptyList(),
                reviews = emptyList()
            )
            _isLoading.value = false
        }
    }
}
