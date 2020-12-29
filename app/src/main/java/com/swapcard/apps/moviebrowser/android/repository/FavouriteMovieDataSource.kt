package com.swapcard.apps.moviebrowser.android.repository

import com.swapcard.apps.moviebrowser.android.model.BaseMovie
import kotlinx.coroutines.flow.Flow

interface FavouriteMovieDataSource {
    suspend fun getFavouriteMovieList(): Flow<BaseMovie>
}