package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.content.Context
import android.text.format.DateFormat
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.model.Genre
import com.swapcard.apps.moviebrowser.android.model.Movie
import java.text.SimpleDateFormat
import java.util.*

class MovieViewModel(val movie: Movie) {
    private val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    fun listItemTitle(): String {
        return when (movie.release) {
            null -> movie.title
            else -> "${movie.title}  (${simpleDateFormat.format(movie.release)})"
        }
    }

    fun listItemSubtitle1(context: Context): String {
        return genres(context)
    }

    fun listItemSubtitle2(): String {
        return if (movie.rating > 0.0) "${movie.rating}" else ""
    }

    fun listItemFavoriteResId(): Int {
        return when (movie.favorite) {
            true -> R.drawable.favorite_tint_24
            false -> R.drawable.not_favorite_tint_24
        }
    }

    fun rating(): String {
        return if (movie.rating > 0.0) "${movie.rating}" else NOT_AVAILABLE
    }

    fun release(context: Context): String {
        val dateFormat = DateFormat.getMediumDateFormat(context)
        movie.release?.let {
            return dateFormat.format(movie.release)
        }
        return NOT_AVAILABLE
    }

    fun runtime(context: Context): String {
        val builder = StringBuilder()
        val hours = movie.runtime / 60
        val minutes = movie.runtime % 60
        var prefix = ""
        if (hours > 0) {
            builder.append("${hours}${context.getString(R.string.abbr_hour)}")
            prefix = " "
        }
        if (minutes > 0) builder.append("${prefix}${minutes}${context.getString(R.string.abbr_minute)}")
        return builder.toString()
    }

    fun genres(context: Context): String {
        return movie.genres.joinToString { genre -> genre.toString(context) }
    }

    companion object {
        private const val NOT_AVAILABLE = "N/A"
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
