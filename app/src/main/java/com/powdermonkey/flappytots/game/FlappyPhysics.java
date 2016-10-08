package com.powdermonkey.flappytots.game;

import android.graphics.PointF;

import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class FlappyPhysics implements I2DPhysics {
    private long ts;
    private PointF p;
    private PointF v;
    private float frame = 0;
    private long imptime;
    private boolean glide;

    public FlappyPhysics(float x, float y) {
        p = new PointF(x, y);
        v = new PointF(0,0);
        ts = System.currentTimeMillis();
    }

    @Override
    public void update(long ts) {
        //pseudo acceleration due to gravity
        v.y = v.y + ((ts - this.ts) / (glide && (v.y > 0) ? 100.0f : 25.0f));
        //try to keep the rotation constant regardless of drawing time
        if (ts - imptime > 300) {
            frame = 0;
        } else {
            frame += ((ts - this.ts) / 100.0f);
        }
        this.ts = ts;
        p.y += v.y;
        //p.x += v.x;
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
        return (int) frame;
    }

    @Override
    public void setPoint(float x, float y) {
        p.x = x;
        p.y = y;
    }

    public void impulse(long ts) {
        imptime = ts;
        v.y = -15;
    }

    public void hitTop(float top) {
        p.y = top;
        v.y = 0;
    }

    public void hitBottom(int height) {
        p.y = height;
        v.y = 0;
    }

    public void glide(boolean g) {
        this.glide = g;
    }
}
