package com.example.themovieapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.themovieapp.testUtils.DatabaseTestUtils
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
//@Config(manifest = Config.NONE)
class MoviesDatabaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var spyDb: MoviesDatabase
    private lateinit var movieDao: MovieDao

    //val movie = SampleMovie.asDatabaseMovie()
    //val movies = listOf(movie)

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope =
        TestScope(testDispatcher)


    @Before
    fun setup() {
        spyDb = Mockito.spy(DatabaseTestUtils.getTestDb())
        movieDao = spyDb.movieDao
    }

    @Test
    fun `Test db creation`() {
        assertNotNull(spyDb)
    }


    //    test passes, but confirms nothing as nothing is really being stored..
//    should perform in androidTest instead..?
/*    @Test
    fun `insert and get`() {
        lateinit var dbMovies: List<DatabaseMovie>
        runBlocking {
            testScope.launch {
                movieDao.insertAll(
                    movies
                )
                dbMovies = movieDao.getMovies().getOrAwaitValue()
            }
        }
        testScope.launch { assertTrue(dbMovies.isNotEmpty()) }
    }*/

}

