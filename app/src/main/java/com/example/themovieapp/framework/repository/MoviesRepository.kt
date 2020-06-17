package com.example.themovieapp.framework.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.themovieapp.framework.db.asDomainModel
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.framework.db.MoviesDatabase
import com.example.themovieapp.framework.db.getDatabase
import com.example.themovieapp.framework.network.MovieApi
import com.example.themovieapp.framework.network.NetworkMovieContainer
import com.example.themovieapp.framework.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        database.movieDao.deleteAll()
    }
}