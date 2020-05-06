package com.example.themovieapp.movieDetails

import android.os.Bundle
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.R
import com.example.themovieapp.network.Movie
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.LooperMode.Mode.PAUSED


@RunWith(AndroidJUnit4::class)
@LooperMode(PAUSED)
class MovieDetailFragmentContentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var movie: Movie
    lateinit var scenario: FragmentScenario<MovieDetailFragment>

    @Before
    fun setUp() {
        movie = setMovieSample()
        genericDetailTestSetup(movie)
    }

    @Test
    fun testMovieDetailText() {

        onView(withId(R.id.movie_title))
            .check(matches(withText(movie.title)))
        onView(withId(R.id.movie_overview))
            .check(matches(withText(movie.overview)))
        onView(withId(R.id.movie_vote_average))
            .check(matches(withText(movie.vote_average.toString())))
        onView(withId(R.id.movie_release_date))
            .check(matches(withText(movie.release_date)))
    }

    fun setMovieSample(): Movie {
        val movieSample = Movie(
            "346",
            "Seven Samurai",
            9.0,
            "/8OKmBV5BUFzmozIC3pPWKHy17kx.jpg",
            "A samurai answers a village's request for protection after he falls on hard times. The town needs protection from bandits, so the samurai gathers six others to help him teach the people how to defend themselves, and the villagers provide the soldiers with food. A giant battle occurs when 40 bandits attack the village.",
            false,
            "1954-04-26"
        )
        return movieSample
    }

    fun genericDetailTestSetup(movie: Movie) {
        val fragmentArgs = Bundle().apply {
            putAll(
                bundleOf(
                    "selectedMovie" to movie
                )
            )
        }

        scenario = launchFragmentInContainer<MovieDetailFragment>(
            fragmentArgs
        )

    }

}

@RunWith(AndroidJUnit4::class)
@LooperMode(PAUSED)
class MovieDetailFragmentRatingColorTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var movie: Movie
    lateinit var scenario: FragmentScenario<MovieDetailFragment>


}




