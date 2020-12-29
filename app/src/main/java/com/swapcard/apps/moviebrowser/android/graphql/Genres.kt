package com.swapcard.apps.moviebrowser.android.graphql

import com.swapcard.apps.moviebrowser.android.model.Genres
import timber.log.Timber

fun Int.toGenre(name: String): Genres? {
    return when (this) {
        28    -> Genres.ACTION
        12    -> Genres.ADVENTURE
        16    -> Genres.ANIMATION
        35    -> Genres.COMEDY
        80    -> Genres.CRIME
        99    -> Genres.DOCUMENTARY
        18    -> Genres.DRAMA
        14    -> Genres.FANTASY
        10751 -> Genres.FAMILY
        36    -> Genres.HISTORY
        27    -> Genres.HORROR
        10402 -> Genres.MUSIC
        9648  -> Genres.MYSTERY
        10749 -> Genres.ROMANCE
        878   -> Genres.SCIENCE_FICTION
        53    -> Genres.THRILLER
        10770 -> Genres.TV_MOVIE
        10752 -> Genres.WAR
        37    -> Genres.WESTERN
        else -> {
            Timber.w("Genre $name with id $this is missing from Genres enum")
            null
        }
    }
}
