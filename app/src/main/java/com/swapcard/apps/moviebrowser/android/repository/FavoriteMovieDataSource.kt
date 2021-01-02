package com.swapcard.apps.moviebrowser.android.repository

import com.swapcard.apps.moviebrowser.android.model.Movie
import kotlinx.coroutines.flow.Flow

interface FavoriteMovieDataSource {
    suspend fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun toggleFavorite(movie: Movie)
}
