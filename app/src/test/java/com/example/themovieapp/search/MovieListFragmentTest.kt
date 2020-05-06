package com.example.themovieapp.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.CustomRecyclerViewTestUtil.Companion.withItemCount
import com.example.themovieapp.R
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
open class MovieListFragmentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var scenario: FragmentScenario<MovieListFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer<MovieListFragment>()
    }

    @After
    fun final() {
        scenario.onFragment { fragment ->
            println(
                fragment.activity?.findViewById<RecyclerView>(
                    R.id.movie_list
                )?.adapter?.itemCount!!
            )
        }
    }

    @Test
    fun recyclerViewCountTest() {
        sleep(5000)
        onView(withId(R.id.movie_list))
            .check(ViewAssertions.matches(withItemCount(20)))
        //simulate button press
        onView(withId(R.id.load_more_button)).perform(ViewActions.click())
        //test btn

/*        sleep(5000)

        scenario.onFragment { fragment ->

            await().atMost(30, TimeUnit.SECONDS).until(
                listResponse(
                    fragment.activity
                        ?.findViewById<RecyclerView>(R.id.movie_list)
                        ?.adapter?.itemCount!!,
                    40
                )
            )
    }*/
    }


//TODO - some nav tests


    open fun listResponse(
        //https://stackoverflow.com/questions/36556854/robolectric-and-retrofit-wait-for-response
        actualCount: Int,
        expectedCount: Int
    ): Callable<Boolean?>? {
        println("actualCount: $actualCount")
        return Callable {
            actualCount.equals(expectedCount)
        }
    }
}


/*
*   Using sleep() as a hamfisted way to wait for API
* response. may implement await funct in future,
* such as idling resource    */