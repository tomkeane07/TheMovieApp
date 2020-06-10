package com.example.themovieapp.testUtils

class SampleApiResponses {
    fun success(): String {
        return """{
                    "page":1,
                    "total_results":1,
                    "total_pages":1,
                    "results": [{"id": "movie_id", "title": "movie_title", "vote_average": 9.0,
                     "poster_path": "movie_poster_path", "overview": "overview", "adult": false,
                      "release_date": "release_date" }]
                }"""
    }
}