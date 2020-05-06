package com.example.themovieapp.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.getOrAwaitValue
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MovieListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: MovieListViewModel

    @Before
    fun setupViewModel(){
        viewModel = MovieListViewModel()
    }

    @Test
    fun onLoadMoreMoviesClickedTest() {
        val pageCount = viewModel.pageCount

        viewModel.onLoadMoreMoviesClicked()
        assertTrue("'load more movies' page incrementation",
            viewModel.pageCount==pageCount.inc())
    }

    //pretty useless test tbh..
    @Test
    fun getMoviesTest(){
        viewModel.getMovies(viewModel.pageCount)

        assertTrue("${viewModel.status.getOrAwaitValue()}",
            viewModel.status.getOrAwaitValue()==MovieApiStatus.LOADING||
                    viewModel.status.getOrAwaitValue()==MovieApiStatus.DONE)
    }
}