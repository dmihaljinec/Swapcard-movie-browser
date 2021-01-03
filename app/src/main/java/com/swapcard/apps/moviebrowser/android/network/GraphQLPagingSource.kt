package com.swapcard.apps.moviebrowser.android.network

import androidx.paging.PagingSource
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.FavoriteMovieDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class GraphQLPagingSource(
    private val apiService: GraphQLApiService,
    private val favoriteMovieDataSource: FavoriteMovieDataSource,
    private val cache: PagingMovieCache,
    private val invalidationReason: InvalidationReason = InvalidationReason.UNKNOWN
) : PagingSource<String, Movie>() {

    override suspend fun load(
        params: LoadParams<String>
    ): LoadResult<String, Movie> = withContext(Dispatchers.IO) {
        // Set of favorite movie ids
        val favorites = favoriteMovieDataSource.getFavoriteMovies().first()
                .map { movie -> movie.id }.toSet()
        return@withContext try {
            val movies: List<Movie>
            val nextKey: String?
            // When movies are requested for the first time and we have some in cache, we'll provide them
            // Only exception is when user requested to download more movies after previous try
            // ended with error
            if (params.key == null && cache.movies.size > 0 && invalidationReason == InvalidationReason.UNKNOWN) {
                Timber.d("loading from cache (${cache.movies.size})")
                movies = cache.movies
                nextKey = cache.nextKey
                updateCache(favorites)
            } else {
                Timber.d("loading from network (${params.key}, ${params.loadSize}})")
                with (apiService) {
                    val response = queryPopularMovies(params.key, params.loadSize)
                    movies = popularMovies(response, favorites).distinctBy { movie -> movie.id }
                    nextKey = nextKey(response)
                }
                updateCache(movies, nextKey)
            }
            Timber.d("loading complete (${movies.size}, $nextKey)")
            LoadResult.Page(movies, params.key, nextKey)
        } catch (e: Exception) {
            Timber.w(e)
            LoadResult.Error(e)
        }
    }

    private fun updateCache(favorites: Set<Long>) {
        val updatedCache = cache.movies.map { movie -> movie.create(favorites.contains(movie.id)) }
        cache.movies.clear()
        cache.movies.addAll(updatedCache)
    }

    private fun updateCache(movies: List<Movie>, nextKey: String?) {
        val cachedMovies = cache.movies
        cachedMovies.addAll(movies)
        cache.movies.clear()
        cache.movies.addAll(cachedMovies.distinctBy { movie -> movie.id })
        cache.nextKey = nextKey
    }

    data class PagingMovieCache(val movies: MutableList<Movie>, var nextKey: String?)

    enum class InvalidationReason {
        UNKNOWN,
        RETRY_AFTER_ERROR
    }
}
