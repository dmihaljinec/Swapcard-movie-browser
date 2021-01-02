package com.swapcard.apps.moviebrowser.android.ui

import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import com.swapcard.apps.moviebrowser.android.BR
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieViewModel

class PagingMovieListAdapter : PagingDataAdapter<MovieViewModel, DataBindingViewHolder>(diffCallback) {
    var clickListener: ((MovieViewModel) -> Unit)? = null
    var favoritesClickListener: ((MovieViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return DataBindingViewHolder(
            parent,
            R.layout.item_movie,
            BR.movie
        )
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.viewModel = item
            clickListener?.run {
                holder.itemView.setOnClickListener { this(item) }
            }
            val favorites = holder.itemView.findViewById<ImageView>(R.id.favorite)
            favoritesClickListener?.run {
                favorites.setOnClickListener { this(item) }
            }
        }
    }

    fun withMovieLoadState(header: LoadStateAdapter<*>, footer: LoadStateAdapter<*>): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = if (this.itemCount == 0 && loadStates.refresh is LoadState.Loading) {
                loadStates.refresh
            } else {
                loadStates.prepend
            }
            footer.loadState = if (this.itemCount > 0 &&
                    (loadStates.refresh is LoadState.Loading || loadStates.refresh is LoadState.Error)) {
                        // TODO loadStates.refresh !is LoadState.NotLoading
                loadStates.refresh
            } else {
                loadStates.append
            }
        }
        return ConcatAdapter(header, this, footer)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<MovieViewModel>() {
            override fun areItemsTheSame(oldItem: MovieViewModel, newItem: MovieViewModel): Boolean {
                return oldItem.movie.id == newItem.movie.id
            }
            override fun areContentsTheSame(oldItem: MovieViewModel, newItem: MovieViewModel): Boolean {
                return oldItem.movie == newItem.movie
            }
        }
    }
}
