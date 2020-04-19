package com.example.themovieapp.network

data class ResponseObject(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
)