package com.swapcard.apps.moviebrowser.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swapcard.apps.moviebrowser.android.db.MovieDatabase.Companion.DB_VERSION

@Database(
        entities = [MovieEntity::class],
        version = DB_VERSION,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase(){

    abstract fun movieDao(): MovieDao

    companion object {
        const val DB_NAME = "Movies.db"
        const val DB_VERSION = 1
        const val TABLE_MOVIES = "movies"
    }
}
