package com.powdermonkey.flappytots.geometry;

import android.graphics.RectF;

/**
 * Created by Peter Davis on 07/10/2016.
 */

public interface IRegion {

    boolean intersect(Circle2dF c);

    boolean intersect(RectF r);
}
