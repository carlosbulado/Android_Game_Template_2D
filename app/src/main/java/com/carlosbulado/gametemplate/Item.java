package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

public class Item {

    private Context context;
    private int initX;
    private int initY;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private Rect hitBox;
    private boolean isMoving;
    private Bitmap spriteImage;
    private int spriteDrawableImage;

    public Item(Context c, int xPosition, int yPosition, int w, int h) {
        this.context = c;
        this.initX = xPosition;
        this.x = xPosition;
        this.initY = yPosition;
        this.y = yPosition;
        this.width = w;
        this.height = h;
        this.isMoving = true;
        this.speed = 9999;

        this.hitBox = new Rect(this.x, this.y,this.x + this.width,this.y + this.height);
    }

    public Item(Context c, int xPosition, int yPosition, int drawable) {
        this.context = c;
        this.initX = xPosition;
        this.x = xPosition;
        this.initY = yPosition;
        this.y = yPosition;
        this.spriteDrawableImage = drawable;
        this.spriteImage = BitmapFactory.decodeResource(c.getResources(), drawable);
        this.width = this.spriteImage.getWidth();
        this.height = this.spriteImage.getHeight();
        this.isMoving = true;
        this.speed = 9999;

        this.hitBox = new Rect(this.x, this.y,this.x + this.width,this.y + this.height);
    }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public int getSpeed() { return speed; }

    public void setSpeed(int s) { this.speed = s; }

    public Bitmap getSpriteImage() { return spriteImage; }

    public Rect getHitBox() { return this.hitBox; }

    public int getspriteDrawableImage() { return this.spriteDrawableImage; }

    public boolean isMoving() { return isMoving; }

    public void setIsMoving(boolean isMoving) { this.isMoving = isMoving; }

    public void updatePosition(int x, int y)
    {
        if(this.isMoving())
        {
            this.setX(x);
            this.setY(y);
            this.updateHitBox();
        }
    }

    public void moveJustX() { this.updatePosition(this.getX() + this.getSpeed(), this.getY()); }

    public void moveJustY(int y) { this.updatePosition(this.getX(), y); }

    public void updateHitBox()
    {
        hitBox.left = this.x;
        hitBox.top = this.y;
        hitBox.right = this.x + this.width;
        hitBox.bottom = this.y + this.height;
    }

    public void respawn()
    {
        this.x = this.initX;
        this.y = this.initY;
        updateHitBox();
    }

    public void moveTowards(Point moveHere)
    {
        double a = (moveHere.x - this.getX());
        double b = (moveHere.y - this.getY());
        double distance = Math.sqrt((a*a) + (b*b));

        // 2. calculate the "rate" to move
        double xn = (a / distance);
        double yn = (b / distance);

        // 3. move the bullet
        boolean stillMoveX = true;
        boolean stillMoveY = true;

        if(a < 0 && (this.x + (int)(xn * this.speed)) < moveHere.x) stillMoveX = false;
        if(a > 0 && (this.x + (int)(xn * this.speed)) > moveHere.x) stillMoveX = false;

        if(b < 0 && (this.y + (int)(yn * this.speed)) < moveHere.y) stillMoveY = false;
        if(b > 0 && (this.y + (int)(yn * this.speed)) > moveHere.y) stillMoveY = false;

        this.updatePosition(stillMoveX ? this.x + (int)(xn * this.speed) : moveHere.x, stillMoveY ? this.y + (int)(yn * this.speed) : moveHere.y);
    }
}
