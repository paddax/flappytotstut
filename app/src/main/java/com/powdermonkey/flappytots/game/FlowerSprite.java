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

public class FlowerSprite implements ISprite {

    private final Bitmap image[] = new Bitmap[7];
    private final int frames;
    private final int height;
    private final int width;
    private RegionSet regions;
    private int mode;

    /**
     * The src bitmap is rescaled too width and height
     * @param src Source bitmap
     * @param width Width of final bitmap
     * @param height Height of final bitmap
     * @param frames Number of frames to generate
     */
    public FlowerSprite(Bitmap src, int width, int height, int frames) {
        this.frames = frames;
        this.width  = width;
        this.height = height;
        regions = new RegionSet(new Circle2dF(0,0,width*6/13), frames);

        image[0] = Bitmap.createScaledBitmap(src, width, height, true);
    }

    public void setModeBitmaps(Bitmap g1, Bitmap g2, Bitmap[] e1) {
        image[1] = Bitmap.createScaledBitmap(g1, width, height, true);
        image[2] = Bitmap.createScaledBitmap(g2, width, height, true);
        image[3] = Bitmap.createScaledBitmap(e1[0], width, height, true);
        image[4] = Bitmap.createScaledBitmap(e1[1], width, height, true);
        image[5] = Bitmap.createScaledBitmap(e1[2], width, height, true);
        image[6] = Bitmap.createScaledBitmap(e1[3], width, height, true);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public RegionSet getRegions() {
        return regions;
    }

    @Override
    public void draw(Canvas canvas, float x, float y, Paint paint, int frame) {
        int i = frame % frames;
        Matrix matrix = new Matrix();
        if(mode < 3)
            matrix.postRotate(360.0f * i / frames, width / 2, height / 2);
        matrix.postTranslate(x - (width >> 1), y - (height >> 1));
        canvas.drawBitmap(image[mode], matrix, paint);
    }


    @Override
    public int getFrameCount() {
        return frames;
    }

    public int getHeight(int frame) {
        return height;
    }
    public int getWidth(int frame) {
        return width;
    }

}
