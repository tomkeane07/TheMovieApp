package com.example.themovieapp.db


/*@RunWith(AndroidJUnit4::class)
class dbTests {
    private lateinit var movieDao: MovieDao
    private lateinit var db: MoviesDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MoviesDatabase::class.java
        ).build()
        movieDao = db.movieDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun `TestDbCreation`() {
        assertNotNull(db)
    }

    @Test
    @Throws(IOException::class)
    fun insertAndget() {
        val movie = SampleMovie.asDatabaseMovie()
        val movies = listOf<DatabaseMovie>(movie)
        lateinit var dbMovie: DatabaseMovie
        Thread {
            movieDao.insertAll(movies)
            dbMovie = movieDao.getMovies().getOrAwaitValue().get(0)
        }
        assertThat(dbMovie.id, equalTo(movie.id))
    }
}*/