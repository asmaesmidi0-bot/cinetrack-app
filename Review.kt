package com.asma.cinetrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val userId: String,
    val username: String,
    val rating: Float,
    val reviewText: String,
    val timestamp: Long = System.currentTimeMillis()
)
