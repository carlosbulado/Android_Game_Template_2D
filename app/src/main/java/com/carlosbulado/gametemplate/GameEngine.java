package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable
{
    // Android debug variables
    static String TAG = "TAPPY SPACESHIP";

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

    // ------------------------------------------------
    // ## SPRITES
    // ------------------------------------------------

    int playerXPosition;
    int playerYPosition;

    int enemyXPosition;
    int enemyYPosition;

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

        // put initial starting postion of enemy
        this.enemyXPosition = 1300;
        this.enemyYPosition = 120;

        // put the initial starting position of your player
        this.playerXPosition = 100;
        this.playerYPosition = 600;

        // @TODO: Any other game setup

    }

    private void printScreenInfo() { Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight); }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

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
        if (this.fingerAction == "mousedown") {
            // if mousedown, then move player up
            // Make the UP movement > than down movement - this will
            // make it look like the player is moving up alot
            this.playerYPosition = this.playerYPosition - 100;
        }

        if (this.fingerAction == "mouseup") {
            // if mouseup, then move player down
            this.playerYPosition = this.playerYPosition + 10;
        }
        // @TODO: Logic of the game
        this.enemyXPosition = this.enemyXPosition - 25;

        if (this.enemyXPosition <= 0) {
            // restart the enemy in the starting position
            this.enemyXPosition = 1300;
            this.enemyYPosition = 120;
        }

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

            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            //@TODO: Draw sprites
            Bitmap playerImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                    R.drawable.player_ship);
            canvas.drawBitmap(playerImage, playerXPosition, playerYPosition, paintbrush);

            Bitmap ememyImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                    R.drawable.alien_ship2);
            canvas.drawBitmap(ememyImage, enemyXPosition, enemyYPosition, paintbrush);

            //@TODO: Draw game stats

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

    String fingerAction = "";

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN)
        {
            fingerAction = "mousedown";
        }
        else if (userAction == MotionEvent.ACTION_UP)
        {
            fingerAction = "mouseup";
        }

        return true;
    }
}
