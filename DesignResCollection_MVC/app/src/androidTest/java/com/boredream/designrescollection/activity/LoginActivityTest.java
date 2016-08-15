package com.boredream.designrescollection.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    /**
     * 用户名和密码都符合要求
     */
    @Test
    public void test1() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        // actions
        onView(withContentDescription("用户名")).perform(typeText("boredream"), closeSoftKeyboard());
        onView(withContentDescription("密码")).perform(typeText("123456"), closeSoftKeyboard());
        onView(withContentDescription("登录")).perform(click());

        Thread.sleep(500);

        // assertions
        // TODO write your assertion~ or waiting seconds for check result by yourself
        Thread.sleep(3000);
    }

    /**
     * 用户名为空，密码正确
     */
    @Test
    public void test2() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        // actions
        onView(withContentDescription("用户名")); // do nothing
        onView(withContentDescription("密码")).perform(typeText("123456"), closeSoftKeyboard());
        onView(withContentDescription("登录")).perform(click());

        Thread.sleep(500);

        // assertions
        onView(withId(android.R.id.message))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(withText("请输入用户名")));
    }

    /**
     * 密码为空、用户名正确
     */
    @Test
    public void test3() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        // actions
        onView(withContentDescription("用户名")).perform(typeText("boredream"), closeSoftKeyboard());
        onView(withContentDescription("密码")); // do nothing
        onView(withContentDescription("登录")).perform(click());

        Thread.sleep(500);

        // assertions
        onView(withId(android.R.id.message))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(withText("请输入密码")));
    }

    /**
     * 用户名错误，密码正确
     */
    @Test
    public void test4() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        // actions
        onView(withContentDescription("用户名")).perform(typeText("hahaha"), closeSoftKeyboard());
        onView(withContentDescription("密码")).perform(typeText("123456"), closeSoftKeyboard());
        onView(withContentDescription("登录")).perform(click());

        Thread.sleep(500);

        // assertions
        onView(withId(android.R.id.message))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(withText("找不到用户")));
    }

    /**
     * 用户名正确，密码错误
     */
    @Test
    public void test5() throws InterruptedException {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        // actions
        onView(withContentDescription("用户名")).perform(typeText("boredream"), closeSoftKeyboard());
        onView(withContentDescription("密码")).perform(typeText("123321"), closeSoftKeyboard());
        onView(withContentDescription("登录")).perform(click());

        Thread.sleep(500);

        // assertions
        onView(withId(android.R.id.message))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(withText("密码不正确")));
    }

}
