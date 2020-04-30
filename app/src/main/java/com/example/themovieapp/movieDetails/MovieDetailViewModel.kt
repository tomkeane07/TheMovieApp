package com.example.themovieapp.movieDetails;

import android.app.Application
import androidx.lifecycle.*
import com.example.themovieapp.network.Movie


class MovieDetailViewModel(
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


    // VIEW CONTENT

}