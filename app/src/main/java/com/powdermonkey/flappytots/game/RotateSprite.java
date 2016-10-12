package com.powdermonkey.flappytots.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;

import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.Circle2dF;
import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.Rect2dF;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates an animated rotatable image from a single image.
 *
 * Created by Peter Davis on 05/10/2016.
 */

public class RotateSprite implements ISprite {

    private final Bitmap[] images;
    private RegionSet regions;

    /**
     * The src bitmap is rescaled too width and height
     * @param src Source bitmap
     * @param width Width of final bitmap
     * @param height Height of final bitmap
     * @param frames Number of frames to generate
     */
    public RotateSprite(Bitmap src, int width, int height, int frames) {
        images = new Bitmap[frames];
        Matrix matrix = new Matrix();
        regions = new RegionSet(new Circle2dF(0,0,width*6/13), frames);

        for(int i=0; i<frames; i++) {
            images[i] = Bitmap.createScaledBitmap(src, width, height, false);
            images[i] = Bitmap.createBitmap(images[i], 0, 0, width, height, matrix, false);
            matrix.setRotate((360.0f / frames) * i, width / 2, height / 2);
        }
    }

    @Override
    public RegionSet getRegions() {
        return regions;
    }

    @Override
    public void draw(Canvas canvas, float x, float y, Paint paint, int frame) {
        int i = frame % images.length;
        canvas.drawBitmap(images[i], x - images[i].getWidth() / 2, y - images[i].getHeight() / 2, paint);
    }


    @Override
    public int getFrameCount() {
        return images.length;
    }

    public int getHeight(int frame) {
        return images[frame % images.length].getHeight();
    }
    public int getWidth(int frame) {
        return images[frame % images.length].getWidth();
    }

}
