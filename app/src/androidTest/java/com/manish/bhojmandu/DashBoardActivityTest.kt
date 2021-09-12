package com.manish.bhojmandu

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.manish.bhojmandu.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4ClassRunner::class)
class DashBoardActivityTest {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun dashTest(){
        onView(withId(R.id.email))
            .perform(ViewActions.typeText("manish@gmail.com"))
        closeSoftKeyboard()

        onView(withId(R.id.password))
            .perform(ViewActions.typeText("manish12"))
       closeSoftKeyboard()

        onView(withId(R.id.la_btn_login))
            .perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.recycler_restaurants))
            .check(matches(isDisplayed()))
        closeSoftKeyboard()
        //nav to dash fragment
        onView(withId(R.id.nav_profile)).perform(scrollTo()).perform(click())

        //verify dash fragment
        onView(withId(R.id.restaurant_title))
            .check(matches(ViewMatchers.withText("Restaurants")))
    }
}