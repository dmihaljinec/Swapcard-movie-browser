package com.swapcard.apps.moviebrowser.android.repository

import com.swapcard.apps.moviebrowser.android.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {
    suspend fun getMovie(id: Long): Flow<Movie>
    suspend fun getSimilarMovies(movie: Movie): Flow<SimilarMoviesLoadState>

    sealed class SimilarMoviesLoadState {
        object Loading : SimilarMoviesLoadState()
        data class Loaded(val movies: List<Movie>) : SimilarMoviesLoadState()
        data class Error(val throwable: Throwable) : SimilarMoviesLoadState()
    }
}
