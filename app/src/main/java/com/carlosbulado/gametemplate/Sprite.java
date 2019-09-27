package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Sprite
{
    private Context context;
    private int initX;
    private int initY;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private Rect hitbox;
    private boolean isMoving;
    private Bitmap spriteImage;
    private int spriteDrawableImage;

    public Sprite(Context c, int xPosition, int yPosition, int w, int h) {
        this.context = c;
        this.initX = xPosition;
        this.x = xPosition;
        this.initY = yPosition;
        this.y = yPosition;
        this.width = w;
        this.height = h;
        this.isMoving = false;
        this.speed = 1;

        this.hitbox = new Rect(this.x, this.y,this.x + this.width,this.y + this.height);
    }

    public Sprite(Context c, int xPosition, int yPosition, int drawable) {
        this.context = c;
        this.initX = xPosition;
        this.x = xPosition;
        this.initY = yPosition;
        this.y = yPosition;
        this.spriteDrawableImage = drawable;
        this.spriteImage = BitmapFactory.decodeResource(c.getResources(), drawable);
        this.width = this.spriteImage.getWidth();
        this.height = this.spriteImage.getHeight();
        this.isMoving = false;
        this.speed = 1;

        this.hitbox = new Rect(this.x, this.y,this.x + this.width,this.y + this.height);
    }

    public Rect getHitBox() { return this.hitbox; }

    public boolean isMoving() { return isMoving; }

    public void setIsMoving(boolean isMoving) { this.isMoving = isMoving; }

    public void updatePosition(int x, int y)
    {
        if(this.isMoving())
        {
            this.x += x;
            this.y += y;
            this.updateHitBox();
        }
    }

    public void updateHitBox()
    {
        hitbox.left = this.x;
        hitbox.top = this.y;
        hitbox.right = this.x + this.width;
        hitbox.bottom = this.y + this.height;
    }

    public void respawn()
    {
        this.x = this.initX;
        this.y = this.initY;
        updateHitBox();
    }
}
