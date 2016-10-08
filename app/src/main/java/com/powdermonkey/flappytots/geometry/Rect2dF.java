package com.powdermonkey.flappytots.geometry;

import android.graphics.RectF;

/**
 * Created by Peter Davis on 08/10/2016.
 */

public class Rect2dF extends RectF implements IRegion {

    @Override
    public boolean intersect(Circle2dF c) {
        return Circle2dF.intersect(c, this);
    }
}
