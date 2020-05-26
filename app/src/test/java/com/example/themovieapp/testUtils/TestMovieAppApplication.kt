package com.example.themovieapp.testUtils

import com.example.themovieapp.MovieAppApplication

class TestMovieAppApplication: MovieAppApplication() {
    protected override fun isUnitTesting(): Boolean {
        return true
    }
}
//may use this to suppress "socket not created issues"..
// have to set test application() somehow