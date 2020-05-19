package com.example.themovieapp.search

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.TestScope
import com.example.themovieapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var application: Application

    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)
    lateinit var viewModel: MovieListViewModel


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieListViewModel(managedCoroutineScope, application)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMoviesTest() {
        managedCoroutineScope.launch {
            assertTrue(
                "initial List, API status: ${viewModel.status.getOrAwaitValue()}",
                viewModel.status.getOrAwaitValue() == MovieApiStatus.DONE
            )
            assertTrue(
                "movieList has ${viewModel.movieList.value?.size}, != 20",
                viewModel.movieList.value?.size == 20
            )
            assertTrue(
                "pageCount = ${viewModel.pageCount}, != 2",
                viewModel.pageCount == 2
            )
            viewModel.onLoadMoreMoviesClicked()
            assertTrue(
                "added to list, API status: ${viewModel.status.getOrAwaitValue()}",
                viewModel.status.getOrAwaitValue() == MovieApiStatus.DONE
            )
            assertTrue(
                "movieList has ${viewModel.movieList.value?.size}, != 40",
                viewModel.movieList.value?.size == 40
            )

        }
    }

    @Test
    fun repoTest(){
        viewModel.refreshDataFromRepository(1)
    }
}