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
import com.swapcard.apps.moviebrowser.android.ui.MovieActivity.Companion.EXTRA_MOVIE
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.MovieViewModel
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.PopularMovieListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class PopularMovieListFragment : Fragment() {
    private val fragmentViewModel: PopularMovieListFragmentViewModel by viewModels()
    private val adapter = PagingMovieListAdapter()
    private val clickListener: (MovieViewModel) -> Unit = { movieViewModel ->
        context?.run {
            startActivity(
                Intent(this, MovieActivity::class.java)
                    .apply {
                        putExtra(EXTRA_MOVIE, movieViewModel.movie)
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
        with (binding.list) {
            adapter = this@PopularMovieListFragment.adapter.withMovieLoadState(
                MovieLoadStateAdapter { fragmentViewModel.retry() },
                MovieLoadStateAdapter { fragmentViewModel.retry() }
            )
            setHasFixedSize(true)
        }
        binding.list.removeAdapter(viewLifecycleOwner)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter.clickListener = clickListener
        adapter.favoritesClickListener = { movieViewModel ->
            lifecycleScope.launchWhenResumed {
                fragmentViewModel.toggleFavorite(movieViewModel.movie)
            }
        }

        lifecycleScope.launchWhenResumed {
            fragmentViewModel.movies().collectLatest {
                adapter.submitData(it)
            }
        }
        return binding.root
    }
}
