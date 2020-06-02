package com.example.themovieapp.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.androidTestUtils.getOrAwaitValue
import com.example.themovieapp.db.MovieDao
import com.example.themovieapp.db.MoviesDatabase
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MoviesRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDao: MovieDao
    private lateinit var db: MoviesDatabase
    private lateinit var repository: MoviesRepository

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope =
        TestScope(testDispatcher)

    val context = ApplicationProvider.getApplicationContext<Context>()


    @Before
    fun setupRepo() {
        db = Room.inMemoryDatabaseBuilder(
            context, MoviesDatabase::class.java
        ).build()
        movieDao = db.movieDao
        repository = MoviesRepository(db)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun TestDbCreation() {
        TestCase.assertNotNull(db)
    }

    @Test
    fun refreshMoviesTest() {
        val moviesOut = repository.movies

        for (i in 1..5) {
            runBlocking {
                repository.refreshMovies(i)
            }
            val actual = moviesOut.getOrAwaitValue()

            assertThat(
                "expected ${i * 20} movies",
                actual.size, equalTo(i * 20)
            )
        }
    }
}