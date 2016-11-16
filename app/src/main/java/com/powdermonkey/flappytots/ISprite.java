package com.powdermonkey.flappytots;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.List;

import javax.vecmath.Point2f;

/**
 * A sprite is an animated bitmap.  Decendents are expected to handle some incoming data determine
 * the collision regions and express frames.
 * <p>
 * Created by Peter Davis on 05/10/2016.
 */

public interface ISprite {

    /**
     * Obtains the collision regions from the sprite frame
     *
     * @return Array of regions translated to location
     */
    RegionSet getRegions();

    /**
     * Draws the sprite at the specified location
     *
     * @param canvas Canvas to draw on
     * @param x      X location of frame
     * @param y      Y location of frame
     * @param paint  Paint mechanism for sprite
     * @param frame  Frame to draw
     */
    void draw(Canvas canvas, float x, float y, Paint paint, int frame);

    /**
     * Determines the height of the current frame
     *
     * @param frame frame of interest
     * @return height of frame
     */
    int getHeight(int frame);

    /**
     * Determines the width of the current frame
     *
     * @param frame frame of interest
     * @return width of frame
     */
    int getWidth(int frame);

    /**
     * Determines the width and height of the specified frame
     *
     * @param frame frame of interest
     * @return size of the specified frame
     */
    Point2f getSize(int frame);

    /**
     * Distance from the top left to the nominal centre of the image
     *
     * @param frame frame of interest
     * @return offset to draw
     */
    Point2f getOffset(int frame);

    /**
     * Determines the total number of frames in this sprite
     *
     * @return Number of frames
     */
    int getFrameCount();

}
