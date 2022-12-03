package edu.cs371m.reddit

import androidx.test.rule.ActivityTestRule


import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import edu.cs371m.reddit.RecyclerViewChildActions.Companion.actionOnChild
import edu.cs371m.reddit.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import edu.cs371m.reddit.ui.PostRowAdapter
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.equalTo


/**
 * [Testing Fundamentals](http://d.android.com/tools/testing/testing_android.html)
 */
// the setup for this test is based on TestFetch that's available on the original project
// I need something like fetch complete to populate the list
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class InstrumentedApplicationTest {

    @Rule @JvmField
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testFirstElement() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    0,
                    withText("Osaka Aquarium just stepped up their gift shop game with these fat seal plushies"))
                )
            )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.score,
                    0,
                    withText("111372")
            )
        ))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.comments,
                    0,
                    withText("1226")
                )
            )
        )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.image,
                    0,
                    withTagValue(equalTo("bigcat0.jpg"))
                )
            )
        )
    }

    @Test
    fun testThirdElement() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    2,
                    withText("This sable is preposterously cute."))
            )
        )
        // onView(withId(R.id.imageTextUnion)).check(matches(withBitmap("bigcat0.jpg")))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.score,
                    2,
                    withText("34450")
                )
            ))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.comments,
                    2,
                    withText("545")
                )
            )
        )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.image,
                    2,
                    withTagValue(equalTo("bigcat2.jpg"))
                )
            )
        )
    }

    @Test
    fun testOnePost() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                actionOnChild(
                    click(),
                    R.id.title
                )
            )
        )
        onView(withId(R.id.onePostTitle)).check(matches(withText("Osaka Aquarium just stepped up their gift shop game with these fat seal plushies")))
        // onView(withId(R.id.onePostImage)).withTagValue(equalTo("bigcat0.jpg"))
    }

    @Test
    fun favList() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        // fav the first item
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<PostRowAdapter.VH>(
                0,
                actionOnChild(
                    click(),
                    R.id.rowFav
                )
            )
        )
        // check the fav list
        onView(withId(R.id.actionFavorite)).perform(click())
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    0,
                    withText("Osaka Aquarium just stepped up their gift shop game with these fat seal plushies"))
            )
        )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.score,
                    0,
                    withText("111372")
                )
            ))
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.comments,
                    0,
                    withText("1226")
                )
            )
        )
        onView(withId(R.id.recyclerView)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.image,
                    0,
                    withTagValue(equalTo("bigcat0.jpg"))
                )
            )
        )
        onView(isRoot()).perform(pressBack())
        // unfav the first item
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<PostRowAdapter.VH>(
                0,
                actionOnChild(
                    click(),
                    R.id.rowFav
                )
            )
        )
        onView(withId(R.id.actionFavorite)).perform(click())
        onView(withId(R.id.recyclerView)).check { view, noViewFoundException ->
            noViewFoundException?.apply {
                throw this
            }
            assertTrue(view is RecyclerView && view.adapter != null && view.adapter?.itemCount?:0 == 0)
        }
    }
}