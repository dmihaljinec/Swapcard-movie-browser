package com.swapcard.apps.moviebrowser.android.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.swapcard.apps.moviebrowser.android.R
import com.swapcard.apps.moviebrowser.android.databinding.FragmentMovieListBinding
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.FavoriteMovieListFragmentViewModel
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class FavoriteMovieListFragment : Fragment() {
    private val fragmentViewModel: FavoriteMovieListFragmentViewModel by viewModels()
    private val adapter = MovieListAdapter(R.layout.item_movie)
    private val clickListener: (MovieViewModel) -> Unit = { movieViewModel ->
        context?.run {
            startActivity(
                    Intent(this, MovieActivity::class.java)
                            .apply {
                                putExtra(MovieActivity.EXTRA_MOVIE, movieViewModel.movie)
                            })
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentMovieListBinding>(
                inflater,
                R.layout.fragment_movie_list,
                container,
                false
        )
        binding.list.adapter = adapter
        binding.list.removeAdapter(viewLifecycleOwner)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter.clickListener = clickListener
        adapter.favoritesClickListener = { movieViewModel ->
            lifecycleScope.launchWhenResumed {
                fragmentViewModel.toggleFavorite(movieViewModel.movie)
            }
        }
        lifecycleScope.launchWhenResumed {
            fragmentViewModel.movies().collect {
                adapter.submitList(it)
            }
        }
        return binding.root
    }
}
