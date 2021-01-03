package com.swapcard.apps.moviebrowser.android.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.databinding.FragmentMovieBinding
import com.swapcard.apps.moviebrowser.android.model.Movie
import com.swapcard.apps.moviebrowser.android.ui.MovieActivity.Companion.EXTRA_MOVIE
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalPagingApi
@AndroidEntryPoint
class MovieFragment : Fragment() {
    private val movieFragmentViewModel: MovieFragmentViewModel by viewModels()
    private val adapter = MovieListAdapter(R.layout.item_movie_polaroid)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = requireActivity()
        val args = arguments ?: Bundle()
        args.putParcelable(
            EXTRA_MOVIE,
            activity.intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        )
        arguments = args
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activity = requireAppCompatActivity()
        val binding = DataBindingUtil.inflate<FragmentMovieBinding>(
            inflater,
            R.layout.fragment_movie,
            container,
            false
        )
        binding.movieFragmentViewModel = movieFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        with(binding.toolbar) {
            activity.setSupportActionBar(this)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { activity.finish() }
            movieFragmentViewModel.movie.observe(viewLifecycleOwner) { movieViewModel ->
                activity.supportActionBar?.title = movieViewModel.movie.title
            }
        }
        binding.list.adapter = adapter
        adapter.clickListener = movieClickListener(context)
        movieFragmentViewModel.similarMovies.observe(viewLifecycleOwner) { adapter.submitList(it) }
        return binding.root
    }
}

fun Fragment.requireAppCompatActivity(): AppCompatActivity {
    val detailsActivity = activity as? AppCompatActivity
    return detailsActivity ?: throw IllegalStateException("Fragment $this not attached to AppCompatActivity.")
}
