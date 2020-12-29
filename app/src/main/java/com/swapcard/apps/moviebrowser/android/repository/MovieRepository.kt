package com.swapcard.apps.moviebrowser.android.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.swapcard.apps.moviebrowser.android.graphql.MovieDataSource
import com.swapcard.apps.moviebrowser.android.model.BaseMovie
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class MovieRepository(
    private val movieDataSource: MovieDataSource
) {

    suspend fun getPopularMovieList(): Flow<PagingData<BaseMovie>> {
        return Pager(
            PagingConfig(
                PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { movieDataSource }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}