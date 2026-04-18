
@file:OptIn(ExperimentalFoundationApi::class)

package com.asma.cinetrack.ui.favorites

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asma.cinetrack.R
import com.asma.cinetrack.ui.components.MoviePosterCard
import com.asma.cinetrack.ui.theme.CineTrackTheme
import com.asma.cinetrack.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onNavigateToDiscover: () -> Unit
) {
    val favoriteMovies by favoritesViewModel.favoriteMovies.collectAsState()

    AnimatedContent(targetState = favoriteMovies.isEmpty(), label = "EmptyStateAnimation") { isEmpty ->
        if (isEmpty) {
            EmptyFavoritesState(onNavigateToDiscover = onNavigateToDiscover)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.favorites_collection_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(
                            id = R.string.favorites_movie_count,
                            favoriteMovies.size
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteMovies, key = { it.id }) { favoriteMovie ->
                        MoviePosterCard(
                            id = favoriteMovie.id,
                            title = favoriteMovie.title,
                            posterPath = favoriteMovie.posterPath,
                            voteAverage = favoriteMovie.voteAverage,
                            onMovieClick = { onMovieClick(favoriteMovie.id) },
                            onRemoveFavorite = { favoritesViewModel.removeFavorite(favoriteMovie.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFavoritesState(onNavigateToDiscover: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Theaters,
                contentDescription = stringResource(id = R.string.favorites_empty_icon_content_description),
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.favorites_collection_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.favorites_empty_prompt),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onNavigateToDiscover) {
                Text(stringResource(id = R.string.favorites_explore_movies_button))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    CineTrackTheme {
        FavoritesScreen(onMovieClick = {}, onNavigateToDiscover = {})
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyFavoritesScreenPreview() {
    CineTrackTheme {
        EmptyFavoritesState(onNavigateToDiscover = {})
    }
}
