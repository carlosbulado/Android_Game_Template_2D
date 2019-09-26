package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
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

    // Random
    Random random = new Random();

    // ------------------------------------------------
    // GAME SPECIFIC VARIABLES
    // ------------------------------------------------

    // ------------------------------------------------
    // ## SPRITES
    // ------------------------------------------------
    SpaceShip player;
    List<SpaceShip> enemies;

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
        this.enemies = new ArrayList<>();
        this.initEnemies();

        // put the initial starting position of your player
        this.player = new SpaceShip(100, 600, R.drawable.player_ship, this.getContext());

        // @TODO: Any other game setup

    }

    private void printScreenInfo() { Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight); }

    private void initEnemies()
    {
        this.enemies.add(new SpaceShip(this.screenWidth - 100, this.random.nextInt(this.screenHeight) + 1, R.drawable.alien_ship2, this.getContext()));
        this.enemies.add(new SpaceShip(this.screenWidth - 100, this.random.nextInt(this.screenHeight) + 1, R.drawable.alien_ship2, this.getContext()));
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
        if (this.fingerAction == "mousedown")
        {
            // if mousedown, then move player up
            // Make the UP movement > than down movement - this will
            // make it look like the player is moving up alot
            this.player.moveSpaceShipOnYAxis(-100);

            if(this.player.getY() < 0) this.player.setY(0);
        }

        if (this.fingerAction == "mouseup")
        {
            // if mouseup, then move player down
            this.player.moveSpaceShipOnYAxis(10);

            if(this.player.getSpaceShipBottom() > this.screenHeight)
            {
                this.player.setBottom(this.screenHeight);
            }
        }
        // @TODO: Logic of the game
        List<SpaceShip> newEnemies = new ArrayList<>();
        List<SpaceShip> removeEnemies = new ArrayList<>();
        for (SpaceShip ship : this.enemies)
        {
            ship.moveSpaceShipOnXAxis(-25);

            if (ship.getX() <= 0)
            {
                removeEnemies.add(ship);
                // restart the enemy in the starting position
                int i1 = this.random.nextInt(2) + 1;
                if(i1 == 1) newEnemies.add(new SpaceShip(this.screenWidth - 100, this.random.nextInt(this.screenHeight) + 1, R.drawable.alien_ship1, this.getContext()));
                else if(i1 == 2) newEnemies.add(new SpaceShip(this.screenWidth - 100, this.random.nextInt(this.screenHeight) + 1, R.drawable.alien_ship2, this.getContext()));
                else newEnemies.add(new SpaceShip(this.screenWidth - 100, this.random.nextInt(this.screenHeight) + 1, R.drawable.alien_ship3, this.getContext()));
            }
        }

        this.enemies.removeAll(removeEnemies);
        this.enemies.addAll(newEnemies);

        //@TODO: Test lose conditions
        for (SpaceShip ship : this.enemies)
        {
            if(this.player.hits(ship))
            {
                this.pauseGame();
            }
        }

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
            // draw player graphic on screen
            this.player.drawSpaceShip(this.canvas, this.paintbrush);

            // draw the enemy graphic on the screen
            for (SpaceShip ship : this.enemies) ship.drawSpaceShip(this.canvas, this.paintbrush);

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

            if(!this.gameIsRunning)
            {
                this.startGame();
            }
        }
        else if (userAction == MotionEvent.ACTION_UP)
        {
            fingerAction = "mouseup";
        }

        return true;
    }
}
