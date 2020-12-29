package com.swapcard.apps.moviebrowser.android.di

import androidx.paging.ExperimentalPagingApi
import com.apollographql.apollo.ApolloClient
import com.swapcard.apps.moviebrowser.android.graphql.MovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@ExperimentalPagingApi
@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.builder()
            .serverUrl("https://tmdb.apps.quintero.io/")
            .build()
    }

    @Provides
    fun provideMovieDataSource(apolloClient: ApolloClient): MovieDataSource {
        return MovieDataSource(apolloClient)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieDataSource: MovieDataSource
    ): MovieRepository {
        return MovieRepository(movieDataSource)
    }
}