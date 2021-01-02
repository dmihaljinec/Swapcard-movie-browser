package com.swapcard.apps.moviebrowser.android.network

import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.FavoriteMovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource.SimilarMoviesLoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GraphQLMovieDataSource(
    private val apiService: GraphQLApiService,
    private val favoriteMovieDataSource: FavoriteMovieDataSource
) : MovieDataSource {

    override suspend fun getMovie(id: Long): Flow<Movie> = withContext(Dispatchers.IO) {
        val favorites = favoriteMovieDataSource.getFavoriteMovies().first()
                .map { movie -> movie.id }.toSet()
        return@withContext apiService.getMovie(id, favorites)
    }

    override suspend fun getSimilarMovies(
            movie: Movie
    ): Flow<SimilarMoviesLoadState> = withContext(Dispatchers.IO) {
        val favorites = favoriteMovieDataSource.getFavoriteMovies().first()
                .map { favoriteMovie -> favoriteMovie.id }.toSet()
        return@withContext flow {
            try {
                emit(SimilarMoviesLoadState.Loading)
                emit(SimilarMoviesLoadState.Loaded(apiService.similarMovies(movie, favorites)))
            } catch (e: Exception) {
                emit(SimilarMoviesLoadState.Error(e))
            }
        }
    }
}
