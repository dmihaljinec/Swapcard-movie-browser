package com.swapcard.apps.moviebrowser.android.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.swapcard.apps.moviebrowser.MovieQuery
import com.swapcard.apps.moviebrowser.PopularMovieListQuery
import com.swapcard.apps.moviebrowser.SimilarMovieListQuery
import com.swapcard.apps.moviebrowser.android.model.Genre
import com.swapcard.apps.moviebrowser.android.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class GraphQLApiService(private val apolloClient: ApolloClient) {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

    suspend fun queryPopularMovies(
            key: String?,
            pageSize: Int
    ): Response<PopularMovieListQuery.Data> = withContext(Dispatchers.IO) {
        val response = apolloClient
            .query(
                PopularMovieListQuery(
                    Input.fromNullable(pageSize),
                    Input.fromNullable(key))
            )
            .await()
        if (response.hasErrors()) {
            response.errors?.forEach { error ->
                Timber.w(error.message)
            }
        }
        return@withContext response
    }

    fun popularMovies(
        response: Response<PopularMovieListQuery.Data>,
        favorites: Set<Long>
    ): List<Movie> {
        return response.data?.movies()?.popular()?.edges()?.mapNotNull { edge ->
            val details = edge?.node()?.details()
            details?.let {
                Movie(
                    details.id().toLong(),
                    details.title(),
                    releaseDate(details.releaseDate()),
                    details.rating(),
                    details.runtime(),
                    genres(details),
                    details.poster().toString(),
                    favorites.contains(details.id().toLong()),
                    details.overview()
                )
            }
        } ?: listOf()
    }

    suspend fun similarMovies(
        movie: Movie,
        favorites: Set<Long>
    ): List<Movie> = withContext(Dispatchers.IO) {
        val response = apolloClient
            .query(
                SimilarMovieListQuery(movie.id.toInt())
            )
            .await()
        if (response.hasErrors()) {
            response.errors?.forEach { error ->
                Timber.w(error.message)
            }
        }
        return@withContext response.data?.movies()?.movie()?.similar()?.edges()?.mapNotNull { edge ->
            val details = edge?.node()?.details()
            details?.let {
                Movie(
                    details.id().toLong(),
                    details.title(),
                    releaseDate(details.releaseDate()),
                    details.rating(),
                    details.runtime(),
                    genres(details),
                    details.poster().toString(),
                    favorites.contains(details.id().toLong()),
                    details.overview()
                )
            }
        } ?: listOf() // TODO probably better to throw exception
    }

    suspend fun getMovie(
        id: Long,
        favorites: Set<Long>
    ): Flow<Movie> = withContext(Dispatchers.IO) {
        return@withContext flow {
            val response = apolloClient.query(
                MovieQuery(id.toInt())
            ).await()
            val details = response.data?.movies()?.movie()?.details()
            details?.let {
                emit(Movie(
                    details.id().toLong(),
                    details.title(),
                    releaseDate(details.releaseDate()),
                    details.rating(),
                    details.runtime(),
                    genres(details),
                    details.poster().toString(),
                    favorites.contains(details.id().toLong()),
                    details.overview())
                )
            }
        }
    }

    fun nextKey(response: Response<PopularMovieListQuery.Data>): String? {
        return response.data?.movies()?.popular()?.pageInfo()?.endCursor()
    }

    fun genres(details: PopularMovieListQuery.Details): List<Genre> {
        return details.genres().mapNotNull { genre -> genre.id().toGenre(genre.name()) }
    }

    fun genres(details: SimilarMovieListQuery.Details): List<Genre> {
        return details.genres().mapNotNull { genre -> genre.id().toGenre(genre.name()) }
    }

    fun genres(details: MovieQuery.Details): List<Genre> {
        return details.genres().mapNotNull { genre -> genre.id().toGenre(genre.name()) }
    }

    private fun releaseDate(date: Any?): Date? {
        return when (val release = date as? String) {
            null -> release
            else -> simpleDateFormat.parse(release)
        }
    }

    private fun Int.toGenre(name: String): Genre? {
        return when (this) {
            28    -> Genre.ACTION
            12    -> Genre.ADVENTURE
            16    -> Genre.ANIMATION
            35    -> Genre.COMEDY
            80    -> Genre.CRIME
            99    -> Genre.DOCUMENTARY
            18    -> Genre.DRAMA
            14    -> Genre.FANTASY
            10751 -> Genre.FAMILY
            36    -> Genre.HISTORY
            27    -> Genre.HORROR
            10402 -> Genre.MUSIC
            9648  -> Genre.MYSTERY
            10749 -> Genre.ROMANCE
            878   -> Genre.SCIENCE_FICTION
            53    -> Genre.THRILLER
            10770 -> Genre.TV_MOVIE
            10752 -> Genre.WAR
            37    -> Genre.WESTERN
            else -> {
                Timber.w("Genre $name with id $this is missing from Genres enum")
                null
            }
        }
    }
}
