package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameEngine extends SurfaceView implements Runnable
{
    // Android debug variables
    static String TAG = "GAME TEMPLATE";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;

    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    // ------------------------------------------------
    // GAME SPECIFIC VARIABLES
    // ------------------------------------------------
    int hp;
    int score;

    // ------------------------------------------------
    // ## SPRITES
    // ------------------------------------------------
    Sprite player;

    // ------------------------------------------------
    // ## GAME STATS
    // ------------------------------------------------

    public GameEngine(Context context, int w, int h)
    {
        super(context);

        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        this.printScreenInfo();

        // @TODO: Add your sprites
        this.player = new Sprite(context, this.screenWidth - 50, this.screenHeight / 2, R.drawable.dino64);

        // @TODO: Any other game setup
        this.hp = 5;
        this.score = 0;
    }

    private void printScreenInfo() { Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight); }

    // ------------------------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------------------------
    @Override
    public void run()
    {
        while (gameIsRunning == true)
        {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }

    public void pauseGame()
    {
        gameIsRunning = false;
        try { gameThread.join(); }
        catch (InterruptedException e) { }
    }

    public void startGame()
    {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // ------------------------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------------------------

    public void updatePositions()
    {
        // @TODO: Update position of all sprites

        // @TODO: Logic of the game

        //@TODO: Test lose conditions

        //@TODO: Test win conditions

    }

    public void redrawSprites()
    {
        if (this.holder.getSurface().isValid())
        {
            // ------------------------------------------------
            this.canvas = this.holder.lockCanvas();
            // ------------------------------------------------

            // configure the drawing tools
            this.canvas.drawColor(Color.WHITE);
            this.paintbrush.setColor(Color.BLACK);

            //@TODO: Draw sprites
            this.canvas.drawBitmap(this.player.getSpriteImage(), this.player.getX(), this.player.getY(), paintbrush);

            //@TODO: Draw game stats
            this.paintbrush.setTextSize(80);
            this.paintbrush.setColor(Color.BLUE);
            this.canvas.drawText("LIVES: " + this.hp, this.screenWidth / 2, 100, this.paintbrush);
            this.canvas.drawText("SCORE: " + this.score, this.screenWidth - 400, 100, this.paintbrush);

            // ------------------------------------------------
            this.holder.unlockCanvasAndPost(canvas);
            // ------------------------------------------------
        }
    }

    public void setFPS()
    {
        //@TODO: How many times the screen will update
        try { gameThread.sleep(50); }
        catch (Exception e) { }
    }

    // ------------------------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN)
        {

        }
        else if (userAction == MotionEvent.ACTION_UP)
        {

        }

        return true;
    }
}