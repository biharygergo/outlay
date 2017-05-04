package com.outlay.view.activity;

import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import app.outlay.R;
import app.outlay.view.activity.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
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

    @Before
    public void signOutIfNeeded() {
        try {
            onView(withId(R.id.drawerIcon)).perform(click());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onView(withText("Sign Out")).perform(click());
        }
        catch (NoMatchingViewException e) {
            // logged out
        }
        try {
            Thread.sleep(9900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    @Test
    public void validateSignUpEmailField(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.signUpEmail)).perform(typeText("test@email.com")).check(matches(withText("test@email.com")));
    }

    @Test
    public void validateNoSignUpEmailInput(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.signUpButton)).perform(click());
        EspressoTextInputLayoutUtils.onErrorViewWithinTilWithId(R.id.signUpInputEmail).check(matches(withText("Invalid email")));
    }

    @Test
    public void validateNoSignUpPasswordInput(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.signUpButton)).perform(click());
        EspressoTextInputLayoutUtils.onErrorViewWithinTilWithId(R.id.signUpInputPassword).check(matches(withText("Invalid password. At least 6 chars")));
    }

    @Test
    public void validateNoMatchPasswordFields(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.signUpPassword)).perform(typeText("Pasword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.signUpRepeatPassword)).perform(typeText("password"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.signUpButton)).perform(click());
        EspressoTextInputLayoutUtils.onErrorViewWithinTilWithId(R.id.signUpInputRepeatPassword).check(matches(withText("Password does not match")));
    }

    @Test
    public void validateEmailAddressAlredyInUse(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.signUpEmail)).perform(typeText("test@email.com"));
        onView(withId(R.id.signUpPassword)).perform(click(), typeText("password"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.signUpRepeatPassword)).perform(typeText("password"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withText(R.string.label_sign_up)).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateSignOutAsTestUser() {
        onView(withId(R.id.signInEmail)).perform(typeText("test@email.com"));
        onView(withId(R.id.signInPassword)).perform(click(), typeText("test123"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.signInButton)).perform(click());
        try {
            Thread.sleep(9900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawerIcon)).perform(click());
        onView(withText("Sign Out")).perform(click());
        onView(withId(R.id.signInForm)).check(matches(isDisplayed()));
    }

    @Test
    public void validateSignOutAsGuest() {
        onView(withId(R.id.skipButton)).perform(click());
        try {
            Thread.sleep(9900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawerIcon)).perform(click());
        onView(withText("Sign Out")).perform(click());
        onView(withId(R.id.loginForm)).check(matches(isDisplayed()));
    }



}