package com.powdermonkey.flappytots.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.powdermonkey.flappytots.AbstractPhysics;
import com.powdermonkey.flappytots.ISprite;

import javax.vecmath.Point2f;

/**
 * Simple moving left character that consists of 2 sprites.
 * <ul>
 *     <li>A flower with centre location</li>
 *     <li>A wilted flower</li>
 *     <li>A stem</li>
 * </ul>
 *
 * The flower has three states
 * <ol>
 *     <li>Wilted</li>
 *     <li>Blooming</li>
 *     <li>Dying (sinking below view)</li>
 * </ol>
 *
 * Created by Peter Davis on 11/11/2016.
 */
public class DroopFlowerPhysics extends AbstractPhysics {

    private ISprite flower;
    private ISprite stem;

    @Override
    public void update(long ts) {

    }

    @Override
    public int getFrame() {
        return 0;
    }

    @Override
    public Point2f getSize() {
        return null;
    }

    @Override
    public Point2f getOffset() {
        return null;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

    }
}
