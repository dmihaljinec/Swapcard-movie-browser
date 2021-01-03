package com.swapcard.apps.moviebrowser.android.ui

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.swapcard.apps.moviebrowser.android.model.Movie
import timber.log.Timber

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, movie: Movie?) {
    movie?.let {
        if (movie.posterUrl.isNotEmpty()) {
            Timber.d("Loading image: ${movie.posterUrl}")
            Glide.with(imageView)
                    .load(Uri.parse(movie.posterUrl))
                    .override(Target.SIZE_ORIGINAL)
                    .into(imageView)
        }
    }
}
