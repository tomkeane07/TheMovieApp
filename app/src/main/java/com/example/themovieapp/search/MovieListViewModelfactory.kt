package com.example.themovieapp.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.ManagedCoroutineScope
import java.lang.IllegalArgumentException

class MovieListViewModelFactory(
    private val managedCoroutineScope: ManagedCoroutineScope
):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieListViewModel::class.java)){
            return MovieListViewModel(managedCoroutineScope) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}