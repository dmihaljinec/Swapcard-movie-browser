Swapcard Movie Browser
=======================
This is an Android test project that implements popular movies browser. It connects to tmdb server and gets popular movies by using GraphQL API. List of popular movies has infinite scrolling and is implemented through paging. It is possible to mark a movie as favorite and this information is kept only locally. Selecting popular, favorite or similar movie opens movie details page with some more information about movie.

Architecture
------------
Project implements clean architecture with exception of interactors (use cases) so view models are using repository directly. Data flow is implemented as reactive stream with Kotlin Flow.

Repository is using multiple data sources, remote and local. Remote data source uses Apollo client library to connect to tmdb server's GraphQL API and to get movies. Local data source is using Room to store favorite movies into database. Paging is implemented with androidx Paging 3 library that supports Kotlin Coroutines and Flow.

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
