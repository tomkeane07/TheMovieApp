package com.example.themovieapp.db

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.androidTestUtils.SampleMovie
import com.example.themovieapp.androidTestUtils.getOrAwaitValue
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import timber.log.Timber

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class dbTests {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDao: MovieDao
    private lateinit var db: MoviesDatabase

    val context = ApplicationProvider.getApplicationContext<Context>()


    @Before
    fun setupDB() {
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
    fun TestDbCreation() {
        assertNotNull(db)
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlockingTest {
        val movie: DatabaseMovie = SampleMovie.asDatabaseMovie()
        val moviesIn = listOf<DatabaseMovie>(movie)
        val moviesOut = movieDao.getMovies()
        movieDao.insertAll(moviesIn)
        val actual = moviesOut.getOrAwaitValue()
        assertTrue("check db populated", actual.isNotEmpty())
        assertThat("check movie id", actual[0].id, equalTo(movie.id))
    }
}