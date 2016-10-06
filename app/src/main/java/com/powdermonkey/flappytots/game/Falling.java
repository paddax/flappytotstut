package com.powdermonkey.flappytots.game;

import android.graphics.PointF;

import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class Falling implements I2DPhysics {
    private long ts;
    private PointF p;
    private PointF v;
    private int frame = 0;

    public Falling(float x, float y) {
        p = new PointF(x, y);
        v = new PointF(0,0);
        ts = System.currentTimeMillis();
    }

    @Override
    public void update(long ts) {
        //pseudo acceleration due to gravity
        v.y = v.y + ((ts - this.ts) / 100.0f);
        //try to keep the rotation constant regardless of drawing time
        frame += ((ts - this.ts) / 15.0f);
        this.ts = ts;
        p.y += v.y;
        p.x += v.x;
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
        v.x = x;
        v.y = y;
    }

    @Override
    public int getFrame() {
        return frame;
    }
}
