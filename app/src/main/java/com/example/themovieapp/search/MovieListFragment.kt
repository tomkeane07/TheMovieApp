package com.example.themovieapp.search;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.utils.LifecycleManagedCoroutineScope
import com.example.themovieapp.R
import com.example.themovieapp.databinding.FragmentMovieListBinding
import com.example.themovieapp.db.DatabaseMovie
import com.example.themovieapp.db.asDomainModel
import com.example.themovieapp.domain.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


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
                findNavController().navigate(
                    R.id.action_movieListFragment_to_MovieDetailFragment,
                    bundleOf("selectedMovie" to it)
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


/*        val recyclerView: RecyclerView = getView()?.findViewById(R.id.movie_list) as RecyclerView
        recyclerView.getViewTreeObserver()
            .addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener() {
                override fun onGlobalLayout() {
                    //At this point the layout is complete and the
                    //dimensions of recyclerView and any child views are known.
                    //Remove listener after changed RecyclerView's height to prevent infinite loop

                }
            })*/

        setHasOptionsMenu(true)
        return binding.root
    }
}