package com.powdermonkey.flappytots.game;

import android.graphics.PointF;

import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 09/10/2016.
 */
public class MovingLeft implements I2DPhysics {

    private long ts;
    PointF p;
    PointF v;

    /**
     * Constructs an object that moves left across the screen
     *
     * @param x Initial x position
     * @param y Initial y position
     * @param vx Vector magnitude of Y in pixels per second
     */
    public MovingLeft(float x, float y, float vx) {
        p = new PointF(x, y);
        v = new PointF(vx, 0);
        ts = System.currentTimeMillis();
    }

    @Override
    public void update(long ts) {
        float t = (ts - this.ts) / 1000.0f; // number of seconds between updates ?
        p.x += (v.x * t);
        p.y += (v.y * t);
        this.ts = ts;
    }

    @Override
    public PointF getPoint() {
        return p;
    }

    @Override
    public PointF getVector() {
        return v;
    }

    @Override
    public void setVector(float x, float y) {
        v.set(x, y);

    }

    @Override
    public int getFrame() {
        return 0;
    }

    @Override
    public void setPoint(float x, float y) {
        p.set(x, y);
    }
}
