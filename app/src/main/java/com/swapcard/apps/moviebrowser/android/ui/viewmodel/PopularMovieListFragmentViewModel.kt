package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.swapcard.apps.moviebrowser.android.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PopularMovieListFragmentViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    suspend fun movies(): Flow<PagingData<MovieViewModel>> {
        return movieRepository.getPopularMovieList()
            .mapLatest { pagingData -> pagingData.map { baseMovie -> baseMovie.toViewModel() } }
            .cachedIn(viewModelScope)
    }
}
