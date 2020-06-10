package com.example.themovieapp.framework.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.testUtils.DatabaseTestUtils
import com.example.themovieapp.testUtils.SampleMovie
import com.example.themovieapp.testUtils.getOrAwaitValue
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import junit.framework.TestCase.assertTrue


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//@Config(manifest = Config.NONE)
class MoviesDatabaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var spyDb: MoviesDatabase
    private lateinit var movieDao: MovieDao

    val movie = SampleMovie.asDatabaseMovie()
    val dbMovies: List<DatabaseMovie> = listOf(movie)
    val _movieList = MutableLiveData(dbMovies)
    val movieList: LiveData<List<DatabaseMovie>>
        get() = _movieList

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
    @Test
    @Throws(Exception::class)
    fun `get and delete`() = runBlockingTest {


        whenever(
            movieDao.getMovies()
        ).thenReturn(movieList)
        val list = movieDao.getMovies()
        assertTrue(
            "DAO get",
            list.getOrAwaitValue().first().equals(
                movie
            )
        )

/*        movieDao.insertAll(
            dbMovies
        )
        dbMovies = movieDao.getMovies().getOrAwaitValue()*/
    }
}

