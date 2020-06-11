package com.example.themovieapp.UI

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.R
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

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var homeViewModel: HomeViewModel
    lateinit var scenario: FragmentScenario<HomeFragment>

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
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }


        // simulate Btn click
        onView(ViewMatchers.withId(R.id.look_up_movies_button)).perform(ViewActions.click())
        // Verify that performing a click prompts the correct Navigation action
        assertThat(navController.currentDestination?.id, equalTo(R.id.movieSearch))
    }
}
