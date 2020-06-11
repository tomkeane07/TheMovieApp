package com.example.themovieapp.UI

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.SystemClock.sleep
import android.view.View
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.util.Checks
import com.example.themovieapp.R
import com.example.themovieapp.framework.domain.Movie
import com.example.themovieapp.ui.fragment.MovieDetailFragment
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MovieDetailFragmentContentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var movie: Movie
    lateinit var scenario: FragmentScenario<MovieDetailFragment>
    lateinit var navController: TestNavHostController


    @Before
    fun setUp() {
        movie = setMovieSample(9.0)
        genericDetailTestSetup(movie)
    }

    @After
    fun close() {

    }

    @Test
    fun testMovieDetailText() {
        // is fragment displaying movie details
        onView(withId(R.id.movie_title))
            .check(matches(withText(movie.title)))
        onView(withId(R.id.movie_overview))
            .check(matches(withText(movie.overview)))
        onView(withId(R.id.movie_vote_average))
            .check(matches(withText(movie.vote_average.toString())))
        onView(withId(R.id.movie_release_date))
            .check(matches(withText(movie.release_date)))
    }

    @Test
    fun MovieDetailRatingColorTest() {
        //is movie rating color correct

        //for Equivalence Partitions
        val sampleRatings = arrayOf(4.0, 6.0, 7.0, 9.0)
        var expectedColor: Int? = 0

        sampleRatings.forEach {
            when {
                5 <= it && it < 6.5 -> {
                    movie = setMovieSample(it)
                    genericDetailTestSetup(movie)

                    scenario.onFragment { fragment ->
                        expectedColor =
                            fragment.context?.let {
                                ContextCompat.getColor(it, R.color.ColorRatingMediumLow)
                            }
                    }
                }

                6.5 <= it && it < 8 -> {
                    movie = setMovieSample(it)
                    genericDetailTestSetup(movie)

                    scenario.onFragment { fragment ->
                        expectedColor =
                            fragment.context?.let {
                                ContextCompat.getColor(it, R.color.ColorRatingMediumHigh)
                            }
                    }
                }


                8 <= it -> {
                    movie = setMovieSample(it)
                    genericDetailTestSetup(movie)

                    scenario.onFragment { fragment ->
                        expectedColor =
                            fragment.context?.let {
                                ContextCompat.getColor(it, R.color.ColorRatingHigh)
                            }
                    }
                }
                else -> {
                    movie = setMovieSample(it)
                    genericDetailTestSetup(movie)

                    scenario.onFragment { fragment ->
                        expectedColor =
                            fragment.context?.let {
                                ContextCompat.getColor(it, R.color.ColorRatingLow)
                            }
                    }
                }

            }
            onView(withId(R.id.movie_vote_average))
                .check(matches(withBackgroundColor(expectedColor)))

        }
    }

    @Test
    fun navToRecommendedMovieTest() {

        sleep(1000)
        //Setup
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.movieDetailFragment)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.see_recommendations_btn))
            .perform(click())

        //Test
        onView(withId(R.id.movie_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2, ViewActions.click()
                )
            )

        ViewMatchers.assertThat(
            navController.currentDestination?.id,
            CoreMatchers.equalTo(R.id.movieDetailFragment)
        )
    }


    fun setMovieSample(vote_average: Double): Movie {
        return Movie(
            "346",
            "Seven Samurai",
            vote_average,
            "/8OKmBV5BUFzmozIC3pPWKHy17kx.jpg",
            "A samurai answers a village's request for protection after he falls on hard times." +
                    " The town needs protection from bandits, so the samurai gathers six others to" +
                    " help him teach the people how to defend themselves, and the villagers provide the soldiers with food." +
                    " A giant battle occurs when 40 bandits attack the village.",
            false,
            "1954-04-26"
        )
    }


    fun genericDetailTestSetup(movie: Movie) {
        val fragmentArgs = Bundle().apply {
            putAll(
                bundleOf(
                    "selectedMovie" to movie
                )
            )
        }


        runBlocking {
            scenario = launchFragmentInContainer<MovieDetailFragment>(
                fragmentArgs
            )
        }

    }

    fun withBackgroundColor(expectedColor: Int?): Matcher<View?>? {
        Checks.checkNotNull(expectedColor)
        return object : BoundedMatcher<View?, TextView>(TextView::class.java) {
            override fun matchesSafely(textView: TextView): Boolean {

                val actualColor = (textView.background as GradientDrawable).color?.defaultColor
                return expectedColor == actualColor
            }

            override fun describeTo(description: Description) {
                description.appendText("withBackgroundColor for movieRating: ${movie.vote_average}")
            }
        }
    }

}