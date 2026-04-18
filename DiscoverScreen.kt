package com.asma.cinetrack.ui.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asma.cinetrack.ui.components.MoviePosterCard
import com.asma.cinetrack.ui.theme.CineTrackTheme
import com.asma.cinetrack.viewmodel.DiscoverViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    discoverViewModel: DiscoverViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val movies by discoverViewModel.movies.collectAsState()
    val genres by discoverViewModel.genres.collectAsState()
    val searchQuery by discoverViewModel.searchQuery.collectAsState()
    val isLoading by discoverViewModel.isLoading.collectAsState()
    val selectedGenre by discoverViewModel.selectedGenre.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { discoverViewModel.searchMovies(it) },
            onSearch = { discoverViewModel.searchMovies(it) },
            active = false,
            onActiveChange = {},
            placeholder = { Text("Search for movies...") },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            items(genres) { genre ->
                FilterChip(
                    selected = genre == selectedGenre,
                    onClick = { discoverViewModel.onGenreSelected(genre) },
                    label = { Text(genre.name) },
                )
            }
        }

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        AnimatedVisibility(
            visible = !isLoading && movies.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movies, key = { it.id }) { movie ->
                    MoviePosterCard(
                        id = movie.id,
                        title = movie.title,
                        posterPath = movie.poster_path,
                        voteAverage = movie.vote_average,
                        onMovieClick = onMovieClick,
                        modifier = Modifier.animateItem(fadeInSpec = tween(500))
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isLoading && movies.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("No movies found. Try a different search or genre.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoverScreenPreview() {
    CineTrackTheme {
        DiscoverScreen(onMovieClick = {})
    }
}
