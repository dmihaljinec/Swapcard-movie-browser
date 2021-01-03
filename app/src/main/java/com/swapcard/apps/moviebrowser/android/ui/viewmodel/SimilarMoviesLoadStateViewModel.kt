package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.content.Context
import android.view.View
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource.SimilarMoviesLoadState

class SimilarMoviesLoadStateViewModel(
        private val similarMoviesLoadState: SimilarMoviesLoadState,
        retry: () -> Unit
) : LoadStateViewModel(retry) {

    override fun loadingVisibility(): Int {
        return when (similarMoviesLoadState) {
            SimilarMoviesLoadState.Loading -> View.VISIBLE
            else -> View.GONE
        }
    }

    override fun emptyVisibility(): Int {
        return when(similarMoviesLoadState is SimilarMoviesLoadState.Loaded && similarMoviesLoadState.movies.isEmpty()) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    override fun errorVisibility(): Int {
        return when (similarMoviesLoadState) {
            is SimilarMoviesLoadState.Error -> View.VISIBLE
            else -> View.GONE
        }
    }

    override fun emptyMessage(context: Context): String {
        return context.getString(R.string.description_empty_similar_movies)
    }

    override fun errorMessage(context: Context): String {
        val error = similarMoviesLoadState as? SimilarMoviesLoadState.Error
        error?.run {
            return error.throwable.localizedMessage ?: ""
        }
        return ""
    }
}
