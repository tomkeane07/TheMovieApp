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
    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    private val _movieList = MutableLiveData<List<Movie>>()
    private val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _movie = MutableLiveData<Movie>()

    val movie: LiveData<Movie>
        get() = _movie


    // allows easy update of the value of the MutableLiveData
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )


    init{
        Log.d("list", "in init")
        getMovieProperties()
    }


    private fun getMovieProperties() {

        coroutineScope.launch{
            var getMoviePropertiesDeferred = MovieApi.retrofitService.getMovies(page = 1)
            try {

                var responseObject = getMoviePropertiesDeferred.await()
                if(responseObject.results.size > 0 ){
                    //_movie.value = responseObject.results[0]
                    _movieList.value = responseObject.results
                }
                Log.d("res", "success")
            } catch (e: Exception) {

                _response.value = "Failure: ${e.message}"
                Log.d("res", "fail")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}