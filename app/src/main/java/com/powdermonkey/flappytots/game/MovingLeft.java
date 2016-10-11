package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.IRegion;

import java.util.List;

/**
 * Created by Peter Davis on 09/10/2016.
 */
public class MovingLeft extends AbstractPhysics {

    private long ts;
    private float frame;

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

        frame += ((ts - this.ts) / 50.0f);
        frame %= sprite.getFrameCount();

        this.ts = ts;
    }

    @Override
    public int getFrame() {
        return (int) frame;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        sprite.draw(canvas, p.x, p.y, paint, (int) frame);

        if(drawCollisionRegions) {
            List<? extends IRegion> rr = sprite.getRegions(getFrame());
            for(IRegion r: rr) {
                r.draw(canvas, paint);
            }
        }
    }
}
