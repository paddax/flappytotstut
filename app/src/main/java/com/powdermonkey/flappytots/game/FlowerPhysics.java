package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.List;

/**
 * Created by Peter Davis on 09/10/2016.
 */
public class FlowerPhysics extends AbstractPhysics {

    private int mode;
    private long ts;
    private float frame;
    private FlowerSprite flower;
    private boolean collision;
    public enum ThatsLife {WILTING, GROWING, DYING, DEAD};
    ThatsLife thatsLife;
    private long collideTime;

    /**
     * Constructs an object that moves left across the screen
     *
     * @param x Initial x position
     * @param y Initial y position
     * @param vx Vector magnitude of Y in pixels per second
     */
    public FlowerPhysics(float x, float y, float vx) {
        p = new PointF(x, y);
        v = new PointF(vx, 0);
        ts = System.currentTimeMillis();
        mode = 0;
        thatsLife = ThatsLife.WILTING;
    }

    @Override
    public void update(long ts) {
        float t = (ts - this.ts) / 1000.0f; // number of seconds between updates ?
        p.x += (v.x * t);
        p.y += (v.y * t);

        frame += ((ts - this.ts) / 50.0f);
        frame %= sprite.getFrameCount();

        if (collision || mode > 2) {
            t = (ts - collideTime) / 1000.0f; // seconds since collision
            if (t > 2.2) {
                thatsLife = ThatsLife.DEAD;
                mode = 3;
            } else if (t > 0.3) {
                mode = 3;
                thatsLife = ThatsLife.DYING;
            } else if (t > 0.) {
                thatsLife = ThatsLife.GROWING;
                mode = 2;
            } else {
                thatsLife = ThatsLife.GROWING;
                mode = 1;
            }
        }
        this.ts = ts;
    }

    @Override
    public void setSprite(ISprite sprite) {
        if (sprite instanceof FlowerSprite) {
            this.sprite = sprite;
            this.flower = (FlowerSprite) sprite;
            regions = new RegionSet(sprite.getRegions());
        } else {
            throw new RuntimeException("FlowerPhysics requires FlowerSprite");
        }
    }


    @Override
    public int getFrame() {
        return (int) frame;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        flower.setMode(mode);
        sprite.draw(canvas, p.x, p.y, paint, (int) frame);

        if(drawCollisionRegions) {
            List<? extends IRegion> rr = regions.frames.get(getFrame());
            for(IRegion r: rr) {
                r.draw(canvas, paint);
            }
        }
    }

    public void setCollision(boolean collision) {
        if(thatsLife == ThatsLife.GROWING || thatsLife == ThatsLife.WILTING) {
            if (!this.collision && collision) {
                collideTime = System.currentTimeMillis();
                flower.setMode(0);
            }
            if (this.collision && !collision) {
                flower.setMode(0);
            }
            this.collision = collision;
        }
    }

    public boolean isDead() {
        return thatsLife == ThatsLife.DEAD;
    }
}
