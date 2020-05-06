package com.example.themovieapp.search;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.themovieapp.R
import com.example.themovieapp.databinding.FragmentMovieListBinding


class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    MovieListFragmentDirections
                        .actionMovieListFragmentToMovieDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayMovieDetailsComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }



//        fun RecyclerView.onScrollToEnd(linearLayoutManager: LinearLayoutManager, onScrollNearEnd: (Unit) -> Unit)
//            = addOnScrollListener(object : RecyclerView.OnScrollListener() {
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            if (linearLayoutManager.childCount + linearLayoutManager.findFirstVisibleItemPosition()
//                >= linearLayoutManager.itemCount - 5) {  //if near fifth item from end
//                onScrollNearEnd(Unit)
//                Log.d("scroll Listener","near end")
//            }
//        }
//    })
}