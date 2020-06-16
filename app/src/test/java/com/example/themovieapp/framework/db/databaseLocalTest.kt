package com.example.themovieapp.framework.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.testUtils.DatabaseTestUtils
import com.example.themovieapp.testUtils.SampleMovie
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import com.nhaarman.mockitokotlin2.doReturn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//@Config(manifest = Config.NONE)
class databaseLocalTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var spyDb: MoviesDatabase

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
        spyDb = Mockito.mock(DatabaseTestUtils.getTestDb().javaClass)
    }

    @Test
    fun `Test db creation`() {
        assertNotNull(spyDb)
    }


    //    test passes, but confirms nothing as nothing is really being stored..
//    should perform in androidTest instead..?
/*    @Test
    @Throws(Exception::class)
    fun `get and delete`() {

//    doReturn(movieList).`when`(spyDb.movieDao.getMovies())

*//*        whenever(
            movieDao.getMovies()
        ).thenReturn(movieList)

        doReturn(movieList).when(movieDao)*//*
//        val list = movieDao.getMovies()
//        var result : DatabaseMovie
//        runBlocking {
//            result = list.getOrAwaitValue().first()
//
//            assertTrue(
//                "DAO get",
//                result
//                    .equals(movie)
//            )
//        }

    }*/
}

