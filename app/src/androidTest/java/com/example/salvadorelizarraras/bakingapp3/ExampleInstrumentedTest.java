package com.example.salvadorelizarraras.bakingapp3;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private IdlingResource mIdlingResources;

    @Before
    public void registerIdlingResource() {

        mIdlingResources = mActivityRule.getActivity().fragmentHome.getIdlingResource();

        //Espresso.registerIdlingResources(mIdlingResources);
        IdlingRegistry.getInstance().register(mIdlingResources);
    }

    @Test
    public void idlingResourceTest() {
        onView(withId(R.id.m_recycler_home)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }
    @Test
    public void FragmentHomeTest()
    {
        onView(withText("Nutella Pie")).perform(click());
    }

    @After
    public void unregisterIdlingResource() {

                        if (mIdlingResources != null) {
                        IdlingRegistry.getInstance().unregister(mIdlingResources);
                    }
    }

}
