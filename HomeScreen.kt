package com.asma.cinetrack.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.asma.cinetrack.data.model.Movie
import com.asma.cinetrack.ui.components.MoviePosterCard
import com.asma.cinetrack.viewmodel.HomeViewModel
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel, 
    onMovieClick: (Int) -> Unit, 
    onNavigateToHome: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    LaunchedEffect(Unit) {
        homeViewModel.fetchPopularMovies()
        homeViewModel.fetchTopRatedMovies()
        homeViewModel.fetchUpcomingMovies()
        homeViewModel.fetchRecommendedMovies()
    }

    val popularMovies by homeViewModel.popularMovies.collectAsState()
    val topRatedMovies by homeViewModel.topRatedMovies.collectAsState()
    val upcomingMovies by homeViewModel.upcomingMovies.collectAsState()
    val featuredMovie by homeViewModel.recommendedMovies.collectAsState()

    val lazyListState = rememberLazyListState()

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = { BottomNavigationBar(
            onNavigateToHome = onNavigateToHome, 
            onNavigateToDiscover = onNavigateToDiscover,
            onNavigateToFavorites = onNavigateToFavorites,
            onNavigateToProfile = onNavigateToProfile
        ) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Generous spacing for editorial blocks
        ) {
            if (featuredMovie.isNotEmpty()) {
                item {
                    FeaturedMovieCard(
                        movie = featuredMovie.first(),
                        onMovieClick = onMovieClick,
                        scrollOffset = { lazyListState.firstVisibleItemScrollOffset }
                    )
                }
            }
            item {
                MovieSection(title = "Popular", movies = popularMovies, onMovieClick = onMovieClick)
            }
            item {
                MovieSection(title = "Top Rated", movies = topRatedMovies, onMovieClick = onMovieClick)
            }
            item {
                MovieSection(title = "Upcoming", movies = upcomingMovies, onMovieClick = onMovieClick)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedMovieCard(movie: Movie, onMovieClick: (Int) -> Unit, scrollOffset: () -> Int) {
    val animatedOffset = remember { Animatable(40f) } // Slightly rising from the bottom
    val animatedAlpha = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        animatedOffset.animateTo(0f, animationSpec = tween(700)) // Cinematic reveal effect
        animatedAlpha.animateTo(1f, animationSpec = tween(700))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onMovieClick(movie.id) }
            )
            .graphicsLayer {
                translationY = animatedOffset.value
                alpha = animatedAlpha.value
            },
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = movie.backdrop_path?.let { "https://image.tmdb.org/t/p/w1280$it" },
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Parallax scrolling effect
                    translationY = scrollOffset() * 0.3f
                },
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f)),
                        startY = 250f
                    )
                )
        )
        Column(
            modifier = Modifier
                .padding(24.dp)
                .graphicsLayer {
                    // Subtly scale down and fade on scroll
                    val value = 1 - (scrollOffset() / 600f).coerceIn(0f, 1f)
                    alpha = value
                    scaleX = 1 - (1 - value) * 0.1f
                    scaleY = 1 - (1 - value) * 0.1f
                }
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format(Locale.US, "%.1f", movie.vote_average),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold // Softer contrast for secondary data
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Trending today",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light // Lighter weight for tertiary data
            )
        }
    }
}

@Composable
fun MovieSection(title: String, movies: List<Movie>, onMovieClick: (Int) -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies.size) { index ->
                val movie = movies[index]
                var visible by remember { mutableStateOf(false) }
                val animatedAlpha = remember { Animatable(0f) }
                val animatedTranslationX = remember { Animatable(40f) } // Soft horizontal slide

                LaunchedEffect(Unit) {
                    delay(index * 150L) // Staggered delay
                    visible = true
                    animatedAlpha.animateTo(1f, tween(400))
                    animatedTranslationX.animateTo(0f, tween(400))
                }

                if (visible) {
                    MoviePosterCard(
                        id = movie.id,
                        title = movie.title,
                        posterPath = movie.poster_path,
                        voteAverage = movie.vote_average,
                        onMovieClick = onMovieClick,
                        modifier = Modifier.graphicsLayer {
                            alpha = animatedAlpha.value
                            translationX = animatedTranslationX.value
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier, 
    onNavigateToHome: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    BottomAppBar(
        modifier = modifier,
        containerColor = Color(0xFF181818),
        contentColor = Color.White.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icons = listOf(
                Icons.Filled.Home,
                Icons.Filled.Search,
                Icons.Filled.Favorite,
                Icons.Filled.Settings
            )
            icons.forEachIndexed { index, icon ->
                val isSelected = selectedIndex == index
                val alpha = if (isSelected) 1f else 0.6f
                val scale = if (isSelected) 1.1f else 1f
                val animatedAlpha = remember { Animatable(alpha) }
                val animatedScale = remember { Animatable(scale) }

                LaunchedEffect(isSelected) {
                    animatedAlpha.animateTo(alpha, tween(300))
                    animatedScale.animateTo(scale, tween(300))
                }

                IconButton(onClick = {
                    selectedIndex = index
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToDiscover()
                        2 -> onNavigateToFavorites()
                        3 -> onNavigateToProfile()
                    }
                }) {
                    Icon(
                        icon,
                        contentDescription = "nav icon",
                        modifier = Modifier.graphicsLayer {
                            scaleX = animatedScale.value
                            scaleY = animatedScale.value
                            this.alpha = animatedAlpha.value
                        }
                    )
                }
            }
        }
    }
}
