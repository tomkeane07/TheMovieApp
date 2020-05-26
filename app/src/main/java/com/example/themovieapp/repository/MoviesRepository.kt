package com.example.themovieapp.repository

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.themovieapp.db.DatabaseMovie
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


    lateinit var domMovies: List<Movie>

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
            val netMovies =
                NetworkMovieContainer(netResObject.results)
            database.movieDao.insertAll(netMovies.asDatabaseModel())

            domMovies =
                database.movieDao.getMovies().asDomainModel()
                    .sortedByDescending { it.vote_average }
            return@withContext domMovies
        }
    }
}