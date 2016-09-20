package com.boredream.designrescollection.ui.login;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.idlingres.RxIdlingResource;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.plugins.RxJavaPlugins;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

	@Rule
	public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

	static {
		RxJavaPlugins.getInstance().registerObservableExecutionHook(RxIdlingResource.get());
	}

	@Test
	public void testLogin_EmptyPassword() throws Exception {
		Intent intent = new Intent();
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.et_username)).perform(typeText("13913391521"), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText(""), closeSoftKeyboard());
		onView(withId(R.id.btn_login)).perform(click());

		// assertions
		onView(withId(android.R.id.message))
				.inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
				.check(matches(withText("密码不能为空")));
	}

	@Test
	public void testLogin_EmptyUsername() throws Exception {
		Intent intent = new Intent();
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.et_username)).perform(typeText(""), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText("123456"), closeSoftKeyboard());
		onView(withId(R.id.btn_login)).perform(click());

		// assertions
		onView(withId(android.R.id.message))
				.inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
				.check(matches(withText("用户名不能为空")));
	}

	@Test
	public void testLogin_Success() throws Exception {
		Intent intent = new Intent();
		intent.putExtra("checkLogin", true);
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.et_username)).perform(typeText("18551681236"), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText("123456"), closeSoftKeyboard());
		onView(withId(R.id.btn_login)).perform(click());

		// assertions
		Assert.assertTrue(UserInfoKeeper.getCurrentUser() != null);
	}

	@Test
	public void testLogin_UserNotExit() throws Exception {
		Intent intent = new Intent();
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.et_username)).perform(typeText("110110110"), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText("123456"), closeSoftKeyboard());
		onView(withId(R.id.btn_login)).perform(click());

		// assertions
		onView(withId(android.R.id.message))
				.inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
				.check(matches(withText("找不到用户")));
	}

	@Test
	public void testLogin_PswError() throws Exception {
		Intent intent = new Intent();
		mActivityRule.launchActivity(intent);

		// actions
		onView(withId(R.id.et_username)).perform(typeText("18551681236"), closeSoftKeyboard());
		onView(withId(R.id.et_password)).perform(typeText("110"), closeSoftKeyboard());
		onView(withId(R.id.btn_login)).perform(click());

		// assertions
		onView(withId(android.R.id.message))
				.inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
				.check(matches(withText("密码不正确")));
	}
}
