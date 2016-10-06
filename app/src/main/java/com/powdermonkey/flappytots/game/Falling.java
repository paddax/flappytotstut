package com.powdermonkey.flappytots.game;

import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class Falling implements I2DPhysics {
    private long ts;
    private float x, y;
    private float vx, vy;
    private int frame = 0;

    public Falling(float x, float y) {
        this.x = x;
        this.y = y;
        vx = 0;
        vy= 0;
        ts = System.currentTimeMillis();
    }

    @Override
    public void update(long ts) {
        //pseudo acceleration due to gravity
        vy = vy + ((ts - this.ts) / 100.0f);
        //try to keep the rotation constant regardless of drawing time
        frame += ((ts - this.ts) / 15.0f);
        this.ts = ts;
        y += vy;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getFrame() {
        return frame;
    }
}
