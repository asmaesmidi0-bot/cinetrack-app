package com.asma.cinetrack.data

import com.asma.cinetrack.data.model.FavoriteMovie

data class UserProfile(
    val id: String,
    val name: String,
    val bio: String,
    val photoUrl: String,
    val favoriteMovies: List<FavoriteMovie> = emptyList(),
    val reviews: List<Review> = emptyList()
)
