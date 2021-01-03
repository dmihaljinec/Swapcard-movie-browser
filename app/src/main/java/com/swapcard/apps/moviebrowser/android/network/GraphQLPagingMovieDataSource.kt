package com.swapcard.apps.moviebrowser.android.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.network.GraphQLPagingSource.InvalidationReason
import com.swapcard.apps.moviebrowser.android.repository.FavoriteMovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.PagingMovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.PagingMovieDataSource.Companion.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
class GraphQLPagingMovieDataSource @Inject constructor(
    private val apiService: GraphQLApiService,
    private val favoriteMovieDataSource: FavoriteMovieDataSource
) : PagingMovieDataSource {
    private var pagingSource: GraphQLPagingSource? = null
    private var invalidationReason: InvalidationReason = InvalidationReason.UNKNOWN

    override suspend fun getPopularMovies(): Flow<PagingData<Movie>> = withContext(Dispatchers.IO) {
        val cache = GraphQLPagingSource.PagingMovieCache(mutableListOf(), null)
        return@withContext flowOf(
            favoriteMovieDataSource.getFavoriteMovies(),
            Pager(
                PagingConfig(
                    PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    GraphQLPagingSource(
                        apiService,
                        favoriteMovieDataSource,
                        cache,
                        invalidationReason
                    ).also {
                        pagingSource = it
                        invalidationReason = InvalidationReason.UNKNOWN
                    }
                }
            ).flow
        )
            .flattenMerge()
            .transform {
                @Suppress("UNCHECKED_CAST")
                val pagingData = it as? PagingData<Movie>
                if (pagingData != null) {
                    emit(pagingData)
                } else {
                    pagingSource?.invalidate()
                }
            }
    }

    override fun retryNextPage() {
        invalidationReason = InvalidationReason.RETRY_AFTER_ERROR
        pagingSource?.invalidate()
    }
}
