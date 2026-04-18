package com.asma.cinetrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the database.
 * The @PrimaryKey annotation on 'id' is crucial for Room.
 */
@Entity(tableName = "user_table") // Make sure this table name matches your DAO queries
data class User(
    // This @PrimaryKey annotation tells Room that 'id' is the unique key.
    // This is the line that fixes the "Unresolved reference 'id'" error.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val email: String,
    val password: String,
    val photoUri: String? = null
)
