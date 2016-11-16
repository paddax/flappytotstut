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

}
