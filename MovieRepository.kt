package com.asma.cinetrack.data.repository

import com.asma.cinetrack.data.TMDbApiService
import com.asma.cinetrack.data.dao.FavoriteMovieDao
import com.asma.cinetrack.data.dao.ReviewDao
import com.asma.cinetrack.data.model.FavoriteMovie
import com.asma.cinetrack.data.model.Movie
import com.asma.cinetrack.data.model.MovieDetails
import com.asma.cinetrack.data.model.Review
import com.asma.cinetrack.data.model.VideoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val reviewDao: ReviewDao,
    private val tmdbApiService: TMDbApiService
) {

    fun getFavoriteMovies(): Flow<List<FavoriteMovie>> {
        return favoriteMovieDao.getAll()
    }

    suspend fun addFavoriteMovie(movie: Movie) {
        val favoriteMovie = FavoriteMovie(
            id = movie.id,
            title = movie.title,
            posterPath = movie.poster_path ?: "",
            releaseDate = movie.release_date,
            voteAverage = movie.vote_average
        )
        favoriteMovieDao.insert(favoriteMovie)
    }

    suspend fun removeFavoriteMovie(movieId: Int) {
        favoriteMovieDao.delete(movieId)
    }

    fun isFavoriteMovie(movieId: Int): Flow<Boolean> {
        return favoriteMovieDao.getById(movieId).map { it != null }
    }

    fun getReviewsForMovie(movieId: Int): Flow<List<Review>> {
        return reviewDao.getReviewsForMovie(movieId)
    }

    suspend fun addReview(review: Review) {
        reviewDao.insert(review)
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return tmdbApiService.getMovieDetails(movieId)
    }
    suspend fun getNowPlaying() = tmdbApiService.getNowPlaying()

    suspend fun getPopular(page: Int) = tmdbApiService.getPopular(page)

    suspend fun getTopRatedMovies(page: Int) = tmdbApiService.getTopRatedMovies(page)

    suspend fun getUpcomingMovies(page: Int) = tmdbApiService.getUpcomingMovies(page)
    
    suspend fun getGenres() = tmdbApiService.getGenres()

    suspend fun getRecommendedMovies(movieId: Int) = tmdbApiService.getRecommendedMovies(movieId)

    suspend fun getMoviesByGenre(genreId: Int) = tmdbApiService.getMoviesByGenre(genreId)

    suspend fun searchMovies(query: String) = tmdbApiService.searchMovies(query)

    suspend fun getMovieVideos(movieId: Int): VideoResponse = tmdbApiService.getMovieVideos(movieId)
}
