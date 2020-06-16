package com.example.themovieapp.androidTestUtils

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object EspressoTestsMatchers {
    fun withDrawable(resourceId: Int): DrawableMatcher {
        return DrawableMatcher(resourceId)
    }

    fun noDrawable(): DrawableMatcher {
        return DrawableMatcher(-1)
    }
}

class DrawableMatcher(i: Int) : TypeSafeMatcher<View?>() {
    protected override fun matchesSafely(item: View?): Boolean {
        return false
    }

    override fun describeTo(description: Description?) {}
}