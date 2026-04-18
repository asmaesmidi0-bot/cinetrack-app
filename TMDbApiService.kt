package com.asma.cinetrack.data

import com.asma.cinetrack.data.model.GenreResponse
import com.asma.cinetrack.data.model.MovieDetails
import com.asma.cinetrack.data.model.MovieResponse
import com.asma.cinetrack.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApiService {
    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetails

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): VideoResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(@Query("with_genres") genreId: Int): MovieResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovies(@Path("movie_id") movieId: Int): MovieResponse
}
