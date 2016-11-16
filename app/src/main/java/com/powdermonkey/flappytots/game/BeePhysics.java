package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.geometry.IRegion;

import java.util.List;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Created by pdavis on 14/11/2016.
 */

public class BeePhysics extends AbstractPhysics {

    private final float jump;
    private final float acceleration;
    private final float gacceleration;
    private final FrameSprite sprite;
    private final int surfaceHeight;

    private long ts;
    private float frame = 0;
    private long imptime;
    private boolean glide;

    public BeePhysics(FrameSprite bee, int height) {
        p = new Point2f(0, 0);
        v = new Vector2f(0,0);
        sprite = bee;
        surfaceHeight = height;

        jump = height / 2f; //1.5f; // pixels/sec2
        acceleration = height; // pixels/sec2
        gacceleration = acceleration / 3;
        ts = System.currentTimeMillis();

    }

    @Override
    public void update(long ts) {
        //pseudo acceleration due to gravity
        float t = (ts - this.ts) / 1000.0f; // time in seconds
        v.y = v.y + (glide&v.y>0?gacceleration:acceleration) * t; // constant acceleration based on time
        p.y += (v.y * t);
        //p.x += v.x;

        if(p.y > surfaceHeight - sprite.getSize((int) frame).y + sprite.getOffset((int) frame).y) {
            p.y = surfaceHeight - sprite.getSize((int) frame).y + sprite.getOffset((int) frame).y;
            v.y = 0;
        }

        if(p.y < sprite.getOffset((int) frame).y) {
            p.y = sprite.getOffset((int) frame).y;
            v.y = 0;
        }

        if (ts - imptime > 500) {
            frame = 0; // falling frames
        } else {
            frame += ((ts - this.ts) / 100.0f); // flap sequence
        }
        frame %= sprite.getFrameCount();

        this.ts = ts;

    }

    @Override
    public int getFrame() {
        return 0;
    }

    @Override
    public Point2f getSize() {
        return sprite.getSize((int) frame);
    }

    @Override
    public Point2f getOffset() {
        return sprite.getOffset((int) frame);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        sprite.draw(canvas, p.x, p.y, paint, (int) frame);
        List<IRegion> gx = sprite.getRegions().frames.get((int) frame);
//        for(IRegion r: gx) {
//            paint.setColor(Color.argb(255, 255, 0, 0));
//            r.draw(canvas, paint);
//        }
    }

    public void impulse(long ts) {
        imptime = ts;
        v.y = -jump;
    }

    public void glide(boolean g) {
        this.glide = g;
    }

    List<IRegion> getRegions() {
        List<IRegion> gx = sprite.getRegions().frames.get((int) frame);
        for(IRegion r: gx) {
            r.move(p);
        }
        return gx;
    }

}
