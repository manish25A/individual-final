package com.manish.bhojmandu

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.manish.bhojmandu.ui.LoginActivity
import org.junit.Rule
import org.junit.Test

class AddToCartTest {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginUI(){
        Espresso.onView(ViewMatchers.withId(R.id.email))
            .perform(ViewActions.typeText("manish@gmail.com"))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("manish12"))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.la_btn_login))
            .perform(ViewActions.click())

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.recycler_restaurants))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.itemName))
            .check(ViewAssertions.matches(ViewMatchers.withText("Recent Products")))
    }
}