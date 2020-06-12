package com.example.themovieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.themovieapp.databinding.MovieDetailFragmentBinding
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.ui.view.MovieDetailViewModel
import com.example.themovieapp.ui.view.MovieDetailViewModelFactory
import com.example.themovieapp.ui.adapters.MovieClickListener
import com.example.themovieapp.ui.adapters.MovieListAdapter
import com.example.themovieapp.utils.LifecycleManagedCoroutineScope
import kotlinx.android.synthetic.main.movie_detail_fragment.*


/**
 * This [Fragment] shows the detailed information about a selected movie.
 * It sets this information in the [MovieDetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class MovieDetailFragment : Fragment() {

    lateinit var selectedMovie: Movie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val binding = MovieDetailFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val movie = MovieDetailFragmentArgs.fromBundle(
            requireArguments()
        ).selectedMovie
        val viewModel = ViewModelProviders.of(
            this,
            MovieDetailViewModelFactory(
                LifecycleManagedCoroutineScope(
                    lifecycleScope,
                    lifecycleScope.coroutineContext
                ),
                movie, application
            )
        ).get(MovieDetailViewModel::class.java)

        binding.viewModel = viewModel

        binding.movieList.adapter =
            MovieListAdapter(
                MovieClickListener { clickedMovie ->
                    this.findNavController().navigate(
                        MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(
                            clickedMovie
                        )
                    )
                    selectedMovie = clickedMovie
                })


        viewModel.viewRecommendations.observe(viewLifecycleOwner, Observer {
            if (it) {
                Details.visibility = View.GONE
                Recommendations.visibility = View.VISIBLE
                see_details_btn.text = movie.title.capitalize()
            } else {
                Details.visibility = View.VISIBLE
                Recommendations.visibility = View.GONE
            }

        })

        return binding.root
    }
}