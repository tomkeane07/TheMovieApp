package com.example.themovieapp.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

//API info: https://developers.themoviedb.org/3/movies/get-top-rated-movies
@Parcelize
data class Movie(
    val id: String,
    val title: String,
    val vote_average: Double,
    val poster_path: String?,
    val overview: String,
    val adult: Boolean,
    val release_date: String
) : Parcelable