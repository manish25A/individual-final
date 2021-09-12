package com.manish.bhojmandu

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.manish.bhojmandu.ui.AboutUs
import org.junit.Rule
import org.junit.Test

class AboutUsTest {

    @get:Rule
    val testRule = ActivityScenarioRule(AboutUs::class.java)

    @Test
    fun aboutusUI() {
        val activity = ActivityScenario.launch(AboutUs::class.java)
        Espresso.onView(withId(R.id.nametext))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}