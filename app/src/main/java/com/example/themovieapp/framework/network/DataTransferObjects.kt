package com.example.themovieapp.framework.network

import com.example.themovieapp.framework.db.DatabaseMovie
import com.example.themovieapp.framework.domain.Movie
import com.squareup.moshi.JsonClass

data class NetworkResponseObject(
    val page: Int,
    val results: List<NetworkMovie>,
    val total_results: Int,
    val total_pages: Int
)


@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val movies: List<NetworkMovie>)


@JsonClass(generateAdapter = true)
data class NetworkMovie(
    val id: String,
    val title: String,
    val vote_average: Double,
    val poster_path: String?,
    val overview: String = "",
    val adult: Boolean,
    val release_date: String = ""
)

/**
 * Convert Network results to domain objects
 */
fun NetworkMovieContainer.asDomainModel(): List<Movie> {
    return movies.map {
        Movie(
            id = it.id,
            title = it.title,
            vote_average = it.vote_average,
            poster_path = it.poster_path,
            overview = it.overview,
            adult = it.adult,
            release_date = it.release_date
        )
    }
}

/**
 * Convert Network results to database objects
 */
fun NetworkMovieContainer.asDatabaseModel(): List<DatabaseMovie> {
    return movies.map {
        DatabaseMovie(
            id = it.id,
            title = it.title,
            vote_average = it.vote_average,
            poster_path = it.poster_path,
            overview = it.overview,
            adult = it.adult,
            release_date = it.release_date
        )
    }
}



