package com.swapcard.apps.moviebrowser.android.ui

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
import com.swapcard.apps.moviebrowser.android.databinding.FragmentPopularMovieListBinding
import com.swapcard.apps.moviebrowser.android.ui.viewmodel.PopularMovieListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class PopularMovieListFragment : Fragment() {
    private val fragmentViewModel: PopularMovieListFragmentViewModel by viewModels()
    private val adapter = MovieListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPopularMovieListBinding>(
            inflater,
            R.layout.fragment_popular_movie_list,
            container,
            false
        )
        binding.list.adapter = adapter
        //binding.list.removeAdapter(viewLifecycleOwner)
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launchWhenResumed {
            fragmentViewModel.movies().collectLatest {
                adapter.submitData(it)
            }
        }
        return binding.root
    }
}
