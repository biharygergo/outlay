package com.outlay.view.activity;

import android.support.test.rule.ActivityTestRule;
import app.outlay.R;
import app.outlay.view.activity.LoginActivity;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LoginActivityInstrumentationTest {

    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    // Looks for an EditText with id = "R.id.signInEmail"
    // Types the text "test@email.com" into the EditText
    // Verifies the EditText has text "test@email.com"
    @Test
    public void validateSignInEmailField() {
        onView(withId(R.id.signInEmail)).perform(typeText("test@email.com")).check(matches(withText("test@email.com")));
    }
}