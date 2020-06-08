package com.example.themovieapp.movieDetails;

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.*
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.network.MovieApi
import com.example.themovieapp.network.MovieApiService
import com.example.themovieapp.network.NetworkMovieContainer
import com.example.themovieapp.network.asDomainModel
import com.example.themovieapp.search.MovieApiStatus
import java.io.IOException
import java.lang.IllegalArgumentException

enum class MovieApiStatus { LOADING, ERROR, DONE }

class MovieDetailViewModel(
    val coroutineScope: ManagedCoroutineScope,
    movie: Movie, app: Application
) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected movie
    private val _selectedMovie = MutableLiveData<Movie>()

    // The external LiveData for the SelectedMovie
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _status = MutableLiveData<MovieApiStatus>()
    val status: LiveData<MovieApiStatus>
        get() = _status

    private val _recommendedMovies = MutableLiveData<List<Movie>>()
    val recommendedMovies: LiveData<List<Movie>>
        get() = _recommendedMovies

    // LiveData to handle navigation to the selected movie
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    private val _viewRecommendations = MutableLiveData<Boolean>(false)
    val viewRecommendations: LiveData<Boolean>
        get() = _viewRecommendations

    //initialize the _selectedMovie MutableLiveData
    init {
        _selectedMovie.value = movie
        getRelated(movie.id)
    }

    fun getRelated(movieId: String) = coroutineScope.launch {
        try {
            _status.value = MovieApiStatus.LOADING
            val netResObject =
                MovieApi.retrofitService.getRelatedMoviesAsync(movie_id = movieId).await()
            _status.value = MovieApiStatus.DONE
            _recommendedMovies.value = NetworkMovieContainer(netResObject.results).asDomainModel()
        } catch (networkError: IOException) {
            _status.value = MovieApiStatus.ERROR
        }
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

    fun recommendationClick(){
        _viewRecommendations.value = !viewRecommendations.value!!
    }

/*    fun rateMovieClick(){
        coroutineScope.launch{
            val accessToken = MovieApi.retrofitService.getAccessToken()



        }
    }*/
}

class MovieDetailViewModelFactory(
    private val managedCoroutineScope: ManagedCoroutineScope,
    private val movie: Movie,
    private val application: Application
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(managedCoroutineScope, movie, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}