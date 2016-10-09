package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.I2DPhysics;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class Falling extends AbstractPhysics {
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
    public int getFrame() {
        return frame;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        sprite.draw(canvas, p.x, p.y, paint, (int) frame);
    }

}
