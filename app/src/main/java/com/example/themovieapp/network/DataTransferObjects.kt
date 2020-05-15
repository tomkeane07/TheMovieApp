package com.example.themovieapp.network

import android.os.Parcelable
import com.example.themovieapp.db.DatabaseMovie
import com.example.themovieapp.domain.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val movies: List<NetworkMovie>)


@JsonClass(generateAdapter = true)
data class NetworkMovie(
    val id: String,
    val title: String,
    val vote_average: Double,
    @Json(name = "poster_path") val poster_path: String?,
    val overview: String,
    val adult: Boolean,
    val release_date: String
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



