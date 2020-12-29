package com.swapcard.apps.moviebrowser.android.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.BR
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieViewModel

class MovieListAdapter : PagingDataAdapter<MovieViewModel, DataBindingViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return DataBindingViewHolder(
            parent,
            R.layout.item_movie,
            BR.movie
        )
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        val item = getItem(position)
        holder.viewModel = item
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