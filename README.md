Swapcard Movie Browser
=======================
This is an Android test project that implements popular movies browser. It connects to [tmdb][1] server and gets popular movies by using GraphQL API. List of popular movies has infinite scrolling and is implemented through paging. It is possible to mark a movie as favorite and this information is kept only locally. Favorites tab shows all movies that are marked as favorite. Selecting popular, favorite or similar movie opens movie details screen with some more information about movie.

Architecture
------------
Project implements clean architecture with exception of interactors (use cases) so view models are using repository directly. Data flow is implemented as reactive stream with Kotlin Flow.

Repository is using multiple data sources, local database and network. [Local data source][2] is using Room to store favorite movies into database. This means that favorites are available even when there is no network connection. Network data sources uses Apollo client library to connect to [tmdb][1] server's GraphQL API and to get movies. There are two cases of network data sources, non paging and paging. [Non paging data source][3] is used to get single movie or list of similar movies. [Paging data source][4] is used for getting infinite list of popular movies. As popular movies list contains information about favorite movies, paging data source combines flows from network and from database. When movie favorite property changes, [network paging source][5] is invalidate so that list can be recreated. To prevent fetching popular movies from network again, paging source utilizes in-memory cache.

Paging is implemented with androidx Paging 3 library that supports Kotlin Coroutines and Flow. Popular movies list is using Paging library load state to show information when additional page is loading or in case when error occurs. For similar movies, which are not paged, view model receives [similar movies load state][6] flow to retain consistant behavior when movies are loaded or when error occurs with ability to retry.

Screenshots
-----------
<p float="left">
  <img src="/popular.png" width="200" />
  <img src="/favorites.png" width="200" />
  <img src="/details_top.png" width="200" />
  <img src="/details_bottom.png" width="200" />
</p>

Testing
-------
Project has unit and instrumentation tests to test application logic. Run them either through Android Studio or via terminal (./gradlew test and ./gradlew cAT)

Author
------
Damir Mihaljinec - @dmihaljinec on GitHub

[1]: https://tmdb.apps.quintero.io/
[2]: https://github.com/dmihaljinec/Swapcard-movie-browser/blob/master/app/src/main/java/com/swapcard/apps/moviebrowser/android/db/RoomFavoriteMovieDataSource.kt
[3]: https://github.com/dmihaljinec/Swapcard-movie-browser/blob/master/app/src/main/java/com/swapcard/apps/moviebrowser/android/network/GraphQLMovieDataSource.kt
[4]: https://github.com/dmihaljinec/Swapcard-movie-browser/blob/master/app/src/main/java/com/swapcard/apps/moviebrowser/android/network/GraphQLPagingMovieDataSource.kt
[5]: https://github.com/dmihaljinec/Swapcard-movie-browser/blob/master/app/src/main/java/com/swapcard/apps/moviebrowser/android/network/GraphQLPagingSource.kt
[6]: https://github.com/dmihaljinec/Swapcard-movie-browser/blob/master/app/src/main/java/com/swapcard/apps/moviebrowser/android/repository/MovieDataSource.kt
