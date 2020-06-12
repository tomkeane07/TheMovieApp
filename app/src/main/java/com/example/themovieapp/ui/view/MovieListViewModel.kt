package com.example.themovieapp.ui.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.framework.db.getDatabase
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.framework.repository.MoviesRepository
import com.example.themovieapp.utils.ManagedCoroutineScope
import kotlinx.coroutines.cancel
import java.io.IOException

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieListViewModel(
    val coroutineScope: ManagedCoroutineScope,
    val application: Application
) : ViewModel() {
    var pageCount = 1

    private val _status = MutableLiveData<MovieApiStatus>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    private val _dbEmpty = MutableLiveData<Boolean>()
    val dbEmpty: LiveData<Boolean>
        get() = _dbEmpty

    //helps decide scroll logic
    var atStart: Boolean = true

    /**
     * The data source this ViewModel will fetch results from.
     */
    val db = getDatabase(application.applicationContext)

    private val moviesRepository = MoviesRepository(db)


    val movieList = moviesRepository.movies


    init {
        getMovies()
    }

    private fun getMovies() = coroutineScope.launch {
        refreshDataFromRepository(pageCount)
    }



    fun refreshDataFromRepository(pageNumber: Int) {
        coroutineScope.launch {
            try {
                _status.value =
                    MovieApiStatus.LOADING
                moviesRepository.refreshMovies(pageNumber)
                _status.value =
                    MovieApiStatus.DONE
            } catch (networkError: IOException) {
                _status.value =
                    MovieApiStatus.ERROR
            }
        }
    }


    fun onLoadMoreMoviesClicked() {
        atStart = false
        if (!movieList.value.isNullOrEmpty()) {
            pageCount = movieList.value!!.size / 20 + 1
        }
        getMovies()
        pageCount.inc()
    }

    fun clearDB() = coroutineScope.launch {
        getDatabase(application).movieDao.deleteAll()
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
            return MovieListViewModel(
                managedCoroutineScope,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}