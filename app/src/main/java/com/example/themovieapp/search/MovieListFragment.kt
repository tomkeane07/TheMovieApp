package com.example.themovieapp.search;

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProviders;
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

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.movieList.adapter = MovieListAdapter()

        setHasOptionsMenu(true)
        return binding.root
    }



//      Inflates the overflow menu that contains filtering options.

/*    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/
}