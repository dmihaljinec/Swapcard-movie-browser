package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.view.View
import androidx.paging.LoadState

class PagingLoadStateViewModel(
    private val loadState: LoadState,
    retry: () -> Unit
) : LoadStateViewModel(retry) {

    override fun loadingVisibility(): Int {
        return when (loadState is LoadState.Loading) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    override fun errorVisibility(): Int {
        return when (loadState is LoadState.Error) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    override fun errorMessage(): String {
        val loadState = this.loadState as? LoadState.Error
        loadState?.run {
            return error.localizedMessage ?: ""
        }
        return ""
    }
}
