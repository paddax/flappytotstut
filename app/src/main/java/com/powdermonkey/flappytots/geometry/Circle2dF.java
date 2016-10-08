package com.powdermonkey.flappytots.geometry;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Peter Davis on 07/10/2016.
 */

public class Circle2dF implements IRegion {

    public PointF center;

    public float radius;

    public Circle2dF(float x, float y, float r) {
        this.center = new PointF(x, y);
        this.radius = r;
    }

    public boolean intersect(Circle2dF c) {
        float x = center.x - c.center.x;
        float y = center.y - c.center.y;

        return Math.hypot(x, y) < radius + c.radius;
    }

    public boolean intersect(RectF r) {
        return intersect(this, r);
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean intersect(Circle2dF c, RectF r) {
        // Find the closest point to the circle within the rectangle
        float closestX = clamp(c.center.x, r.left, r.right);
        float closestY = clamp(c.center.y, r.top, r.bottom);

        // Calculate the distance between the circle's center and this closest point
        float distanceX = c.center.x - closestX;
        float distanceY = c.center.y - closestY;

        // If the distance is less than the circle's radius, an intersection occurs
        float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
        return distanceSquared < (c.radius * c.radius);
    }
}
