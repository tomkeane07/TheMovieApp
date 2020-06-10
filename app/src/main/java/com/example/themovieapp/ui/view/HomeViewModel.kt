package com.example.themovieapp.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    fun onSeeMoviesClicked(){
        _navigateToSearch.value = true
    }

    fun onNavigatedToSearch() {
        _navigateToSearch.value = false
    }
}