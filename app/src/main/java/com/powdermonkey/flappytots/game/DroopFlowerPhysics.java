package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.geometry.Circle2dF;
import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.Rect2dF;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.List;

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

    private final RegionSet flowhit;
    private FrameSprite flower;
    private FrameSprite stem;
    private long ts;
    private int frame;
    private Rect2dF fail;

    public enum EHitType { MISS, KISS, KILL };

    public DroopFlowerPhysics(FrameSprite f, FrameSprite s, Point2f px, Vector2f vx) {
        flower = f;
        stem = s;
        flower.setOffset(0, new Point2f(flower.getWidth(0) / 2, flower.getHeight(0) / 10));
        p = new Point2f(px);
        v = new Vector2f(vx);
        ts = System.currentTimeMillis();
        flowhit = f.getRegions();

        fail = new Rect2dF(-s.getWidth(0) / 2, f.getHeight(0) / 3, s.getWidth(0) / 2, s.getHeight(0));
        stem.setOffset(0, new Point2f(stem.getWidth(0) / 2, 0));
    }

    @Override
    public void update(long ts) {
        float t = (ts - this.ts) / 1000.0f; // number of seconds between updates ?
        p.x += (v.x * t);
        p.y += (v.y * t);
        flowhit.move(frame, p);
        fail.move(p);

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

    public EHitType collide(List<? extends IRegion> reg) {
        EHitType result = EHitType.MISS;
        for(IRegion r: reg) {
            if(flowhit.intersect(frame, r)) {
                result = EHitType.KISS;
                frame = 1;
            }
            if(r.intersect(fail)) {
                v.y = v.x * -10;
                return EHitType.KILL;
            }
        }
        return result;
    }

    @Override
    public Point2f getOffset() {
        return flower.getOffset(0);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        stem.draw(canvas, p.x, p.y, paint, 0);
        flower.draw(canvas, p.x, p.y, paint, frame);
//        paint.setColor(Color.argb(255, 0, 0, 255));
//        flowhit.draw(frame, canvas, paint);
//        paint.setColor(Color.argb(255, 0, 255, 0));
//        fail.draw(canvas, paint);
    }
}
