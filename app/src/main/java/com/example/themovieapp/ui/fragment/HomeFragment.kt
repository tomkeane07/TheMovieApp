package com.example.themovieapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.themovieapp.R
import com.example.themovieapp.databinding.HomeFragmentBinding
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.ui.view.HomeViewModel
import com.example.themovieapp.ui.view.HomeViewModelFactory
import com.example.themovieapp.utils.LifecycleManagedCoroutineScope
import com.example.themovieapp.utils.MovieClickListener
import com.example.themovieapp.utils.MovieListAdapter
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    lateinit var selectedMovie: Movie
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = HomeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(
            this,
            HomeViewModelFactory(
                LifecycleManagedCoroutineScope(
                    lifecycleScope,
                    lifecycleScope.coroutineContext
                ), application
            )
        ).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        binding.searchByNameMovieList.adapter =
            MovieListAdapter(MovieClickListener { clickedMovie ->
                viewModel.displayMovieDetails(clickedMovie)
                this.findNavController().navigate(
                    R.id.action_home_to_movieDetailFragment,
                    bundleOf("selectedMovie" to clickedMovie)
                )
                selectedMovie = clickedMovie
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayMovieDetailsComplete()
            })


        //Observes change in LiveData, then performs navigation
        viewModel.navigateToSearch.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if (navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_homeFragment_to_movieListFragment)
                    viewModel.onNavigatedToSearch()
                }
            }
        )

        viewModel.searchByName.observe(viewLifecycleOwner,
            Observer<Boolean> {
                if (search_by_name_text.text.toString().length > 0)
                    viewModel.searchByName(search_by_name_text.text.toString())
            })
        

        return binding.root
    }

}

