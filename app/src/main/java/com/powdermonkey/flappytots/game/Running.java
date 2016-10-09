package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class Running extends AbstractPhysics {
    private long ts;
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
        p.x += ((ts - this.ts) / 3.0f); // constant right (running) 1px every 3ms
        v.x *= 0.99; // Chucked x velocity attenuation
        //try to keep the frame rotation constant regardless of drawing time
        frame += ((ts - this.ts) / 150.0f); // One frame every 150ms
        p.y += v.y;
        p.x += v.x;
        this.ts = ts;
    }

    @Override
    public int getFrame() {
        return Math.round(frame);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        sprite.draw(canvas, p.x, p.y, paint, (int) frame);
    }

}
