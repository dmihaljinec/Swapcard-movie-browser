package com.swapcard.apps.moviebrowser.android.model

import android.os.Parcelable
import java.util.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
        val id: Long,
        val title: String,
        val release: Date?,
        val rating: Double,
        val runtime: Int,
        val genres: List<Genre>,
        val posterUrl: String,
        val favorite: Boolean,
        val overview: String,
        val director: String,
        val casts: String
) : Parcelable {

    fun create(isFavorite: Boolean): Movie {
        return Movie(
                id,
                title,
                release,
                rating,
                runtime,
                genres,
                posterUrl,
                isFavorite,
                overview,
                director,
                casts
        )
    }
}
