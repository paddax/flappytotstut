package com.powdermonkey.flappytots;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Peter Davis on 09/10/2016.
 */

public abstract class AbstractPhysics implements I2DPhysics {

    protected PointF p;
    protected PointF v;
    protected ISprite sprite;
    protected boolean drawCollisionRegions = true;

    @Override
    public PointF getPoint() {
        return p;
    }

    @Override
    public PointF getVector() {
        return v;
    }

    @Override
    public void setVector(float x, float y) {
        v.x = x;
        v.y = y;
    }

    @Override
    public void setPoint(float x, float y) {
        p.x = x;
        p.y = y;
    }

    @Override
    public ISprite getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(ISprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void updateRegions() {
        sprite.updateRegions(getPoint(), getFrame());
    }
}
