package com.example.themovieapp.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.themovieapp.domain.Movie
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

/**
 * DatabaseMovie represents a video entity in the database.
 */
@Parcelize
@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val id: String,
    val title: String,
    val vote_average: Double,
    val poster_path: String?,
    val overview: String,
    val adult: Boolean,
    val release_date: String
) : Parcelable


/**
 * Map DatabaseMovies to domain entities
 */
fun List<DatabaseMovie>.asDomainModel(): List<Movie> {
    return map {
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