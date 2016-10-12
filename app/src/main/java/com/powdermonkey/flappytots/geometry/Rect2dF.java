package com.powdermonkey.flappytots.geometry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Peter Davis on 08/10/2016.
 */

public class Rect2dF implements IRegion {

    private RectF defined;
    RectF active;

    public Rect2dF(float left, float top, float right, float bottom) {
        defined = new RectF(left, top, right, bottom);
        active = new RectF(defined);
    }

    public Rect2dF(RectF r) {
        this(r.left, r.top, r.right, r.bottom);
    }


    @Override
    public boolean intersect(IRegion r) {
        if(r.getType() == CIRCLE)
            return Circle2dF.intersect((Circle2dF) r, this);
        else
            return active.intersect(((Rect2dF)r).active);
    }

    @Override
    public void move(float x, float y) {
        active.set(defined);
        active.offset(x, y);
    }

    @Override
    public void move(PointF p) {
        this.move(p.x, p.y);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(active, paint);
    }

    @Override
    public void scale(float x, float y) {
        defined.left *= x;
        defined.right *= x;
        defined.top *= y;
        defined.bottom *= y;
    }

    @Override
    public int getType() {
        return RECT;
    }

    @Override
    public void offset(float x, float y) {
        defined.offset(x, y);
    }

    @Override
    public IRegion copy() {
        return new Rect2dF(defined);
    }
}
