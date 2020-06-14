package com.example.themovieapp.framework.network

import com.example.themovieapp.testUtils.SampleApiResponses
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
                    SampleApiResponses().success()
                )
        )

        val response = apiService.getMoviesAsync(page = 1).await()
        Timber.d(response.results.toString())

        assertEquals(1, response.page)
        assertEquals(1, response.total_results)
        assertEquals(1, response.total_pages)
        assertEquals("movie_id", response.results.first().id)
    }
}