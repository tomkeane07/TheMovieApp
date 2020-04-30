package com.example.themovieapp.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.themovieapp.databinding.MovieDetailFragmentBinding


/**
 * This [Fragment] shows the detailed information about a selected movie.
 * It sets this information in the [MovieDetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class MovieDetailFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = MovieDetailFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)


        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).selectedMovie
        binding.viewModel = ViewModelProviders.of(
            this, MovieDetailViewModelFactory(movie, application)
        ).get(MovieDetailViewModel::class.java)

        return binding.root
    }
}