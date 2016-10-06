package com.powdermonkey.flappytots;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * A sprite is an animated bitmap.  Decendents are expected to handle some incoming data determine
 * the collision regions and express frames.
 *
 * Created by Peter Davis on 05/10/2016.
 */

public interface ISprite {

    boolean collide(ISprite s);

    void draw(Canvas canvas, int x, int y, Paint paint, int frame);

    int getFrameCount();

}
