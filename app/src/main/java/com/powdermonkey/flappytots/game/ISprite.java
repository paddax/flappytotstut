package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public interface ISprite {

    boolean collide(ISprite s);

    void draw(Canvas canvas, int x, int y, Paint paint, int frame);

    int getFrameCount();

}
