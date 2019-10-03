package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

public class Player extends Item
{
    public Player(Context c, int xPosition, int yPosition, int w, int h) {
        super(c, xPosition, yPosition, w, h);
    }

    public Player(Context c, int xPosition, int yPosition, int drawable) {
        super(c, xPosition, yPosition, drawable);
    }
}
