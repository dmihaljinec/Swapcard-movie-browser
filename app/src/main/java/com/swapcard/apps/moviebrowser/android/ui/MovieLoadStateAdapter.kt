package com.swapcard.apps.moviebrowser.android.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.swapcard.apps.moviebrowser.android.BR
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.PagingLoadStateViewModel

class MovieLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<DataBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): DataBindingViewHolder {
        return DataBindingViewHolder(
            parent,
            R.layout.item_load_state,
            BR.loadState
        )
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, loadState: LoadState) {
        val viewModel = PagingLoadStateViewModel(loadState, retry)
        holder.viewModel = viewModel
    }
}
