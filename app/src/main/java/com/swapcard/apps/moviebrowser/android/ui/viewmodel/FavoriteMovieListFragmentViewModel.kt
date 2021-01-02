package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class FavoriteMovieListFragmentViewModel @ViewModelInject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    suspend fun movies(): Flow<List<MovieViewModel>> {
        return movieRepository.getFavoriteMovies()
                .map { movies -> movies.map { movie -> movie.toViewModel() } }
    }

    suspend fun toggleFavorite(movie: Movie) {
        movieRepository.toggleFavorite(movie)
    }
}
