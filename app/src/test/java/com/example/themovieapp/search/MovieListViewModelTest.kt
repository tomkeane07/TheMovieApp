package com.example.themovieapp.search

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
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)
    lateinit var viewModel: MovieListViewModel


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieListViewModel(managedCoroutineScope)

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
}