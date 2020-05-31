package com.example.themovieapp.repository

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.themovieapp.db.DatabaseMovie
import com.example.themovieapp.db.asDomainModel
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.db.MoviesDatabase
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.NetworkMovieContainer
import com.example.themovieapp.network.asDatabaseModel
import com.example.themovieapp.utils.ManagedCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.lang.Thread.sleep
import kotlin.coroutines.coroutineContext

/**
 * Repository for fetching movies from the network and storing them on disk
 */
class MoviesRepository(
    private val database: MoviesDatabase
) {
    val movies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies()) {
            it.asDomainModel()
        }

    suspend fun refreshMovies(pageNumber: Int) = withContext(Dispatchers.IO) {
        val netResObject =
            MovieApi.retrofitService.getMoviesAsync(page = pageNumber).await()
        val netMovies =
            NetworkMovieContainer(netResObject.results)
        database.movieDao.insertAll(netMovies.asDatabaseModel())
    }
}