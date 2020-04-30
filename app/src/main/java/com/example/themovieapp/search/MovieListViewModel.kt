package com.example.themovieapp.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapp.network.Movie
import com.example.themovieapp.network.MovieApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieListViewModel: ViewModel() {

    private var pageCount = 1

    private val _movieList = MutableLiveData<List<Movie>>()

    val movieList: LiveData<List<Movie>>
        get() = _movieList


    // LiveData to handle navigation to the selected movie
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie



    // allows easy update of the value of the MutableLiveData
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )


    init{
        Log.d("list", "in init")
            pageCount = getMovies(pageCount)
    }


    private fun getMovies(pageNumber: Int): Int {

        coroutineScope.launch{
            val getMoviesDeferred = MovieApi.retrofitService.getMovies(page = pageNumber)
            try {

                val responseObject = getMoviesDeferred.await()
                if(responseObject.results.size > 0 ){
                    //_movie.value = responseObject.results[0]
                    if(_movieList.value!=null){
                        val unsortedList: List<Movie> = movieList.value!!.toList()
                        _movieList.value =  unsortedList.plus(responseObject.results).sortedByDescending { it.vote_average }

                    }
                    else{_movieList.value = responseObject.results}
                }
                Log.d("getMovies", "success: ${movieList.value!!.size} movies found")
            } catch (e: Exception) {
                _movieList.value = ArrayList()
                Log.d("getMovies", "failed to get Movies ${e.message}")
            }

        }
        return pageNumber.inc()
    }


    fun onLoadMoreMoviesClicked(){
        pageCount = getMovies(pageCount)
    }



    /**
     * When the movie is clicked, set the [_navigateToSelectedMovie] [MutableLiveData]
     * @param movie The [Movie] that was clicked on.
     */
    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }


    /**
     * After the navigation has taken place, make sure navigateToSelectedMovie is set to null
     */
    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }





    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}