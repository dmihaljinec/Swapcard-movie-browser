package com.swapcard.apps.moviebrowser.android.db

import androidx.room.TypeConverter
import com.swapcard.apps.moviebrowser.android.model.Genre
import java.util.*

class Converters {

    @TypeConverter
    fun genresToString(genres: List<Genre>): String {
        return genres.joinToString(separator = ",") { genre -> genre.name }
    }

    @TypeConverter
    fun genresFromString(genres: String): List<Genre> {
        return genres.split(",").map { name -> Genre.valueOf(name) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}
