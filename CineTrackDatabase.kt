package com.asma.cinetrack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.asma.cinetrack.data.dao.FavoriteMovieDao
import com.asma.cinetrack.data.dao.ReviewDao
import com.asma.cinetrack.data.model.FavoriteMovie
import com.asma.cinetrack.data.model.Review

@Database(entities = [FavoriteMovie::class, Review::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CineTrackDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun reviewDao(): ReviewDao
}

class Converters {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        return value.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }
}
