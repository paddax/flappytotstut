package com.powdermonkey.flappytots;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.geometry.IRegion;

import java.util.List;

/**
 * A sprite is an animated bitmap.  Decendents are expected to handle some incoming data determine
 * the collision regions and express frames.
 *
 * Created by Peter Davis on 05/10/2016.
 */

public interface ISprite {

    /**
     * Obtains the collision regions from the sprite frame
     *
     * @param frame Frame specific regions
     * @return Array of regions translated to location
     */
    List<? extends IRegion> getRegions(int frame);

    /**
     * Determines if the sprite at location intersects with regions presented
     *
     * @param regions Prepared regions of other sprite
     * @return true if the sprites are colliding
     */
    boolean collide(List<? extends IRegion> regions);

    /**
     * Updates the collision regions
     *
     * @param p     Point representing the draw location on the screen
     * @param frame Frame to update
     */
    void updateRegions(PointF p, int frame);

    void draw(Canvas canvas, float x, float y, Paint paint, int frame);

    int getHeight(int frame);

    int getWidth(int frame);

    int getFrameCount();

}
