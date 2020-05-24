package com.example.themovieapp.UI

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.themovieapp.ManagedCoroutineScope
import com.example.themovieapp.R
import com.example.themovieapp.TestScope
import com.example.themovieapp.search.MovieListFragment
import com.example.themovieapp.search.MovieListFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@ExperimentalCoroutinesApi
class MovieListFragmentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var scenario: FragmentScenario<MovieListFragment>

    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)

    @Before
    fun setUp(){
        managedCoroutineScope.launch {
            scenario = launchFragmentInContainer<MovieListFragment>()
        }
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