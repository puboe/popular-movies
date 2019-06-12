# Popular TV shows

This app uses **MVVM** together with **Retrofit** and **coroutines** to fetch and show popular tv shows from *The Movie DB* API.
For each show it displays the show's name, overview, rating and poster.

**Important:** Before running the app, make sure to include your Movie DB's API key in the correct *build.gradle* file.

##### Screenshot
<img src="https://user-images.githubusercontent.com/8038503/59320256-14066780-8ca4-11e9-8233-a0e612eb0cd3.png" width=250/>

##### Possible improvements:
- Add static code analysis tools.
- Add continuous integration.
- Use new jetpack's paging library to handle pagination.
- Cache results into local storage (for example a Room database).
