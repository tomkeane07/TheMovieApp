package com.example.themovieapp.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapp.network.Movie
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.ResponseObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel: ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()

    val movieList: LiveData<List<Movie>>
        get() = _movieList


/*    private val _movie = MutableLiveData<Movie>()

    val movie: LiveData<Movie>
        get() = _movie*/
    

    // allows easy update of the value of the MutableLiveData
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )


    init{
        Log.d("list", "in init")
        getMovies()
    }


    private fun getMovies() {

        coroutineScope.launch{
            var getMoviesDeferred = MovieApi.retrofitService.getMovies(page = 1)
            try {

                var responseObject = getMoviesDeferred.await()
                if(responseObject.results.size > 0 ){
                    //_movie.value = responseObject.results[0]
                    _movieList.value = responseObject.results
                }
                Log.d("getMovies", "success: ${responseObject.results.size} movies found")
            } catch (e: Exception) {
                _movieList.value = ArrayList()
                Log.d("getMovies", "failed to get Movies ${e.message}")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}