package com.manish.bhojmandu

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.manish.bhojmandu.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)
class ActivityLoginTest {
    @Test
    fun loginActivityTest() {
        val activity = ActivityScenario.launch(LoginActivity::class.java)
        Espresso.onView(withId(R.id.LoginLinear))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}