package com.example.themovieapp

import com.nhaarman.mockitokotlin2.verify
import com.example.themovieapp.db.MoviesDatabase
import com.example.themovieapp.testUtils.DatabaseTestUtils
import com.example.themovieapp.testUtils.SampleMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MoviesDatabaseTest {
    private lateinit var spyDb: MoviesDatabase

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        spyDb = Mockito.spy(DatabaseTestUtils.getTestDb())
    }

    @Test
    fun `Test db creation`() {
        assertNotNull(spyDb)
    }

    @Test
    fun `deletAll method clears databaseMovies`() =
        runBlocking {
            spyDb.movieDao.deleteAll()
            assertTrue(spyDb.movieDao.getMovies().isEmpty())
            //assertTrue(false)
        }

    @Test
    fun `insert and get`(){
        runBlocking {
                spyDb.movieDao.insertAll(
                    listOf(SampleMovie.asDatabaseMovie())
                )
            assertTrue(spyDb.movieDao.getMovies().size == 1)
        }
    }
}
