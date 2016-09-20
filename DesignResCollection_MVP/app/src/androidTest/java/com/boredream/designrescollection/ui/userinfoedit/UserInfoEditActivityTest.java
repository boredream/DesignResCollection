package com.boredream.designrescollection.ui.userinfoedit;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.boredream.designrescollection.R;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.idlingres.RxIdlingResource;
import com.boredream.designrescollection.utils.UserInfoKeeper;

import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import rx.plugins.RxJavaPlugins;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class UserInfoEditActivityTest {

    @Rule
    public ActivityTestRule<UserInfoEditActivity> mActivityRule = new ActivityTestRule<>(UserInfoEditActivity.class, true, true);

    static {
        User user = new User();
        user.setObjectId("57abf85e2e958a00543737da");
        user.setUsername("18551681236");
        user.setNickname("");
        user.setSessionToken("hh45pryl55lbm6n3l6sfbuywg");
        UserInfoKeeper.setCurrentUser(user);

        RxJavaPlugins.getInstance().registerObservableExecutionHook(RxIdlingResource.get());
    }

    @Test
    public void testUploadAvatar() throws Exception {
        Intents.init();

        // Stub the Intent.
        Instrumentation.ActivityResult resultGallery = createImageGalleryActivityResultStub();
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(resultGallery);

        // actions
        onView(withId(R.id.ll_avatar)).perform(click());
        onView(withText("相册"))
                .inRoot(withDecorView(IsNot.not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .perform(click());

        // assertions
        Assert.assertTrue(UserInfoKeeper.getCurrentUser().getAvatar() != null);
    }

    private Instrumentation.ActivityResult createImageGalleryActivityResultStub() {
        Uri uri = Uri.parse("file:///android_asset/test_avatar.jpg");

        Intent resultData = new Intent();
        resultData.setData(uri);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    @Test
    public void testUpdateNickname() throws Exception {
        String newName = "boredream" + new Random().nextInt(10);

        // actions
        onView(withId(R.id.ll_username)).perform(click());
        onView(withId(R.id.et_input)).perform(typeText(newName), closeSoftKeyboard());
        onView(withText("保存")).perform(click());

        // assertions
        onView(withId(R.id.tv_username)).check(matches(withText(newName)));
    }
}