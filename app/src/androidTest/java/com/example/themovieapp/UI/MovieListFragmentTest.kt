package com.example.themovieapp.UI

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.R
import com.example.themovieapp.utils.TestScope
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
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class MovieListFragmentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var scenario: FragmentScenario<MovieListFragment>
    lateinit var context: Context

    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope =
        TestScope(testDispatcher)

    @Before
    fun setUp() {
        managedCoroutineScope.launch {
            scenario = launchFragmentInContainer<MovieListFragment>()
            context = ApplicationProvider.getApplicationContext<Context>()
        }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

/* returning true every time
    @Test
    fun recyclerViewCountTest() {
        managedCoroutineScope.launch {
            val db = getDatabase(context)
            val initSize = db.movieDao.getMovies().size

            onView(withId(R.id.load_more_button))
                .check(RecyclerViewItemCountAssertion(initSize + 20))

//            simulate button press and test again
            onView(withId(R.id.load_more_button)).perform(click())
                .check(RecyclerViewItemCountAssertion(initSize + 20))
        }
    }
*/

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

/*
class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, `is`(expectedCount))
    }

}*/