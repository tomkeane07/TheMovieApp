package com.example.themovieapp.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.asDomainModel
import kotlinx.coroutines.cancel
import java.lang.IllegalArgumentException

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
                _movieList.value = movieList.value!!.toList().plus(responseObject.asDomainModel())
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


class MovieListViewModelFactory(
    private val managedCoroutineScope: ManagedCoroutineScope
):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieListViewModel::class.java)){
            return MovieListViewModel(managedCoroutineScope) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}