package com.swapcard.apps.moviebrowser.android.model

import java.util.*

data class BaseMovie(
    val id: Long,
    val title: String,
    val release: Date?,
    val rating: Double,
    val genres: List<Genres>,
    val posterUrl: String,
    val favourite: Boolean
)
