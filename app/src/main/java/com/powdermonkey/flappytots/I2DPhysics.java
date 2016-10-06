package com.powdermonkey.flappytots;

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
     * Location X of object
     * @return curent x
     */
    float getX();

    /**
     * Location Y of object
     * @return current y
     */
    float getY();

    /**
     * Current frame where a number of frames represent the object
     * @return current frame
     */
    int getFrame();
}
