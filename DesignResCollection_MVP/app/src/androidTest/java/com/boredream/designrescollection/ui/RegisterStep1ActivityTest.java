package com.boredream.designrescollection.ui;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.boredream.designrescollection.R;
import com.boredream.designrescollection.ui.register.RegisterStep1Activity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class RegisterStep1ActivityTest {

	@Rule
	public ActivityTestRule<RegisterStep1Activity> mActivityRule = new ActivityTestRule<>(RegisterStep1Activity.class, true, false);

	@Test
	public void test() {
		Intent intent = new Intent();
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.et_username)).perform(typeText("13913391521"), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText("123456"), closeSoftKeyboard());
		onView(withId(R.id.btn_next)).perform(click());

		// assertions
		onView(withId(android.R.id.message))
			.inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
			.check(matches(withText("账号已被注册")));
	}
}
