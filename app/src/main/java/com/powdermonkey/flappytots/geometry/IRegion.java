package com.powdermonkey.flappytots.geometry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import javax.vecmath.Point2f;

/**
 * Created by Peter Davis on 07/10/2016.
 */

public interface IRegion {

    int CIRCLE = 1;
    int RECT = 2;

    /**
     * Deterimines if this region intersects with another region
     * @param r Region to detect collision
     * @return True if the regions intersect (collide)
     */
    boolean intersect(IRegion r);

    /**
     * Moves the collision region to the specified location, maintaining the designed offset
     * @param x X location to move
     * @param y Y location to move
     */
    void move(float x, float y);


    /**
     * Moves the collision region to the specified location, maintaining the designed offset
     *
     * @param p Location to move to
     */
    void move(Point2f p);

    /**
     * Draws the collision
     * @param canvas Canvas to draw on
     * @param paint Paint to use, this should not be modified
     */
    void draw(Canvas canvas, Paint paint);

    /**
     * Scales the designed region
     * @param x X scale
     * @param y Y scale
     */
    void scale(float x, float y);

    /**
     * Must be one of the defined types CIRCLE/RECT. The purpose is to remove the need for a
     * potentially slow instanceof or assignableFrom()
     * @return defined type
     */
    int getType();

    /**
     * Offsets the designed x,y of the collision region
     * @param x X offset to apply to design
     * @param y Y offset to apply to design
     */
    void offset(float x, float y);

    /**
     * Constructs a copy of the region
     * @return Copy of the region
     */
    IRegion copy();
}
