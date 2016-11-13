package com.powdermonkey.flappytots.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.gameold.FrameSprite;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Simple moving left character that consists of 2 sprites.
 * <ul>
 *     <li>A flower with centre location</li>
 *     <li>A wilted flower</li>
 *     <li>A stem</li>
 * </ul>
 *
 * The flower has three states
 * <ol>
 *     <li>Wilted</li>
 *     <li>Blooming</li>
 *     <li>Dying (sinking below view)</li>
 * </ol>
 *
 * Created by Peter Davis on 11/11/2016.
 */
public class DroopFlowerPhysics extends AbstractPhysics {

    private FrameSprite flower;
    private FrameSprite stem;
    private long ts;
    private int frame;

    public DroopFlowerPhysics(FrameSprite f, FrameSprite s, Point2f px, Vector2f vx) {
        flower = f;
        stem = s;
        flower.setOffset(0, new Point2f(flower.getWidth(0) / 2, flower.getHeight(0) / 10));
        p = new Point2f(px);
        v = new Vector2f(vx);
        ts = System.currentTimeMillis();

        stem.setOffset(0, new Point2f(stem.getWidth(0) / 2, 0));
    }

    @Override
    public void update(long ts) {
        float t = (ts - this.ts) / 1000.0f; // number of seconds between updates ?
        p.x += (v.x * t);
        p.y += (v.y * t);

        this.ts = ts;

    }

    @Override
    public int getFrame() {
        return frame;
    }

    @Override
    public Point2f getSize() {
        return flower.getSize(0);
    }

    @Override
    public Point2f getOffset() {
        return flower.getOffset(0);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        stem.draw(canvas, p.x, p.y, paint, 0);
        flower.draw(canvas, p.x, p.y, paint, 0);
    }
}
