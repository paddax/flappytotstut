package com.powdermonkey.flappytots.gameold;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class FPS {

    private long ts;
    private float fps;

    public FPS() {
        ts = System.currentTimeMillis();
    }

    public float fps() {
        long tmp = System.currentTimeMillis();

        if(tmp > 0) {
            fps = 1000.0F / (tmp - ts);
        }
        ts = tmp;
        return fps;
    }

    public float getFPS() {
        return fps;
    }
}
