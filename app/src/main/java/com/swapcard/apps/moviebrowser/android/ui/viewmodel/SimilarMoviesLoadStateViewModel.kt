package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.view.View
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

    override fun errorVisibility(): Int {
        return when (similarMoviesLoadState) {
            is SimilarMoviesLoadState.Error -> View.VISIBLE
            else -> View.GONE
        }
    }

    override fun errorMessage(): String {
        val error = similarMoviesLoadState as? SimilarMoviesLoadState.Error
        error?.run {
            return error.throwable.localizedMessage ?: ""
        }
        return ""
    }
}
