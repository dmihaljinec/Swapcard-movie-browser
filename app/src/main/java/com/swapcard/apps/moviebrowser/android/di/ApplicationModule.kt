package com.swapcard.apps.moviebrowser.android.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room.databaseBuilder
import com.apollographql.apollo.ApolloClient
import com.swapcard.apps.moviebrowser.android.db.MovieDatabase
import com.swapcard.apps.moviebrowser.android.db.MovieDatabase.Companion.DB_NAME
import com.swapcard.apps.moviebrowser.android.db.RoomFavoriteMovieDataSource
import com.swapcard.apps.moviebrowser.android.network.GraphQLMovieDataSource
import com.swapcard.apps.moviebrowser.android.network.GraphQLPagingMovieDataSource
import com.swapcard.apps.moviebrowser.android.network.GraphQLApiService
import com.swapcard.apps.moviebrowser.android.repository.FavoriteMovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.MovieDataSource
import com.swapcard.apps.moviebrowser.android.repository.MovieRepository
import com.swapcard.apps.moviebrowser.android.repository.PagingMovieDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
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
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return databaseBuilder(context, MovieDatabase::class.java, DB_NAME)
                .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteMovieDataSource(movieDatabase: MovieDatabase): FavoriteMovieDataSource {
        return RoomFavoriteMovieDataSource(movieDatabase)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(
        graphQLApiService: GraphQLApiService,
        favoriteMovieDataSource: FavoriteMovieDataSource
    ): MovieDataSource {
        return GraphQLMovieDataSource(graphQLApiService, favoriteMovieDataSource)
    }

    @Provides
    @Singleton
    fun provideGraphQLApiService(apolloClient: ApolloClient): GraphQLApiService {
        return GraphQLApiService(apolloClient)
    }

    @Provides
    @Singleton
    fun providePagingMovieDataSource(
        graphQLApiService: GraphQLApiService,
        favoriteMovieDataSource: FavoriteMovieDataSource
    ): PagingMovieDataSource {
        return GraphQLPagingMovieDataSource(graphQLApiService, favoriteMovieDataSource)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieDataSource: MovieDataSource,
        pagingMovieDataSource: PagingMovieDataSource,
        favoriteMovieDataSource: FavoriteMovieDataSource
    ): MovieRepository {
        return MovieRepository(movieDataSource, pagingMovieDataSource, favoriteMovieDataSource)
    }
}
