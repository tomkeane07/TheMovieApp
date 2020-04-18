package com.example.themovieapp.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel: ViewModel() {
    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response


    init{
        Log.d("list", "in init")
    }

}