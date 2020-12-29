package com.swapcard.apps.moviebrowser.android.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class TabsAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 1

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PopularMovieListFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Popular"
    }
}