package com.example.themovieapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.net.HttpURLConnection

/*
https://stackoverflow.com/questions/61636331/testing-a-retrofit-moshi-apiservice-which-returns-a-deferred-object
*/
class MovieApiServiceTest {
    private var mockWebServer = MockWebServer()

    private lateinit var apiService: MovieApiService

    @Before
    fun setUp() {
        // checkthis blogpost for more details about mock server
        // https://medium.com/@hanru.yeh/unit-test-retrofit-and-mockwebserver-a3e4e81fd2a2
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(mockWebServer.url("/")) // note the URL is different from production one
            .build()
            .create(MovieApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testCompleteIntegration() = runBlocking { // that will allow to wait for coroutine
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(
                    """{
                    "page":0,
                    "total_results":1,
                    "total_pages":1,
                    "results": [{"id": "movie_id", "title": "movie_title", "vote_average": 9.0,
                     "poster_path": "movie_poster_path", "overview": "overview", "adult": false,
                      "release_date": "release_date" }]
                }"""
                )
        )

        val response = apiService.getMoviesAsync(page = 1).await()
        Timber.d(response.results.toString())

        assertEquals(0, response.page)
        assertEquals(1, response.total_results)
        assertEquals(1, response.total_pages)
        assertEquals("movie_id", response.results.first().id)
    }
}