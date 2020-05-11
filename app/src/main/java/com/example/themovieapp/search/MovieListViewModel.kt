package com.example.themovieapp.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.network.Movie
import com.example.themovieapp.network.MovieApi
import kotlinx.coroutines.cancel

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListViewModel(val coroutineScope: ManagedCoroutineScope) : ViewModel() {
    var pageCount = 1

    private val _movieList = MutableLiveData<List<Movie>>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    val movieList: LiveData<List<Movie>>
        get() = _movieList


    private val _status = MutableLiveData<MovieApiStatus>()


    // LiveData to handle navigation to the selected movie
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie


    init {
        getMovies(pageCount)
    }


    fun getMovies(pageNumber: Int) =

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
                _movieList.value = movieList.value!!.toList().plus(responseObject.results)
                    .sortedByDescending { it.vote_average }
            } catch (e: Exception) {
                _status.value = MovieApiStatus.ERROR
                _movieList.value = ArrayList()
            }
        }


    fun onLoadMoreMoviesClicked() =
        getMovies(pageCount)


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
}