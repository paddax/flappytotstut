package com.powdermonkey.flappytots.gameold;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.List;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class FlappyPhysics extends AbstractPhysics {
    private final float jump;
    private final float acceleration;
    private final float gacceleration;
    private long ts;
    private float frame = 0;
    private long imptime;
    private boolean glide;
    private ISprite sprite;

    public FlappyPhysics(float width, float height) {
        p = new Point2f(0, 0);
        v = new Vector2f(0,0);

        jump = height / 1.5f; // pixels/sec2
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
        return (int) frame;
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

        if(drawCollisionRegions) {
            List<? extends IRegion> rr = regions.frames.get(getFrame());
            for(IRegion r: rr) {
                r.draw(canvas, paint);
            }
        }
    }

    public void setSprite(ISprite sprite) {
        this.sprite = sprite;
        regions = new RegionSet(sprite.getRegions());
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
