package com.example.themovieapp.movieDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.domain.Movie
import java.lang.IllegalArgumentException

class MovieDetailViewModelFactory(
    private val movie: Movie,
    private val application: Application):
        ViewModelProvider.Factory{
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailViewModel::class.java)){
            return MovieDetailViewModel(movie, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}