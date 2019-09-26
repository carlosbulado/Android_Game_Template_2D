package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpaceShip
{
    private int x;
    private int y;

    private Bitmap spaceShipImg;
    private Rect spaceShipArea;

    public SpaceShip(int x, int y, int drawable, Context context)
    {
        this.x = x;
        this.y = y;

        this.spaceShipImg = BitmapFactory.decodeResource(context.getResources(), drawable);
    }

    public void setX(int x) { this.x = x; }

    public int getX() { return x; }

    public void setY(int y) { this.y = y; }

    public int getY() { return y; }

    public Rect getSpaceShipArea() { return spaceShipArea; }

    public int getSpaceShipBottom() { return this.y + this.spaceShipImg.getHeight(); }

    public void moveSpaceShipOnXAxis(int movement) { this.x += movement; }

    public void moveSpaceShipOnYAxis(int movement) { this.y += movement; }

    public void drawSpaceShip(Canvas canvas, Paint paintbrush)
    {
        canvas.drawBitmap(this.spaceShipImg, this.x, this.y, paintbrush);
        this.spaceShipArea = new Rect(this.x,
                this.y,
                this.x + this.spaceShipImg.getWidth(),
                this.y + this.spaceShipImg.getHeight()
        );
        canvas.drawRect(this.spaceShipArea, paintbrush);
    }

    public void setBottom(int bottom) { this.y = bottom - this.spaceShipImg.getHeight(); }

    public boolean hits(SpaceShip another) { return this.spaceShipArea != null && another.getSpaceShipArea() != null && this.spaceShipArea.intersect(another.getSpaceShipArea()); }
}
