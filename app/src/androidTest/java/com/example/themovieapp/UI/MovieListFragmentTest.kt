package com.example.themovieapp.UI

import android.content.Context
import android.os.SystemClock.sleep
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.themovieapp.R
import com.example.themovieapp.ui.fragment.MovieListFragment
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class MovieListFragmentTest {


    lateinit var scenario: FragmentScenario<MovieListFragment>
    lateinit var context: Context
    lateinit var navController: TestNavHostController

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope =
        TestScope(testDispatcher)

    @Before
    fun setUp() {
        runBlocking {
            scenario = launchFragmentInContainer<MovieListFragment>()
            context = ApplicationProvider.getApplicationContext()

        }

        navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.movieSearch)


        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun navToMovieDetailTest() {
        //got sick of playing with coroutines so the movie_list can load.. sleep is insurance policy
        sleep(1000)
        onView(withId(R.id.movie_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        assertThat(
            navController.currentDestination?.id,
            CoreMatchers.equalTo(R.id.movieDetailFragment)
        )
    }



    //returning true every time.. probably better to test non-instrumentally
/*    @Test
    fun recyclerViewCountTest(){
        val recyclerView = scenario.findViewById(R.id.movie_list)
        val dbCount = getDatabase(context).movieDao.getMovies().getOrAwaitValue().size
        onView(withId(R.id.movie_list))
            .check(RecyclerViewItemCountAssertion(dbCount))


//            simulate button press, interacts with real app db
        onView(withId(R.id.load_more_button)).perform(click())

        onView(withId(R.id.movie_list))
            .check(RecyclerViewItemCountAssertion(dbCount + 20))
    }*/

}


//TODO - some nav-back tests

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, `is`(expectedCount))
    }
}