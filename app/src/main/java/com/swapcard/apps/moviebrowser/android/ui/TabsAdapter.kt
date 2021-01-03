package com.swapcard.apps.moviebrowser.android.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.paging.ExperimentalPagingApi
import com.swapcard.apps.moviebrowser.android.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class TabsAdapter(
        private val context: Context,
        fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PopularMovieListFragment()
            1 -> FavoriteMovieListFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(when (position) {
            0 -> R.string.title_popular
            1 -> R.string.title_favorites
            else -> throw IllegalStateException("Unexpected position: $position")
        })
    }
}
