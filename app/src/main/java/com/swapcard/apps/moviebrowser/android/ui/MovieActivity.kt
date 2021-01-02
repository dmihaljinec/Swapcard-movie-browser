package com.swapcard.apps.moviebrowser.android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
    }

    companion object {
        const val EXTRA_MOVIE = "movie"
    }
}

fun movieClickListener(context: Context?): (MovieViewModel) -> Unit = { movieViewModel ->
    context?.run {
        startActivity(
                Intent(this, MovieActivity::class.java)
                        .apply {
                            putExtra(MovieActivity.EXTRA_MOVIE, movieViewModel.movie)
                        })
    }
}
