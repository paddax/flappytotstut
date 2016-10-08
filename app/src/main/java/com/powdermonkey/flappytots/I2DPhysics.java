package com.powdermonkey.flappytots;

import android.graphics.PointF;

/**
 * Created by Peter Davis on 06/10/2016.
 */
public interface I2DPhysics {

    /**
     * Updates the object
     * @param ts Timestamp of this update
     */
    void update(long ts);

    /**
     * Location of object
     * @return curent location
     */
    PointF getPoint();

    PointF getVector();

    void setVector(float x, float y);

    /**
    /**
     * Current frame where a number of frames represent the object
     * @return current frame
     */
    int getFrame();

    void setPoint(float x, float y);
}
