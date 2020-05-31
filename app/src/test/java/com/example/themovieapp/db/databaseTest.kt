package com.example.themovieapp.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.db.MoviesDatabase
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.testUtils.DatabaseTestUtils
import com.example.themovieapp.testUtils.SampleMovie
import com.example.themovieapp.testUtils.getOrAwaitValue
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
//@Config(manifest = Config.NONE)
class MoviesDatabaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var spyDb: MoviesDatabase
    private lateinit var movieDao: MovieDao

    val movie = SampleMovie.asDatabaseMovie()
    val movies = listOf(movie)

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
//    should perform in androidTest..?
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
        val x = 2
        testScope.launch { assertTrue(dbMovies.isNotEmpty()) }
    }*/

}

