package com.example.themovieapp.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.themovieapp.databinding.MovieDetailFragmentBinding
import com.example.themovieapp.domain.Movie
import com.example.themovieapp.search.MovieClickListener
import com.example.themovieapp.search.MovieListAdapter
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
        binding.setLifecycleOwner(this)


        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).selectedMovie
        val viewModel = ViewModelProviders.of(
            this, MovieDetailViewModelFactory(
                LifecycleManagedCoroutineScope(
                    lifecycleScope,
                    lifecycleScope.coroutineContext
                ),
                movie, application
            )
        ).get(MovieDetailViewModel::class.java)

        binding.viewModel = viewModel

        binding.movieList.adapter = MovieListAdapter(MovieClickListener { clickedMovie ->
            viewModel.displayMovieDetails(clickedMovie)
            selectedMovie = clickedMovie
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    MovieDetailFragmentDirections
                        .actionMovieDetailFragmentSelf(it)
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayMovieDetailsComplete()
            }
        })

        viewModel.viewRecommendations.observe(viewLifecycleOwner, Observer {
            if (it) {
                Details.visibility = View.GONE
                Recommendations.visibility = View.VISIBLE
                detailBtn.setText(movie.title.capitalize())
            } else {
                Details.visibility = View.VISIBLE
                Recommendations.visibility = View.GONE
            }

        })

        return binding.root
    }
}