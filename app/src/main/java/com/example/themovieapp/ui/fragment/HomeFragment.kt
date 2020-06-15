package com.example.themovieapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.example.themovieapp.ui.adapters.MovieClickListener
import com.example.themovieapp.ui.adapters.MovieListAdapter
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
            MovieListAdapter(
                MovieClickListener { clickedMovie ->
                    this.findNavController().navigate(
                        R.id.action_home_to_movieDetailFragment,
                        bundleOf("selectedMovie" to clickedMovie)
                    )
                    selectedMovie = clickedMovie
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



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        search_by_name_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (search_by_name_text.text.toString().length > 0) {
                    viewModel.searchByName(search_by_name_text.text.toString())
                } else{
                    viewModel._movieList.value = listOf()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        search_by_name_clear_button.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v:View) {
                search_by_name_text.text.clear()
                viewModel._movieList.value = listOf()
            }
        })
    }

}

