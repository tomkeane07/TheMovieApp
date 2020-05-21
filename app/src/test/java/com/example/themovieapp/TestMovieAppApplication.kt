package com.example.themovieapp

class TestMovieAppApplication: MovieAppApplication() {
    protected override fun isUnitTesting(): Boolean {
        return true
    }
}
//may use this to suppress "socket not created issues"..
// have to set test application() somehow