package com.example.themovieapp.db

import androidx.room.PrimaryKey
import com.example.themovieapp.domain.Movie
import com.squareup.moshi.Json

data class DatabaseMovie constructor(
    @PrimaryKey
    val id: String,
    val title: String,
    val vote_average: Double,
    @Json(name = "poster_path") val poster_path: String?,
    val overview: String,
    val adult: Boolean,
    val release_date: String
)

fun List<DatabaseMovie>.asDomainModel(): List<Movie>{
    return map{
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