package com.powdermonkey.flappytots.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.gameold.FrameSprite;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Created by Peter Davis on 13/11/2016.
 */

public class Foreground extends AbstractPhysics {

    private final FrameSprite sprite;
    private final int imprints;
    private final int screenWidth;
    private long ts;
    private Point2f size;
    private Point2f offset;

    public Foreground(Bitmap base, int swidth, int sheight, float time) {
        screenWidth = swidth;
        this.v = new Vector2f(-screenWidth / time, 0);
        int h = sheight / 2;
        int w = h * base.getWidth() / base.getHeight();
        sprite = new FrameSprite(base, w, h, 1);

        this.p = new Point2f(0, sheight - h / 2);
        this.ts = System.currentTimeMillis();
        imprints = screenWidth / w + 2;
        size = new Point2f(w * imprints, h);
        offset = new Point2f(0, 0);
    }

    @Override
    public void update(long ts) {
        float t = (ts - this.ts) / 1000.0f; // number of seconds between updates ?
        p.x += (v.x * t);
        p.y += (v.y * t);

        if(p.x < -sprite.getWidth(0) / 2) {
            p.x += sprite.getWidth(0);
        }
        this.ts = ts;
    }

    @Override
    public int getFrame() {
        return 0;
    }

    @Override
    public Point2f getSize() {
        return size;
    }

    @Override
    public Point2f getOffset() {
        return offset;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for(int i=0; i<imprints; i++) {
            sprite.draw(canvas, p.x + (sprite.getWidth(0) - 2) * i, p.y, paint, 0);
        }
    }
}
