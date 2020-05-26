package com.example.themovieapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.themovieapp.R
import com.example.themovieapp.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {


    private lateinit var viewModel: HomeViewModel
    fun getViewModel(): HomeViewModel {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel


        //Observes change in LiveData, then performs navigation
        viewModel.navigateToSearch.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate->
                if(navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_homeFragment_to_movieListFragment)
                    viewModel.onNavigatedToSearch()
                }
            }
        )

        return binding.root
    }
}