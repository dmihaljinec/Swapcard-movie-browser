package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PopularMovieListFragmentViewModel @ViewModelInject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {
    private lateinit var moviesFlow: Flow<PagingData<MovieViewModel>>

    suspend fun movies(): Flow<PagingData<MovieViewModel>> {
        if (this::moviesFlow.isInitialized)
            return moviesFlow
        moviesFlow = movieRepository.getPopularMovies()
                .mapLatest { pagingData -> pagingData.map { movie -> movie.toViewModel() } }
        return moviesFlow
    }

    suspend fun toggleFavorite(movie: Movie) {
        movieRepository.toggleFavorite(movie)
    }

    fun retry() {
        movieRepository.retryNextPage()
    }
}
