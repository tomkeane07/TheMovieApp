package com.example.themovieapp.search;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.themovieapp.utils.LifecycleManagedCoroutineScope
import com.example.themovieapp.R
import com.example.themovieapp.databinding.FragmentMovieListBinding
import com.example.themovieapp.domain.Movie


class MovieListFragment : Fragment() {

    //using this var for fragmentTesting purposes
    lateinit var selectedMovie: Movie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val viewModel = ViewModelProviders.of(
            this,
            MovieListViewModelFactory(
                LifecycleManagedCoroutineScope(
                    lifecycleScope,
                    lifecycleScope.coroutineContext
                ),
                application
            )
        ).get(MovieListViewModel::class.java)

        val binding = FragmentMovieListBinding.inflate(inflater)

        //val binding = GridViewItemBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the MovieListViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the RecyclerView with clickHandler lambda that
        // tells the viewModel when our movie is clicked
        binding.movieList.adapter = MovieListAdapter(MovieClickListener { movie ->
            viewModel.displayMovieDetails(movie)
            selectedMovie = movie
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    MovieListFragmentDirections
                        .actionMovieListFragmentToMovieDetailFragment(it)
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayMovieDetailsComplete()
            }
        })

        //Observes change in LiveData, then performs navigation
        viewModel.dbEmpty.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if (navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_movieSearch_to_home)
                    viewModel.dbCleared()
                }
            }
        )

        setHasOptionsMenu(true)
        return binding.root
    }
}