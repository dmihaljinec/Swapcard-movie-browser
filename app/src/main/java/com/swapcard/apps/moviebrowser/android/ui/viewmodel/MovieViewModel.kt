package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.content.Context
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.model.Genre
import com.swapcard.apps.moviebrowser.android.model.Movie
import java.text.SimpleDateFormat
import java.util.*

class MovieViewModel(val movie: Movie) {
    private val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    fun title(): String {
        return when (movie.release) {
            null -> movie.title
            else -> "${movie.title} (${simpleDateFormat.format(movie.release)})"
        }
    }

    fun genres(context: Context): String {
        return movie.genres.joinToString { genre -> genre.toString(context) }
    }

    // TODO: remove
    fun favorite(): String {
        return when (movie.favorite) {
            true -> "Fav"
            false -> "Not"
        }
    }
}

fun Movie.toViewModel() = MovieViewModel(this)

fun Genre.toString(context: Context): String {
    return context.getString(when (this) {
        Genre.ACTION -> R.string.genre_action
        Genre.ADVENTURE -> R.string.genre_adventure
        Genre.ANIMATION -> R.string.genre_animation
        Genre.COMEDY -> R.string.genre_comedy
        Genre.CRIME -> R.string.genre_crime
        Genre.DOCUMENTARY -> R.string.genre_documentary
        Genre.DRAMA -> R.string.genre_drama
        Genre.FANTASY -> R.string.genre_fantasy
        Genre.FAMILY -> R.string.genre_family
        Genre.HISTORY -> R.string.genre_history
        Genre.HORROR -> R.string.genre_horror
        Genre.MUSIC -> R.string.genre_music
        Genre.MYSTERY -> R.string.genre_mystery
        Genre.ROMANCE -> R.string.genre_romance
        Genre.SCIENCE_FICTION -> R.string.genre_science_fiction
        Genre.THRILLER -> R.string.genre_thriller
        Genre.TV_MOVIE -> R.string.genre_tv_movie
        Genre.WAR -> R.string.genre_war
        Genre.WESTERN -> R.string.genre_western
    })
}
