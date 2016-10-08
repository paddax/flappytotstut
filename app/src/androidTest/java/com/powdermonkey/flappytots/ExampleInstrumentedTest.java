package com.powdermonkey.flappytots;

import android.content.Context;
import android.graphics.RectF;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.powdermonkey.flappytots.geometry.Circle2dF;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("com.powdermonkey.flappytots", appContext.getPackageName());
    }

    @Test
    public void circleIntersect() throws Exception {
        Circle2dF c1 = new Circle2dF(0, 0, 1);
        Circle2dF c2 = new Circle2dF(2.1f, 0, 1);

        assertFalse("Circles should not intersect: ", c1.intersect(c2));

        c2 = new Circle2dF(1, 0, 1);
        assertTrue("Circles intersect: ", c1.intersect(c2));
    }

    @Test
    public void circleRectIntersect() throws Exception {
        Circle2dF c1 = new Circle2dF(1, 0, 1);
        RectF r = new RectF(1.5f, -2, 42, 2);
        assertTrue("Circle intersects rectangle: ", c1.intersect(r));

        c1 = new Circle2dF(0,0,1);
        r = new RectF(-10, -10, 10, 10);
        assertTrue("Circle intersects rectangle: ", c1.intersect(r));
    }

}
