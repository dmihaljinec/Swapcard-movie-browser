package com.swapcard.apps.moviebrowser.android.graphql

import androidx.paging.*
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloHttpException
import com.swapcard.apps.moviebrowser.PopularMovieListQuery
import com.swapcard.apps.moviebrowser.android.model.BaseMovie
import com.swapcard.apps.moviebrowser.android.model.Genres
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalPagingApi
class MovieDataSource(
    private val apolloClient: ApolloClient
) : PagingSource<String, BaseMovie>() {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

    override suspend fun load(params: LoadParams<String>): LoadResult<String, BaseMovie> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apolloClient
                .query(PopularMovieListQuery(
                    Input.fromNullable(params.loadSize),
                    Input.fromNullable(params.key)))
                .await()

            val movies = response.data?.movies()?.popular()?.edges()?.mapNotNull { edge ->
                val details = edge?.node()?.details()
                details?.let {
                    BaseMovie(
                        details.id().toLong(),
                        details.title(),
                        releaseDate(details),
                        details.rating(),
                        genres(details),
                        details.poster().toString(),
                        false
                    )
                }
            } ?: listOf()
            val nextKey = response.data?.movies()?.popular()?.pageInfo()?.endCursor()
            Timber.d("Loaded page of size: ${movies.size} next key: $nextKey")
            if (response.hasErrors()) {
                response.errors?.forEach { error ->
                    Timber.w(error.message)
                }
            }
            LoadResult.Page(movies, params.key, nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: ApolloHttpException) {
            Timber.w(e)
            LoadResult.Error(e)
        }
    }

    private fun genres(details: PopularMovieListQuery.Details): List<Genres> {
        return details.genres().mapNotNull { genre -> genre.id().toGenre(genre.name()) }
    }

    private fun releaseDate(details: PopularMovieListQuery.Details): Date? {
        return when (val release = details.releaseDate() as? String) {
            null -> release
            else -> simpleDateFormat.parse(release)
        }
    }
}
