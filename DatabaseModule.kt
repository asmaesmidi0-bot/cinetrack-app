
package com.asma.cinetrack.di

import android.content.Context
import androidx.room.Room
import com.asma.cinetrack.data.CineTrackDatabase
import com.asma.cinetrack.data.dao.FavoriteMovieDao
import com.asma.cinetrack.data.dao.ReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCineTrackDatabase(@ApplicationContext context: Context): CineTrackDatabase {
        return Room.databaseBuilder(
            context,
            CineTrackDatabase::class.java,
            "cinetrack_database"
        ).build()
    }

    @Provides
    fun provideFavoriteMovieDao(database: CineTrackDatabase): FavoriteMovieDao {
        return database.favoriteMovieDao()
    }

    @Provides
    fun provideReviewDao(database: CineTrackDatabase): ReviewDao {
        return database.reviewDao()
    }
}
