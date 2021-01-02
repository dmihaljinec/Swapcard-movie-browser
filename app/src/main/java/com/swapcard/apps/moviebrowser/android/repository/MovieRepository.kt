package com.swapcard.apps.moviebrowser.android.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource.SimilarMoviesLoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

@FlowPreview
@ExperimentalPagingApi
class MovieRepository(
    private val movieDataSource: MovieDataSource,
    private val pagingMovieDataSource: PagingMovieDataSource,
    private val favoriteMovieDataSource: FavoriteMovieDataSource
) {

    suspend fun getPopularMovies(): Flow<PagingData<Movie>> {
        return pagingMovieDataSource.getPopularMovies()
    }

    fun retryNextPage() {
        pagingMovieDataSource.retryNextPage()
    }

    suspend fun getMovie(movie: Movie): Flow<Movie>  = withContext(Dispatchers.IO) {
        return@withContext favoriteMovieDataSource.getFavoriteMovies()
            .transform { movies ->
                val favorites = movies.map { movie -> movie.id }.toSet()
                emit(movie.create(favorites.contains(movie.id)))
            }
    }

    suspend fun getSimilarMovies(movie: Movie): Flow<SimilarMoviesLoadState> {
        return movieDataSource.getSimilarMovies(movie)
    }

    suspend fun getFavoriteMovies(): Flow<List<Movie>> {
        return favoriteMovieDataSource.getFavoriteMovies()
    }

    suspend fun toggleFavorite(movie: Movie) {
        favoriteMovieDataSource.toggleFavorite(movie)
    }
}
