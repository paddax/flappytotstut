package com.powdermonkey.flappytots.geometry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Peter Davis on 07/10/2016.
 */

public class Circle2dF implements IRegion {

    public PointF defined;
    public PointF center;
    public float radius;

    public Circle2dF(float x, float y, float r) {
        this.center = new PointF(x, y);
        this.defined = new PointF(x, y);
        this.radius = r;
    }

    @Override
    public boolean intersect(IRegion r) {
        if(r.getType() == RECT) {
            return intersect(this, (Rect2dF) r);
        }
        else {
            Circle2dF c = (Circle2dF) r;
            float x = center.x - c.center.x;
            float y = center.y - c.center.y;

            return Math.hypot(x, y) < radius + c.radius;
        }
    }

    @Override
    public void move(float x, float y) {
        center.set(defined);
        center.offset(x, y);
    }

    @Override
    public void move(PointF p) {
        move(p.x, p.y);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawOval(new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius), paint);
    }

    @Override
    public void scale(float x, float y) {
        defined.x *= x;
        defined.y *= y;
        radius *= (x < y) ? x : y;
    }

    @Override
    public int getType() {
        return CIRCLE;
    }

    @Override
    public void offset(float x, float y) {
        defined.x += x;
        defined.y += y;
    }


    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean intersect(Circle2dF c, Rect2dF r) {
        // Find the closest point to the circle within the rectangle
        float closestX = clamp(c.center.x, r.active.left, r.active.right);
        float closestY = clamp(c.center.y, r.active.top, r.active.bottom);

        // Calculate the distance between the circle's center and this closest point
        float distanceX = c.center.x - closestX;
        float distanceY = c.center.y - closestY;

        // If the distance is less than the circle's radius, an intersection occurs
        float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
        return distanceSquared < (c.radius * c.radius);
    }
}
