package com.asma.cinetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asma.cinetrack.ui.addreview.AddReviewScreen
import com.asma.cinetrack.ui.auth.LoginScreen
import com.asma.cinetrack.ui.auth.SignUpScreen
import com.asma.cinetrack.ui.auth.VerificationScreen
import com.asma.cinetrack.ui.discover.DiscoverScreen
import com.asma.cinetrack.ui.favorites.FavoritesScreen
import com.asma.cinetrack.ui.home.HomeScreen
import com.asma.cinetrack.ui.landing.LandingScreen
import com.asma.cinetrack.ui.moviedetails.MovieDetailsScreen
import com.asma.cinetrack.ui.profile.ChangePasswordScreen
import com.asma.cinetrack.ui.profile.ProfileScreen
import com.asma.cinetrack.ui.reviews.ReviewsScreen
import com.asma.cinetrack.ui.theme.CineTrackTheme
import com.asma.cinetrack.ui.user.UserProfileScreen
import com.asma.cinetrack.viewmodel.AuthViewModel
import com.asma.cinetrack.viewmodel.AuthState
import com.asma.cinetrack.viewmodel.ThemeViewModel
import com.asma.cinetrack.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val useDarkTheme by themeViewModel.darkTheme.collectAsState(initial = isSystemInDarkTheme())

            CineTrackTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.authState.collectAsStateWithLifecycle()

                    LaunchedEffect(authState) {
                        if (authState is AuthState.AwaitingVerification) {
                            navController.navigate("verification") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }

                    val startDestination = remember(authState) {
                        when (authState) {
                            is AuthState.Authenticated -> "home"
                            is AuthState.Unauthenticated, is AuthState.AwaitingVerification, is AuthState.Error -> "auth"
                            else -> "loading"
                        }
                    }

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("loading") {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        navigation(startDestination = "landing", route = "auth") {
                            composable("landing") {
                                LandingScreen(
                                    onLoginClick = { navController.navigate("login") },
                                    onSignUpClick = { navController.navigate("signup") }
                                )
                            }
                            composable("login") {
                                LoginScreen(
                                    authViewModel = authViewModel,
                                    onLoginSuccess = { 
                                        navController.navigate("home") { popUpTo("auth") { inclusive = true } } 
                                    },
                                    onSignUpClick = { 
                                        authViewModel.resetState()
                                        navController.navigate("signup") 
                                    }
                                )
                            }
                            composable("signup") {
                                SignUpScreen(
                                    authViewModel = authViewModel,
                                    onSignUpSuccess = { /* Handled by LaunchedEffect */ },
                                    onLoginClick = { 
                                        authViewModel.resetState()
                                        navController.navigate("login") 
                                    }
                                )
                            }
                            composable("verification") {
                                VerificationScreen(
                                    onNavigateToLogin = {
                                        authViewModel.resetState()
                                        navController.navigate("login") {
                                            popUpTo("verification") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }

                        composable("home") {
                            HomeScreen(
                                homeViewModel = hiltViewModel(),
                                onMovieClick = { movieId -> navController.navigate("movieDetails/$movieId") },
                                onNavigateToHome = { navController.navigate("home") },
                                onNavigateToDiscover = { navController.navigate("discover") },
                                onNavigateToFavorites = { navController.navigate("favorites") },
                                onNavigateToProfile = { navController.navigate("profile") }
                            )
                        }
                        composable("discover") {
                            DiscoverScreen(onMovieClick = { movieId -> navController.navigate("movieDetails/$movieId") })
                        }
                        composable("favorites") {
                            FavoritesScreen(
                                onMovieClick = { movieId -> navController.navigate("movieDetails/$movieId") },
                                onNavigateToDiscover = { navController.navigate("discover") }
                            )
                        }
                        composable(
                            "movieDetails/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) {
                            MovieDetailsScreen(
                                viewModel = hiltViewModel(),
                                onBack = { navController.popBackStack() },
                                onNavigateToReviews = { movieId -> navController.navigate("reviews/$movieId") }
                            )
                        }
                        composable(
                            "reviews/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) {
                            val movieId = it.arguments?.getInt("movieId") ?: 0
                            ReviewsScreen(
                                viewModel = hiltViewModel(),
                                onBack = { navController.popBackStack() },
                                onAddReview = { navController.navigate("addReview/$movieId") }
                            )
                        }
                        composable(
                            "addReview/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) {
                            AddReviewScreen(
                                viewModel = hiltViewModel(),
                                onBack = { navController.popBackStack() },
                                onSubmissionComplete = { navController.popBackStack() }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                authViewModel = authViewModel,
                                onBack = { navController.popBackStack() },
                                onLogout = {
                                    navController.navigate("auth") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onNavigateToUserProfile = { userId -> navController.navigate("userProfile/$userId") },
                                onNavigateToChangePassword = { navController.navigate("changePassword") }
                            )
                        }
                        composable(
                            "userProfile/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.StringType })
                        ) {
                            val userId = it.arguments?.getString("userId") ?: ""
                            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
                            LaunchedEffect(userId) {
                                userProfileViewModel.loadUserProfile(userId)
                            }
                            UserProfileScreen(
                                viewModel = userProfileViewModel,
                                onBack = { navController.popBackStack() },
                                onMovieClick = { movieId -> navController.navigate("movieDetails/$movieId") }
                            )
                        }
                        composable("changePassword") {
                            ChangePasswordScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
