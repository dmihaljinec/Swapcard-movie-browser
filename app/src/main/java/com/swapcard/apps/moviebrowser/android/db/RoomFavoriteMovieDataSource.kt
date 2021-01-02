package com.swapcard.apps.moviebrowser.android.db

import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.FavoriteMovieDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomFavoriteMovieDataSource @Inject constructor(
        private val favoriteMovies: MovieDatabase
) : FavoriteMovieDataSource {

    override suspend fun getFavoriteMovies(): Flow<List<Movie>> = withContext(Dispatchers.IO) {
        return@withContext favoriteMovies.movieDao().getAll()
                .map { movieEntities -> movieEntities.map { movieEntity -> movieEntity.toMovie() } }
    }

    override suspend fun toggleFavorite(movie: Movie) = withContext(Dispatchers.IO) {
        when (movie.favorite) {
            true -> favoriteMovies.movieDao().remove(MovieEntity(movie))
            false -> favoriteMovies.movieDao().add(MovieEntity(movie))
        }
    }
}
