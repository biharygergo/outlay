package com.outlay.view.activity;

import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.TextView;

import app.outlay.R;
import app.outlay.view.activity.LoginActivity;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

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

    @Test
    public void validateNoEmailInput(){
        onView(withId(R.id.signInButton)).perform(click());
        EspressoTextInputLayoutUtils.onErrorViewWithinTilWithId(R.id.signInInputEmail).check(matches(withText("Invalid email")));

    }

    @Test
    public void validateNoPasswordInput(){
        onView(withId(R.id.signInButton)).perform(click());
        EspressoTextInputLayoutUtils.onErrorViewWithinTilWithId(R.id.signInInputPassword).check(matches(withText("Invalid password. At least 6 chars")));

    }

    @Test
    public void invalidUserNoEntry(){
        EspressoTextInputLayoutUtils.onEditTextWithinTilWithId(R.id.signInInputEmail).perform(typeText("aaaa@aaa.hu"));
        EspressoTextInputLayoutUtils.onEditTextWithinTilWithId(R.id.signInInputPassword).perform(typeText("Bela123"));
        onView(withText(R.string.label_sign_in)).check(matches(isDisplayed()));
    }



}