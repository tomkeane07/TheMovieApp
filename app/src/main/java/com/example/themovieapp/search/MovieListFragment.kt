package com.example.themovieapp.search;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProviders;
import com.example.themovieapp.databinding.FragmentMovieListBinding


class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMovieListBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        return binding.root
    }
}