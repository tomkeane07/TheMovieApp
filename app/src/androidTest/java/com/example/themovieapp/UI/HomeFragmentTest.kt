package com.example.themovieapp.UI

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.R
import com.example.themovieapp.androidTestUtils.CustomRecyclerViewTestUtil
import com.example.themovieapp.ui.fragment.HomeFragment
import com.example.themovieapp.ui.view.HomeViewModel
import com.example.themovieapp.utils.ManagedCoroutineScope
import com.example.themovieapp.utils.TestScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    lateinit var homeViewModel: HomeViewModel
    lateinit var scenario: FragmentScenario<HomeFragment>
    lateinit var navController: NavController

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope: ManagedCoroutineScope =
        TestScope(testDispatcher)


    @Before
    fun setup() {
        scenario = launchFragmentInContainer<HomeFragment>()
    }

    //Test nav via UI
    @Test
    fun testLookUpMoviesButton() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }


        // simulate Btn click
        onView(withId(R.id.look_up_movies_button)).perform(ViewActions.click())
        // Verify that performing a click prompts the correct Navigation action
        assertThat(navController.currentDestination?.id, equalTo(R.id.top_rated_movie_search))
    }

    @Test
    fun testSearchByName_Text_ClearButton() {
        onView(withId(R.id.search_by_name_text))
            .perform(ViewActions.typeText("dog day afternoon"))
            .check(matches(withText("dog day afternoon")))

        onView(withId(R.id.search_by_name_clear_button))
            .perform(ViewActions.click())

        onView(withId(R.id.search_by_name_text))
            .check(matches(withText("")))
    }

    @Test
    fun testSearchByName_Text_List_Nav() {
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.search_by_name_text))
            .perform(ViewActions.typeText("dog day afternoon"))
            .check(matches(withText("dog day afternoon")))

        sleep(500)
        onView(withId(R.id.search_by_name_movie_list))
            .check(matches(CustomRecyclerViewTestUtil.itemCountGreaterThan(0)))

        onView(withId(R.id.search_by_name_movie_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, ViewActions.click()
                )
            )
        sleep(100)
        assertThat(navController.currentDestination?.id, equalTo(R.id.movie_detail))
    }
}
