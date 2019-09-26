package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Rect;

public class Sprite
{
    private Context context;
    protected int x;
    protected int y;
    private int width;
    private int height;
    private Rect hitbox;

    public Sprite(Context c, int xPosition, int yPosition, int w, int h) {
        this.context = c;
        this.x = xPosition;
        this.y = yPosition;
        this.width = w;
        this.height = h;

        this.hitbox = new Rect(this.x, this.y,this.x + w,this.y + h);
    }

    public void updatePosition() {
        // override this in the child class
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
}
