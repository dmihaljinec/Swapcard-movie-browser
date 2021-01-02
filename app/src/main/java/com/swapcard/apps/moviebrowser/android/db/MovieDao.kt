package com.swapcard.apps.moviebrowser.android.db

import androidx.room.*
import com.swapcard.apps.moviebrowser.android.db.MovieDatabase.Companion.TABLE_MOVIES
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(movie: MovieEntity)

    @Delete
    suspend fun remove(movie: MovieEntity)

    @Query("SELECT * FROM $TABLE_MOVIES")
    fun getAll(): Flow<List<MovieEntity>>
}
