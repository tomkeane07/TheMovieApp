package com.example.themovieapp.ui.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.framework.network.MovieApi
import com.example.themovieapp.framework.network.NetworkMovieContainer
import com.example.themovieapp.framework.network.asDomainModel
import com.example.themovieapp.utils.ManagedCoroutineScope
import kotlinx.coroutines.CoroutineScope
import java.io.IOException
import java.lang.IllegalArgumentException
import kotlin.coroutines.coroutineContext

class HomeViewModel(
    val coroutineScope: ManagedCoroutineScope, app: Application
) : ViewModel() {
    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    fun onSeeMoviesClicked() {
        _navigateToSearch.value = true
    }

    fun onNavigatedToSearch() {
        _navigateToSearch.value = false
    }


    val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _status = MutableLiveData<MovieApiStatus>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    fun searchByName(name: String) = coroutineScope.launch {
        try {
            _status.value = MovieApiStatus.LOADING
            val netResObject =
                MovieApi.retrofitService.searchByName(query = name).await()
            _status.value = MovieApiStatus.DONE
            _movieList.value = NetworkMovieContainer(netResObject.results).asDomainModel()
        } catch (networkError: IOException) {
            _status.value = MovieApiStatus.ERROR
        }
    }

}

class HomeViewModelFactory(
    private val managedCoroutineScope: ManagedCoroutineScope,
    private val application: Application
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                managedCoroutineScope,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}