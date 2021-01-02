package com.swapcard.apps.moviebrowser.android.ui.viewmodel

abstract class LoadStateViewModel(private val retry: () -> Unit) {

    abstract fun loadingVisibility(): Int
    abstract fun errorVisibility(): Int
    abstract fun errorMessage(): String

    fun retry() = retry.invoke()
}
