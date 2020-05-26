package com.example.themovieapp.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themovieapp.R
import com.example.themovieapp.testUtils.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


//Due to the simplicity of the homePage,
// I decided to keep it's viewModel & Fragment tests constrained to one file
@RunWith(AndroidJUnit4::class)
class HomePageTest {

    //private lateinit var homeViewModel: HomeViewModel


    //This rule runs all Architecture Components-related background jobs
    // in the same thread so that the test results happen synchronously,
    // and in a repeatable order.
    //  When you write tests that include testing LiveData, use this rule!
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var homeViewModel: HomeViewModel
    lateinit var mockNavController: NavController
    lateinit var homeScenario: FragmentScenario<HomeFragment>

    @Before
    fun setup(){
        // Create a graphical FragmentScenario for Home
        homeScenario = launchFragmentInContainer<HomeFragment>()

        // Create a mock NavController
        mockNavController = mock(NavController::class.java)

        homeScenario.onFragment { fragment ->
            homeViewModel =  fragment.getViewModel()
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
    }

    //Test nav via UI
    @Test
    fun testLookUpMoviesButton(){

        // simulate Btn click
        onView(ViewMatchers.withId(R.id.look_up_movies_button)).perform(ViewActions.click())
        // Verify that performing a click prompts the correct Navigation action
        verify(mockNavController).navigate(R.id.action_homeFragment_to_movieListFragment)
    }

    //Test nav view UI & LIveData
    @Test
    fun testNavigateToSearchLiveData(){
        //perform navigation click
        onView(ViewMatchers.withId(R.id.look_up_movies_button)).perform(ViewActions.click())

        val navigateToSearch = homeViewModel.navigateToSearch.getOrAwaitValue()

        //assert onNavigatedToSearch has occured
        assert(!navigateToSearch)
        /*
        * I'm aware that performing both tests is a bit redundant,
        * but I wanted to attempt two methods for learning purposes
        * */
    }
}