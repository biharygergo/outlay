package com.outlay.view.activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.TextView;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import app.outlay.R;
import app.outlay.view.activity.LoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
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

    @Before
    public void deleteCategories() {
        try {
            onView(withId(R.id.addCategory)).check(matches(isDisplayed()));
        }
        catch (AssertionFailedError e) {
            onView(withId(R.id.drawerIcon)).perform(click());
            onView(withText(R.string.menu_item_categories)).perform(click());
            try {
                while(true) {
                    onView(withId(R.id.categoriesGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
                    openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
                    onView(withText(R.string.label_delete)).perform(click());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            catch (Exception nomore) {
                // no more categories
            }
            pressBack();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
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

    @Test
    public void validateEachNumpadButton() {
        int buttons[] = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        for (int i = 0; i < 10; i++) {
            onView(withId(buttons[i])).perform(click());
            onView(withId(R.id.amountEditable)).check(matches(withText(String.valueOf(i))));
            onView(withId(R.id.btnClear)).perform(click());
        }
    }

    @Test
    public void validateAddAndDeleteCategory() {
        onView(withId(R.id.addCategory)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.iconsGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.categoryName)).perform(typeText("category"));
        onView(withId(R.id.action_save)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.categoriesGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.label_delete)).perform(click());
    }


}