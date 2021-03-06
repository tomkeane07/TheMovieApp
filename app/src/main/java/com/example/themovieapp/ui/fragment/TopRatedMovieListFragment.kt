package com.example.themovieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.utils.LifecycleManagedCoroutineScope
import com.example.themovieapp.R
import com.example.themovieapp.databinding.FragmentTopRatedMovieListBinding
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.ui.adapters.MovieClickListener
import com.example.themovieapp.ui.adapters.MovieListAdapter
import com.example.themovieapp.ui.view.TopRatedMovieListViewModel
import com.example.themovieapp.ui.view.MovieListViewModelFactory


class TopRatedMovieListFragment : Fragment() {

    //using this var for fragmentTesting purposes
    lateinit var selectedMovie: Movie
    lateinit var viewModelTopRated: TopRatedMovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        viewModelTopRated = ViewModelProviders.of(
            this,
            MovieListViewModelFactory(
                LifecycleManagedCoroutineScope(
                    lifecycleScope,
                    lifecycleScope.coroutineContext
                ),
                application
            )
        ).get(TopRatedMovieListViewModel::class.java)

        val binding = FragmentTopRatedMovieListBinding.inflate(inflater)

        //val binding = GridViewItemBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the TopRatedMovieListViewModel
        binding.viewModel = viewModelTopRated

        // Sets the adapter of the RecyclerView with clickHandler lambda that
        // tells the viewModelTopRated when our movie is clicked
        binding.movieList.adapter =
            MovieListAdapter(
                MovieClickListener { clickedMovie ->
                    findNavController().navigate(
                        R.id.action_movieListFragment_to_MovieDetailFragment,
                        bundleOf("selectedMovie" to clickedMovie)
                    )
                    selectedMovie = clickedMovie
                })


        //Observes change in LiveData, then performs navigation
        viewModelTopRated.dbEmpty.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if (navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_movieSearch_to_home)
                    viewModelTopRated.dbCleared()
                }
            }
        )

        //Scroll to bottom on list update
        binding.movieList.layoutManager =
            object : LinearLayoutManager(activity, VERTICAL, false) {
                override fun onLayoutCompleted(state: RecyclerView.State) {
                    super.onLayoutCompleted(state)
                    findLastVisibleItemPosition()
                    val count = (binding.movieList.adapter as MovieListAdapter).itemCount
                    if (!viewModelTopRated.atStart) {
                        //speed the scroll up a bit, but still looks nice
                        binding.movieList.scrollToPosition(count - 5)
                        binding.movieList.smoothScrollToPosition(count)
                    }
                }
            }


        setHasOptionsMenu(true)
        return binding.root
    }
}