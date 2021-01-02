package com.swapcard.apps.moviebrowser.android.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swapcard.apps.moviebrowser.android.model.Genre
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.db.MovieDatabase.Companion.TABLE_MOVIES
import java.util.*

@Entity(tableName = TABLE_MOVIES)
data class MovieEntity(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val title: String,
        val release: Date?,
        val rating: Double,
        val runtime: Int,
        val genres: List<Genre>,
        val poster: String,
        val overview: String

) {
    constructor(movie: Movie) : this(
            movie.id,
            movie.title,
            movie.release,
            movie.rating,
            movie.runtime,
            movie.genres,
            movie.posterUrl,
            movie.overview
    )

    fun toMovie(): Movie {
        return Movie(
                id,
                title,
                release,
                rating,
                runtime,
                genres,
                poster,
                true,
                overview
        )
    }
}
