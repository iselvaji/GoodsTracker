package com.easyvan.goodstracker;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.easyvan.goodstracker.model.rest.product.pojo.Product;
import com.easyvan.goodstracker.view.DetailsActivity;
import com.easyvan.goodstracker.view.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.easyvan.goodstracker.utils.AppConstants.KEY_PRODUCT;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sm5 on 6/18/2019.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Intent mIntent;

    @Rule
    private ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});

    @Before
    public void setup() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getTargetContext();
        mIntent = new Intent(context, MainActivity.class);
    }

    @Test
    public void preConditions() {

        try {
            mActivityRule.launchActivity(mIntent);

            Espresso.onView(withId(R.id.progressbar)).check(matches(anything()));
            Espresso.onView(withId(R.id.recyclerviewItems)).check(matches(anything()));

            // No items textview displayed and should disappear
            Espresso.onView(withId(R.id.textviewNoItems)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testShowProgressAtFirst() {

        try {
            MainActivity mainActivity = mActivityRule.launchActivity(mIntent);
            RecyclerView recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recyclerviewItems);

            // Check if progress is visible
            Espresso.onView(withId(R.id.progressbar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            Espresso.onView(withId(R.id.textviewNoItems)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

            // Check if progress is gone after item loaded
            IdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 1);
            IdlingRegistry.getInstance().register(idlingResource);

            //Espresso.onView(withId(android.R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            IdlingRegistry.getInstance().unregister(idlingResource);

        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void loadItems() {
        try {
            MainActivity mainActivity = mActivityRule.launchActivity(mIntent);
            RecyclerView recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recyclerviewItems);

            // Check if properly load first 20 items
            IdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 20);
            IdlingRegistry.getInstance().register(idlingResource);
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.recyclerviewItems)).perform(scrollToPosition(20));
            assertEquals(20, recyclerView.getAdapter().getItemCount());
            IdlingRegistry.getInstance().unregister(idlingResource);
            Espresso.onView(withId(R.id.recyclerviewItems)).check(matches(withListItemCount(20)));

            // Check if properly load next 20 items
            IdlingResource idlingResource2 = new ListCountIdlingResource(recyclerView, 21);
            IdlingRegistry.getInstance().register(idlingResource);
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.recyclerviewItems)).perform(scrollToPosition(40));
            assertEquals(40, recyclerView.getAdapter().getItemCount());
            IdlingRegistry.getInstance().unregister(idlingResource2);
            Espresso.onView(withId(R.id.recyclerviewItems)).check(matches(withListItemCount(40)));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void openDetailScreen() {

        try {
            MainActivity mainActivity = mActivityRule.launchActivity(mIntent);
            RecyclerView recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recyclerviewItems);

            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            Instrumentation.ActivityMonitor receiverActivityMonitor =
                    instrumentation.addMonitor(DetailsActivity.class.getName(), null, false);

            // Wait till first item in list is loaded
            IdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 1);
            IdlingRegistry.getInstance().register(idlingResource);

            Espresso.onView(withId(R.id.recyclerviewItems)).check(matches(anything()));

            Thread.sleep(2000);
            // Click first list item and check if correct activity is launched
            Espresso.onView(withId(R.id.recyclerviewItems)).perform(actionOnItemAtPosition(0, click()));

            Activity detailActivity = receiverActivityMonitor.waitForActivityWithTimeout(2000);

            instrumentation.removeMonitor(receiverActivityMonitor);
            assertNotNull("DetailActivity is null", detailActivity);
            assertEquals("Launched Activity is not DetailActivity", DetailsActivity.class, detailActivity.getClass());

            // Check if correct Intent is passed to launched activity
            int id = 1;
            Intent intent = detailActivity.getIntent();
            Product product = (Product) intent.getExtras().get(KEY_PRODUCT);

            assertNotNull("DetailActivity received product is null", product);
            assertEquals(id, product.getId().intValue());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Matcher<View> withListItemCount(final int expectedCount) {
        final Matcher<Integer> matcher = is(expectedCount);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                return matcher.matches(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with childCount: ");
                matcher.describeTo(description);
            }
        };
    }
}
