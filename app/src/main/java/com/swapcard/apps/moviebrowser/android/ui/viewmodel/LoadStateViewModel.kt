package com.swapcard.apps.moviebrowser.android.ui.viewmodel

import android.content.Context

abstract class LoadStateViewModel(private val retry: () -> Unit) {

    abstract fun loadingVisibility(): Int
    abstract fun emptyVisibility(): Int
    abstract fun errorVisibility(): Int
    abstract fun emptyMessage(context: Context): String
    abstract fun errorMessage(context: Context): String

    fun retry() = retry.invoke()
}
