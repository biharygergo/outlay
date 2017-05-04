package com.outlay.view.activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.TextView;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import app.outlay.R;
import app.outlay.view.activity.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class MainActivityInstrumentationTest {

    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void signInIfNeeded() {
        try {
            onView(withId(R.id.skipButton)).perform(click());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        catch (NoMatchingViewException e) {
            // already logged in
        }
    }

    private void checkNavigationAndBack(String expectedTitle) {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(expectedTitle)));
        Espresso.pressBack();
        onView(withId(R.id.drawerIcon)).check(matches(isDisplayed()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateBasicNavigation() {
        onView(withId(R.id.chartIcon)).perform(click());
        checkNavigationAndBack("Today");

        onView(withId(R.id.addCategory)).perform(click());
        checkNavigationAndBack("Categories");

        onView(withId(R.id.drawerIcon)).perform(click());
        onView(withText(R.string.menu_item_analysis)).perform(click());
        checkNavigationAndBack("Analysis");

        onView(withId(R.id.drawerIcon)).perform(click());
        onView(withText(R.string.menu_item_categories)).perform(click());
        checkNavigationAndBack("Categories");
    }



}