package com.example.themovieapp.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.db.asDomainModel
import com.example.themovieapp.db.getDatabase
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.NetworkMovieContainer
import com.example.themovieapp.network.asDomainModel
import com.example.themovieapp.repository.MoviesRepository
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.io.IOException

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListViewModel(
    val coroutineScope: ManagedCoroutineScope,
    val application: Application
) : ViewModel() {
    var pageCount = 1

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    //movies received from repo(max of 60)
    lateinit var repoMovies: List<Movie>

    //add more from web
    lateinit var webMovies: List<Movie>

    private val _status = MutableLiveData<MovieApiStatus>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    private val _dbEmpty = MutableLiveData<Boolean>()
    val dbEmpty: LiveData<Boolean>
        get() = _dbEmpty

    /**
     * The data source this ViewModel will fetch results from.
     */
    val db = getDatabase(application.applicationContext)

    private val moviesRepository = MoviesRepository(db)

    // LiveData to handle navigation to the selected movie
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie


    init {
        getMovies()
    }

    private fun getMovies() = coroutineScope.launch {
        if (pageCount <= 3)
            refreshDataFromRepository(pageCount)
        else {
            getDataFromWeb(pageCount)
        }
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

    fun refreshDataFromRepository(pageNumber: Int) {
        coroutineScope.launch {
            try {
                _status.value = MovieApiStatus.LOADING
                moviesRepository.refreshMovies(pageNumber)
                _status.value = MovieApiStatus.DONE
                pageCount = pageNumber.inc()
                repoMovies = moviesRepository.domMovies
                //update MovieList
                _movieList.value = repoMovies

                Timber.d("${_movieList.value?.size}")
                Timber.d("$pageCount")
                //increment page.. skip pages already in db
                pageCount = _movieList.value?.size?.div(20)!! + 1
            } catch (networkError: IOException) {
                _status.value = MovieApiStatus.ERROR
            }
        }
    }

    fun getDataFromWeb(pageNumber: Int) =
        coroutineScope.launch {
            try {
                _status.value = MovieApiStatus.LOADING
                val netResObject =
                    MovieApi.retrofitService.getMoviesAsync(page = pageNumber).await()
                webMovies =
                    NetworkMovieContainer(netResObject.results).asDomainModel()
                _status.value = MovieApiStatus.DONE
                _movieList.value = movieList.value!!.toList().plus(webMovies)
                pageCount = pageNumber.inc()
            } catch (networkError: IOException) {
                _status.value = MovieApiStatus.ERROR
            }
        }


    fun onLoadMoreMoviesClicked() {
        getMovies()
    }

    fun clearDB() = coroutineScope.launch {
        getDatabase(application).movieDao.deleteAll()
        Timber.d(getDatabase(application).movieDao.getMovies().toString())
        _movieList.value =
            getDatabase(application).movieDao.getMovies().asDomainModel()
        pageCount = 0
        _dbEmpty.value = true
    }

    fun dbCleared() {
        _dbEmpty.value = false
    }


    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
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