package com.swapcard.apps.moviebrowser.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.swapcard.apps.moviebrowser.android.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewPager()
    }

    private fun setupViewPager() {
        val pager = findViewById<ViewPager>(R.id.pager)
        pager.adapter = TabsAdapter(this, supportFragmentManager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(pager)
    }
}

fun RecyclerView.removeAdapter(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) adapter = null
    })
}
