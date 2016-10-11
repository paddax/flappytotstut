package com.powdermonkey.flappytots.geometry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Peter Davis on 07/10/2016.
 */

public interface IRegion {

    int CIRCLE = 1;
    int RECT = 2;

    boolean intersect(IRegion r);

    void move(float x, float y);
    void move(PointF p);

    void draw(Canvas canvas, Paint paint);

    void scale(float x, float y);

    int getType();

    void offset(float x, float y);
}
