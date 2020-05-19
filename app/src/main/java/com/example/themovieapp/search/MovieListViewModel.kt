package com.example.themovieapp.search

import android.app.Application
import android.os.SystemClock.sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.db.getDatabase
import com.example.themovieapp.repository.MoviesRepository
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.io.IOException
import java.lang.IllegalArgumentException

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListViewModel(
    val coroutineScope: ManagedCoroutineScope,
    val application: Application
) : ViewModel() {
    var pageCount = 1

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _status = MutableLiveData<MovieApiStatus>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val moviesRepository = MoviesRepository(
        getDatabase(application)
    )
    val repoMovieList = moviesRepository.movies


    // LiveData to handle navigation to the selected movie
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie


    init {
        refreshDataFromRepository(pageCount)
        //getMovies(pageCount)
    }


/*    fun getMovies(pageNumber: Int) =

        coroutineScope.launch{
            val getMoviesDeferred =
                MovieApi.retrofitService.getMoviesAsync(page = pageNumber)
            try {
                _status.value = MovieApiStatus.LOADING
                val responseObject = getMoviesDeferred.await()
                _status.value = MovieApiStatus.DONE
                if (_movieList.value == null) {
                    _movieList.value = ArrayList()
                }
                pageCount = pageNumber.inc()
                _movieList.value = movieList.value!!.toList().plus(responseObject.asDomainModel())
                    .sortedByDescending { it.vote_average }
            } catch (e: Exception) {
                _status.value = MovieApiStatus.ERROR
                _movieList.value = ArrayList()
            }
        }*/


    fun onLoadMoreMoviesClicked() {
        if (pageCount <= 3)
        //   getMovies(pageCount)
        else
            refreshDataFromRepository(pageCount)
    }


    /**
     * When the movie is clicked, set the [_navigateToSelectedMovie] [MutableLiveData]
     * @param movie The [Movie] that was clicked on.
     */
    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedMovie is set to null
     */
    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }


    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun refreshDataFromRepository(pageNumber: Int) {
        coroutineScope.launch {
            try {
                _status.value = MovieApiStatus.LOADING
                moviesRepository.refreshMovies(pageNumber)
                _status.value = MovieApiStatus.DONE
                pageCount = pageNumber.inc()

            } catch (networkError: IOException) {
                _status.value = MovieApiStatus.ERROR
            }
        }
        Timber.d(repoMovieList.value.toString())
    }
}


class MovieListViewModelFactory(
    private val managedCoroutineScope: ManagedCoroutineScope,
    private val application: Application
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
            return MovieListViewModel(managedCoroutineScope, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}