package com.example.themovieapp.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.view.get
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.CustomRecyclerViewTestUtil.Companion.withItemCount
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.R
import com.example.themovieapp.TestScope
import com.example.themovieapp.domain.Movie
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.apache.tools.ant.types.Assertions
import org.apache.tools.ant.types.Resource
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.lang.Thread.sleep
import java.util.concurrent.Callable


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
open class MovieListFragmentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var scenario: FragmentScenario<MovieListFragment>

    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)

    @Before
    fun setUp() = managedCoroutineScope.launch {
        scenario = launchFragmentInContainer<MovieListFragment>()
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

/*    @Test
    fun recyclerViewCountTest() {
        managedCoroutineScope.launch {
            onView(withId(R.id.movie_list))
                .check(ViewAssertions.matches(withItemCount(20)))

//            simulate button press and test again
            onView(withId(R.id.load_more_button)).perform(click())
                .check(ViewAssertions.matches((withItemCount(40))))
        }
    }*/

    @Test
    fun navToMovieDetailTest() {
        managedCoroutineScope.launch {
            val mockNavController = Mockito.mock(NavController::class.java)
            scenario.onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
            }


            onView(withId(R.id.movie_list))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, click()
                    )
                )
            scenario.onFragment { fragment ->
                Mockito.verify(mockNavController)
                    .navigate(
                        MovieListFragmentDirections
                            .actionMovieListFragmentToMovieDetailFragment(fragment.selectedMovie)
                    )
            }
        }
    }
    //TODO - some nav-back tests
}