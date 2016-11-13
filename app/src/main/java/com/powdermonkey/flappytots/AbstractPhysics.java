package com.powdermonkey.flappytots;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Created by Peter Davis on 09/10/2016.
 */

public abstract class AbstractPhysics implements I2DPhysics {

    protected Point2f p;
    protected Vector2f v;
    protected boolean drawCollisionRegions = false;
    protected RegionSet regions;

    @Override
    public Point2f getPoint() {
        return p;
    }

    @Override
    public Vector2f getVector() {
        return v;
    }

    @Override
    public void setVector(float x, float y) {
        v.x = x;
        v.y = y;
    }

    @Override
    public void setPoint(float x, float y) {
        p.x = x;
        p.y = y;
    }

    @Override
    public void updateRegions() {
        for (IRegion r : regions.frames.get(getFrame()))
            r.move(p);
    }

    /**
     * Lists the currently active regions for the current frame
     *
     * @return List of active regions
     */
    public List<? extends IRegion> getRegions() {
        return regions.frames.get(getFrame());
    }

    /**
     * Determines if this object collides with another object
     *
     * @param reg Regions of the other object
     * @return True of the any region overlaps (collides)
     */
    public boolean collide(List<? extends IRegion> reg) {
        for (IRegion r : regions.frames.get(getFrame())) {
            for (IRegion r1 : reg) {
                if (r.intersect(r1))
                    return true;
            }
        }
        return false;
    }

    /**
     * Updates the collision regions based on the location of the sprite
     */
    public void updateCollisionRegion() {
        List<IRegion> x1 = regions.frames.get(getFrame() % regions.frames.size());
        for (IRegion r : x1)
            r.move(p);
    }
}
