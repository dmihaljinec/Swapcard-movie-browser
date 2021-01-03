package com.swapcard.apps.moviebrowser.android.ui

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.swapcard.apps.moviebrowser.android.BR
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieViewModel

class MovieListAdapter(
        private val movieLayoutId: Int
) : ListAdapter<MovieViewModel, DataBindingViewHolder>(diffCallback) {
    var clickListener: ((MovieViewModel) -> Unit)? = null
    var favoritesClickListener: ((MovieViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return DataBindingViewHolder(
                parent,
                movieLayoutId,
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
                favorites?.setOnClickListener { this(item) }
            }
        }
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
