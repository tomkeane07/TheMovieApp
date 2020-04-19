package com.example.themovieapp.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapp.network.Movie
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.ResponseObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel: ViewModel() {
    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response


    init{
        Log.d("list", "in init")
        getMovieProperties()
    }

    private fun getMovieProperties() {
        MovieApi.retrofitService.getMovies(page = 1).enqueue(
            object: Callback<ResponseObject> {
                override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                    Log.d("list", "failure ${_response.value}")
                }

                override fun onResponse(call: Call<ResponseObject>, response: Response<ResponseObject>) {
                    _response.value = "Success: ${response.body()?.results?.size} movies found"
                    Log.d("list", "response ${response}")
                }
            })
    }
}