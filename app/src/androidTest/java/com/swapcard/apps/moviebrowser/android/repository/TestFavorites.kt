package com.swapcard.apps.moviebrowser.android.repository

import androidx.paging.ExperimentalPagingApi
import androidx.room.Room.databaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.swapcard.apps.moviebrowser.android.App
import com.swapcard.apps.moviebrowser.android.db.MovieDatabase
import com.swapcard.apps.moviebrowser.android.db.RoomFavoriteMovieDataSource
import com.swapcard.apps.moviebrowser.android.model.Genre
import com.swapcard.apps.moviebrowser.android.model.Movie
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@FlowPreview
@ExperimentalPagingApi
@RunWith(AndroidJUnit4ClassRunner::class)
class TestFavorites {
    private lateinit var favoriteMovieDataSource: FavoriteMovieDataSource

    @Before
    fun initVars() {
        val context = ApplicationProvider.getApplicationContext<App>()
        val movieDatabase = databaseBuilder(context, MovieDatabase::class.java, MovieDatabase.DB_NAME)
            .build()
        favoriteMovieDataSource = RoomFavoriteMovieDataSource(movieDatabase)
    }

    @Test
    fun testFavorite() = runBlocking {
        val movieRepository = MovieRepository(
            mockk {},
            mockk {},
            favoriteMovieDataSource
        )
        var favoriteMovies = movieRepository.getFavoriteMovies().first()
        assertTrue(favoriteMovies.isEmpty())
        val movie = Movie(1, "Title", null, 6.3, 116, listOf(Genre.DOCUMENTARY), "", false, "Nice documentary", "Director", "Actors")
        movieRepository.toggleFavorite(movie)
        favoriteMovies = movieRepository.getFavoriteMovies().first()
        assertTrue(favoriteMovies.isNotEmpty())
        movieRepository.toggleFavorite(favoriteMovies.first())
        favoriteMovies = movieRepository.getFavoriteMovies().first()
        assertTrue(favoriteMovies.isEmpty())
    }
}
