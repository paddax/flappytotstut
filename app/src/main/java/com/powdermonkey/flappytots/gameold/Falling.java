package com.powdermonkey.flappytots.gameold;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.RegionSet;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class Falling extends AbstractPhysics {
    private long ts;
    private int frame = 0;
    private ISprite sprite;

    public Falling(float x, float y) {
        p = new Point2f(x, y);
        v = new Vector2f(0,0);
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
        updateCollisionRegion();
    }

    public void setSprite(ISprite sprite) {
        this.sprite = sprite;
        regions = new RegionSet(sprite.getRegions());
    }

    @Override
    public int getFrame() {
        return frame;
    }

    @Override
    public Point2f getSize() {
        return sprite.getSize(frame);
    }

    @Override
    public Point2f getOffset() {
        return sprite.getOffset(frame);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        sprite.draw(canvas, p.x, p.y, paint, (int) frame);
    }

}
