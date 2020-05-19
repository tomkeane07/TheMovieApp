package com.example.themovieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.themovieapp.db.asDomainModel
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.db.MoviesDatabase
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.NetworkMovieContainer
import com.example.themovieapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Thread.sleep

/**
 * Repository for fetching movies from the network and storing them on disk
 */
class MoviesRepository(private val database: MoviesDatabase) {

    val movies: LiveData<List<Movie>> =
        Transformations.map(
            database.movieDao.getMovies()
        ) {
            it.asDomainModel()
        }

    /**
     * Refresh the movies stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun refreshMovies(pageNumber: Int) {
        withContext(Dispatchers.IO) {
            //movies = database.movieDao.getMovies().asDomainModel()
            Timber.d("refresh videos is called")
            val netResObject =
                MovieApi.retrofitService.getMoviesAsync(page = pageNumber).await()
            val movieList =
                NetworkMovieContainer(netResObject.results)

            movieList.asDatabaseModel().forEach {
                Timber.d(it.toString())
            }
            database.movieDao.insertAll(movieList.asDatabaseModel())
        }
    }
}