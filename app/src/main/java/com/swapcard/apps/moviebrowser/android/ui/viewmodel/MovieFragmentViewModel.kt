package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource.SimilarMoviesLoadState
import com.swapcard.apps.moviebrowser.android.repository.MovieRepository
import com.swapcard.apps.moviebrowser.android.ui.MovieActivity.Companion.EXTRA_MOVIE
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalPagingApi
class MovieFragmentViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val extraMovie = savedStateHandle.get<Movie>(EXTRA_MOVIE) ?: throw IllegalArgumentException("Movie is required")
    private val _movie = MutableLiveData<MovieViewModel>()
    val movie: LiveData<MovieViewModel>
        get() = _movie
    private val _similarMovies = MutableLiveData<List<MovieViewModel>>()
    val similarMovies: LiveData<List<MovieViewModel>>
        get() = _similarMovies
    private val _similarMoviesLoadStateViewModel = MutableLiveData<SimilarMoviesLoadStateViewModel>()
    val similarMoviesLoadStateViewModel: LiveData<SimilarMoviesLoadStateViewModel>
        get() = _similarMoviesLoadStateViewModel

    init {
        viewModelScope.launch {
            movieRepository.getMovie(extraMovie)
                    .map { movie -> movie.toViewModel() }
                    .distinctUntilChanged()
                    .collect { movieViewModel ->
                        _movie.value = movieViewModel
                    }
        }
        loadSimilarMovies()
    }

    private fun loadSimilarMovies() {
        viewModelScope.launch {
            movieRepository.getSimilarMovies(extraMovie).collect { similarMoviesLoadState ->
                _similarMoviesLoadStateViewModel.value = SimilarMoviesLoadStateViewModel(similarMoviesLoadState, ::retry)
                when (similarMoviesLoadState) {
                    is SimilarMoviesLoadState.Loaded -> {
                        _similarMovies.value = similarMoviesLoadState.movies.map { movie -> movie.toViewModel() }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val movie = this@MovieFragmentViewModel.movie.value?.movie
            movie?.run { movieRepository.toggleFavorite(this) }
        }
    }

    private fun retry() {
        loadSimilarMovies()
    }
}
