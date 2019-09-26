package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Rect;

public class Sprite
{
    private Context context;
    private int initX;
    private int initY;
    protected int x;
    protected int y;
    private int width;
    private int height;
    private Rect hitbox;
    private boolean ismoving;

    public Sprite(Context c, int xPosition, int yPosition, int w, int h) {
        this.context = c;
        this.initX = xPosition;
        this.x = xPosition;
        this.initY = yPosition;
        this.y = yPosition;
        this.width = w;
        this.height = h;
        this.ismoving = false;

        this.hitbox = new Rect(this.x, this.y,this.x + w,this.y + h);
    }

    public void updatePosition(int x, int y)
    {
        if(this.isIsmoving())
        {
            this.x += x;
            this.y += y;
            this.updateHitbox();
        }
    }

    public void updateHitbox() {
        hitbox.left = this.x;
        hitbox.top = this.y;
        hitbox.right = this.x + this.width;
        hitbox.bottom = this.y + this.height;
    }

    public Rect getHitbox() {
        return this.hitbox;
    }

    public boolean isIsmoving() { return ismoving; }

    public void setIsmoving(boolean ismoving) { this.ismoving = ismoving; }

    public void respawn()
    {
        this.x = this.initX;
        this.y = this.initY;
        updateHitbox();
    }
}
