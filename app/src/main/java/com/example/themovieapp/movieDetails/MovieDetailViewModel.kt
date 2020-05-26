package com.example.themovieapp.movieDetails;

import android.app.Application
import androidx.lifecycle.*
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.network.MovieApi
import java.lang.IllegalArgumentException


class MovieDetailViewModel(
    val coroutineScope: ManagedCoroutineScope,
    movie: Movie, app: Application): AndroidViewModel(app) {

    // The internal MutableLiveData for the selected movie
    private val _selectedMovie = MutableLiveData<Movie>()

    // The external LiveData for the SelectedMovie
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie


    //initialize the _selectedMovie MutableLiveData
    init{
        _selectedMovie.value = movie
    }

    fun getRelated(movieId: Int) = coroutineScope.launch{
        val netResObject = MovieApi.retrofitService.getRelatedMoviesAsync(movie_id = movieId).await()
    }


    // VIEW CONTENT

}

class MovieDetailViewModelFactory(
    private val managedCoroutineScope: ManagedCoroutineScope,
    private val movie: Movie,
    private val application: Application):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailViewModel::class.java)){
            return MovieDetailViewModel(managedCoroutineScope, movie, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}