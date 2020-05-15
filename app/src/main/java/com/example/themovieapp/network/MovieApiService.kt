package com.example.themovieapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_key  = "42a09a435ec64d8fa772179fcb739f91"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()


interface MovieApiService{
//https://developers.themoviedb.org/3/movies/get-top-rated-movies
//https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Query.html
    @GET("movie/top_rated")
    fun getMoviesAsync(
        @Query("api_key") apiKey: String = API_key,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Deferred<NetworkMovieContainer>
}


/*
Because this call is expensive, and the app only needs
one Retrofit service instance, you expose the service to the rest of the app using
a public object called MovieApi, and lazily initialize the Retrofit service there
*/
object MovieApi {
    val retrofitService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}