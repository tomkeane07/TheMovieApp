package com.example.themovieapp.UI

import android.content.Context
import android.net.wifi.WifiManager
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
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.themovieapp.R
import com.example.themovieapp.ui.fragment.TopRatedMovieListFragment
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
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class TopRatedMovieListFragmentTest {


    lateinit var scenario: FragmentScenario<TopRatedMovieListFragment>
    lateinit var context: Context
    lateinit var navController: TestNavHostController

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope =
        TestScope(testDispatcher)

    @Before
    fun setUp() {


        runBlocking {
            scenario = launchFragmentInContainer<TopRatedMovieListFragment>()
            context = ApplicationProvider.getApplicationContext()
        }

        navController = TestNavHostController(context)
        navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.top_rated_movie_search)


        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }


    @After
    fun tearDown() {
        //wifi may get turned off in refreshButton test
        val wifiManager = context
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.setWifiEnabled(true)

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun navToMovieDetailTest() {
        //got sick of playing with coroutines so the movie_list can load.. sleep is insurance policy
        sleep(500)
        onView(withId(R.id.movie_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        assertThat(
            navController.currentDestination?.id,
            CoreMatchers.equalTo(R.id.movie_detail)
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

/*    @Test
    fun testRefreshButton() {
        //Navsetup()
        //ensure db is empty
        scenario.onFragment {
            it.viewModelTopRated.clearDB()
        }
        //turn off internet
        val wifiManager = context
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.setWifiEnabled(false)


        //tests
        onView(withId(R.id.movie_list_empty_text))
            .check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.movie_list_empty_button))
            .check(matches(isCompletelyDisplayed()))
            .perform(click())
        //turn on internet
        wifiManager.setWifiEnabled(true)

        //tests
        onView(withId(R.id.movie_list_empty_button))
            .perform(click())

*//*        sleep(500)
        onView(withId(R.id.movie_list))
            .check(matches(CustomRecyclerViewTestUtil.itemCountGreaterThan(0)))*//*

    }*/

    fun Navsetup() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }
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