package com.example.themovieapp.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class ResponseObject(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
)


//API info: https://developers.themoviedb.org/3/movies/get-top-rated-movies
@Parcelize
data class Movie(
    val id: String,
    val title: String,
    val vote_average: Double,
    @Json(name = "poster_path") val poster_path: String?,
    val overview: String,
    val adult: Boolean,
    val release_date: String
) : Parcelable