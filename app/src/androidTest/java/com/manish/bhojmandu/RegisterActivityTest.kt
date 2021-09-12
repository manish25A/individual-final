package com.manish.bhojmandu

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.manish.bhojmandu.ui.SignupActivity
import org.junit.Rule
import org.junit.Test

class RegisterActivityTest {
    @get:Rule
    val testRule = ActivityScenarioRule(SignupActivity::class.java)

    @Test
    fun testLoginUI() {


        Espresso.onView(ViewMatchers.withId(R.id.etFName))
            .perform(ViewActions.typeText("test"))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etLName))
            .perform(ViewActions.typeText("test Lname"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.etEmail))
            .perform(ViewActions.typeText("test111@gmail.com"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.etNumber))
            .perform(ViewActions.typeText("1234567890"))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etPass))
            .perform(ViewActions.typeText("manish2345"))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnregister))
            .perform(ViewActions.click())

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.LoginLinear))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}