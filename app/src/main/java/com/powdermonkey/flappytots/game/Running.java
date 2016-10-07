package com.powdermonkey.flappytots.game;

import android.graphics.PointF;

import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class Running implements I2DPhysics {
    private long ts;
    private PointF p;
    private PointF v;
    private float frame = 0;

    public Running(float x, float y) {
        p = new PointF(x, y);
        v = new PointF(0,0);
        ts = System.currentTimeMillis();
    }

    @Override
    public void update(long ts) {
        //pseudo acceleration due to gravity
        v.y = v.y + ((ts - this.ts) / 200.0f);
        p.x += ((ts - this.ts) / 3.0f); // constant right (running)
        v.x *= 0.99; // Chucked x velocity attenuation
        //try to keep the frame rotation constant regardless of drawing time
        frame += ((ts - this.ts) / 150.0f); // One frame every 150ms
        p.y += v.y;
        p.x += v.x;
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
        v.x = x;
        v.y = y;
    }

    @Override
    public int getFrame() {
        return Math.round(frame);
    }
}
