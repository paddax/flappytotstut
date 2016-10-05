package com.powdermonkey.flappytots.game;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class FallingFlower {
    private long ts;
    private float x, y;
    private float vx, vy;
    private int frame = 0;

    public FallingFlower(float x, float y) {
        this.x = x;
        this.y = y;
        vx = 0;
        vy= 0;
        ts = System.currentTimeMillis();
    }

    public void update(long ts) {
        //pseudo acceleration due to gravity
        vy = vy + ((ts - this.ts) / 100.0f);
        //try to keep the rotation constant regardless of drawing time
        frame += ((ts - this.ts) / 15.0f);
        this.ts = ts;
        y += vy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getFrame() {
        return frame;
    }
}
