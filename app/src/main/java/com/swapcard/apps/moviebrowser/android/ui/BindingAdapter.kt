package com.swapcard.apps.moviebrowser.android.ui

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.swapcard.apps.moviebrowser.android.model.Movie

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, movie: Movie?) {
    movie?.let {
        Glide.with(imageView)
            .load(Uri.parse(movie.posterUrl))
            .into(imageView)
    }
}
