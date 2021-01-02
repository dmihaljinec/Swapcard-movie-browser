package com.swapcard.apps.moviebrowser.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.swapcard.apps.moviebrowser.android.R
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
