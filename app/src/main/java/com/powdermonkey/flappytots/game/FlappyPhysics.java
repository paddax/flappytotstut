package com.powdermonkey.flappytots.game;

import android.graphics.PointF;

import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class FlappyPhysics implements I2DPhysics {
    private final float jump;
    private final float acceleration;
    private final float gacceleration;
    private long ts;
    private PointF p;
    private PointF v;
    private float frame = 0;
    private long imptime;
    private boolean glide;

    public FlappyPhysics(float width, float height) {
        p = new PointF(0, 0);
        v = new PointF(0,0);

        jump = height / 1.5f; // pixels/sec2
        acceleration = height; // pixels/sec2
        gacceleration = acceleration / 4;
        ts = System.currentTimeMillis();
    }

    @Override
    public void update(long ts) {
        //pseudo acceleration due to gravity
        float t = (ts - this.ts) / 1000.0f; // time in seconds
        v.y = v.y + (glide&v.y>0?gacceleration:acceleration) * t; // constant acceleration based on time
        p.y += (v.y * t);
        //p.x += v.x;


        if (ts - imptime > 500) {
            frame = 0; // falling frames
        } else {
            frame += ((ts - this.ts) / 100.0f); // flap sequence
        }

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
        return (int) frame;
    }

    @Override
    public void setPoint(float x, float y) {
        p.x = x;
        p.y = y;
    }

    public void impulse(long ts) {
        imptime = ts;
        v.y = -jump;
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
