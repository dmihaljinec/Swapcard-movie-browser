package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.content.Context
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.model.BaseMovie
import com.swapcard.apps.moviebrowser.android.model.Genres
import java.text.SimpleDateFormat
import java.util.*

class MovieViewModel(val movie: BaseMovie) {
    private val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    fun title(): String {
        return when (movie.release) {
            null -> movie.title
            else -> "${movie.title} (${simpleDateFormat.format(movie.release)})"
        }
    }

    fun genres(context: Context): String {
        return movie.genres.map { genre -> genre.toString(context) }.joinToString()
    }
}

fun BaseMovie.toViewModel() = MovieViewModel(this)

fun Genres.toString(context: Context): String {
    return context.getString(when (this) {
        Genres.ACTION -> R.string.genre_action
        Genres.ADVENTURE -> R.string.genre_adventure
        Genres.ANIMATION -> R.string.genre_animation
        Genres.COMEDY -> R.string.genre_comedy
        Genres.CRIME -> R.string.genre_crime
        Genres.DOCUMENTARY -> R.string.genre_documentary
        Genres.DRAMA -> R.string.genre_drama
        Genres.FANTASY -> R.string.genre_fantasy
        Genres.FAMILY -> R.string.genre_family
        Genres.HISTORY -> R.string.genre_history
        Genres.HORROR -> R.string.genre_horror
        Genres.MUSIC -> R.string.genre_music
        Genres.MYSTERY -> R.string.genre_mystery
        Genres.ROMANCE -> R.string.genre_romance
        Genres.SCIENCE_FICTION -> R.string.genre_science_fiction
        Genres.THRILLER -> R.string.genre_thriller
        Genres.TV_MOVIE -> R.string.genre_tv_movie
        Genres.WAR -> R.string.genre_war
        Genres.WESTERN -> R.string.genre_western
    })
}
