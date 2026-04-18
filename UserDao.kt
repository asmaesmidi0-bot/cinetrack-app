package com.asma.cinetrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the User table.
 */
@Dao
interface UserDao {

    /**
     * Inserts a user into the table. If the user already exists, it replaces it.
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    /**
     * Retrieves a user by their ID as a Flow.
     * The Flow will automatically update when the user data changes.
     * @param id the user's ID.
     * @return a flow of the user object.
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserById(id: Int): Flow<User?>

    /**
     * Retrieves the first user found in the table.
     * This is useful for single-user applications.
     * @return a a flow of the user object.
     */
    @Query("SELECT * FROM user_table LIMIT 1")
    fun getFirstUser(): Flow<User?>
}
