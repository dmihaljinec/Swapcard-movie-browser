package com.swapcard.apps.moviebrowser.android.network

import com.swapcard.apps.moviebrowser.android.model.Genre
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.repository.FavoriteMovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.RuntimeException

class TestGraphQLMovieDataSource {
    private val movie1 = Movie(1, "Title 1", null, 6.9, 137, listOf(Genre.ACTION, Genre.ADVENTURE), "", false, "Overview text", "Director", "Actor, another one")
    private val movie2 = Movie(2, "Title 2", null, 5.4, 98, listOf(Genre.ANIMATION, Genre.FAMILY), "", false, "Overview text", "Director", "Actor, another one")
    private val movie3 = Movie(3, "Title 3", null, 8.1, 211, listOf(Genre.ACTION, Genre.FANTASY, Genre.ADVENTURE), "", false, "Overview text", "Director", "Actor, another one")
    private val apiService = mockk<GraphQLApiService> {
        coEvery { similarMovies(movie1, any()) } coAnswers { listOf(movie3) }
        coEvery { similarMovies(movie2, any()) } coAnswers { throw RuntimeException(ERROR_MESSAGE) }
        coEvery { similarMovies(movie3, any()) } coAnswers { listOf() }
    }
    private val favoriteMovieDataSource = mockk<FavoriteMovieDataSource> {
        coEvery { getFavoriteMovies() } coAnswers { flowOf(listOf(movie1, movie2)) }
    }

    @Test
    fun testSimilarMoviesLoadState() = runBlocking {
        val movieDataSource = GraphQLMovieDataSource(apiService, favoriteMovieDataSource)
        var emitCount = 0
        movieDataSource.getSimilarMovies(movie1).collect { similarMoviesLoadState ->
            when(++emitCount) {
                1 -> assertTrue(similarMoviesLoadState is MovieDataSource.SimilarMoviesLoadState.Loading)
                2 -> assertTrue(similarMoviesLoadState is MovieDataSource.SimilarMoviesLoadState.Loaded && similarMoviesLoadState.movies.isNotEmpty())
            }
        }

        emitCount = 0
        movieDataSource.getSimilarMovies(movie2).collect { similarMoviesLoadState ->
            when(++emitCount) {
                1 -> assertTrue(similarMoviesLoadState is MovieDataSource.SimilarMoviesLoadState.Loading)
                2 -> assertTrue(similarMoviesLoadState is MovieDataSource.SimilarMoviesLoadState.Error &&
                        similarMoviesLoadState.throwable is RuntimeException && similarMoviesLoadState.throwable.message == ERROR_MESSAGE)
            }
        }

        emitCount = 0
        movieDataSource.getSimilarMovies(movie3).collect { similarMoviesLoadState ->
            when(++emitCount) {
                1 -> assertTrue(similarMoviesLoadState is MovieDataSource.SimilarMoviesLoadState.Loading)
                2 -> assertTrue(similarMoviesLoadState is MovieDataSource.SimilarMoviesLoadState.Loaded && similarMoviesLoadState.movies.isEmpty())
            }
        }
    }

    companion object {
        private const val ERROR_MESSAGE = "Failed"
    }
}
