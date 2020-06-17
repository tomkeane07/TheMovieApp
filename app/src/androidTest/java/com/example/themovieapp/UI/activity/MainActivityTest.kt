package com.example.themovieapp.UI.activity

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.themovieapp.R
import com.example.themovieapp.ui.activity.MainActivity
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit.rule

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testNavigationView() {
        onView(withId(R.id.drawer_layout)).perform(open())

        onView(withId(R.id.navigation_view))
            .check(matches(hasChildCount(1)))
            .perform(NavigationViewActions.navigateTo(R.id.about_App))

    }
}