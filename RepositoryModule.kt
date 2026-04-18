package com.asma.cinetrack.di

import com.asma.cinetrack.data.dao.FavoriteMovieDao
import com.asma.cinetrack.data.dao.ReviewDao
import com.asma.cinetrack.data.TMDbApiService
import com.asma.cinetrack.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        favoriteMovieDao: FavoriteMovieDao,
        reviewDao: ReviewDao,
        tmdbApiService: TMDbApiService
    ): MovieRepository {
        return MovieRepository(favoriteMovieDao, reviewDao, tmdbApiService)
    }
}
