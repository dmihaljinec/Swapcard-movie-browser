Swapcard Movie Browser
=======================
This is an Android test project that implements popular movies browser. It connects to [tmdb][1] server and gets popular movies by using GraphQL API. List of popular movies has infinite scrolling and is implemented through paging. It is possible to mark a movie as favorite and this information is kept only locally. Favorites tab shows all movies that are marked as favorite. Selecting popular, favorite or similar movie opens movie details screen with some more information about movie.

Architecture
------------
Project implements clean architecture with exception of interactors (use cases) so view models are using repository directly. Data flow is implemented as reactive stream with Kotlin Flow.

Repository is using multiple data sources, network and local database. Network data sources uses Apollo client library to connect to [tmdb][1] server's GraphQL API and to get movies. There are two cases of network data sources, paging and non paging. Paging data source is used for getting infinite list of popular movies. Paging data source utilizes in-memory cache. Non paging data source is used to get single movie or list of similar movies. Local data source is using Room to store favorite movies into database. This means that favorites are available even when there is no network connection.

Paging is implemented with androidx Paging 3 library that supports Kotlin Coroutines and Flow. Popular movies list is using Paging library load state to show information when additional page is loading or in case when error occurs. For similar movies, which are not paged, view model receives [SimilarMoviesLoadState][2] flow to retain consistant behavior when movies are loaded or when error occurs with ability to retry.

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
[2]: https://github.com/dmihaljinec/Swapcard-movie-browser/blob/master/app/src/main/java/com/swapcard/apps/moviebrowser/android/repository/MovieDataSource.kt
