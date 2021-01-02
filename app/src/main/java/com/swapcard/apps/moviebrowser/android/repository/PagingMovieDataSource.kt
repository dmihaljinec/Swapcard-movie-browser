package com.swapcard.apps.moviebrowser.android.repository

import androidx.paging.PagingData
import com.swapcard.apps.moviebrowser.android.model.Movie
import kotlinx.coroutines.flow.Flow

interface PagingMovieDataSource {
    suspend fun getPopularMovies(): Flow<PagingData<Movie>>
    fun retryNextPage()

    companion object {
        const val PAGE_SIZE = 15
    }
}
