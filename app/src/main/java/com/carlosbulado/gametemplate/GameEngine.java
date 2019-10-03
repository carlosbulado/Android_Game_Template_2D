package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

    Random r = new Random();
    int fpsEnemyCount = 0;
    int fpsLifeCount = 0;

    // ------------------------------------------------
    // GAME SPECIFIC VARIABLES
    // ------------------------------------------------
    int hp;
    int score;

    // ------------------------------------------------
    // ## SPRITES
    // ------------------------------------------------
    Sprite player;
    List<Sprite> enemies;

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
        this.player = new Sprite(context, this.screenWidth - 50, this.screenHeight - 850, R.drawable.dino64);
        this.enemies = new ArrayList<>();

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
        while (true)
        {
            if(gameIsRunning)
            {
                this.updatePositions();
                this.redrawSprites();
                this.setFPS();
            }
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
        this.fpsEnemyCount += 1;
        this.fpsLifeCount += 1;
        // @TODO: Update position of all sprites

        // @TODO: Logic of the game
        int isSpawnObject = this.r.nextInt(12) + 1;
        if(this.fpsEnemyCount == 40)
        {
            this.fpsEnemyCount = 0;
            Sprite enemy = new Sprite(this.getContext(), 100, 100, R.drawable.poop32);

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

            //@TODO: Draw sprites
            this.canvas.drawBitmap(this.player.getSpriteImage(), this.player.getX(), this.player.getY(), paintbrush);

            Rect lane1 = new Rect(100, this.screenHeight - 920, this.screenWidth - 70, this.screenHeight - 910);
            Rect lane2 = new Rect(100, this.screenHeight - 620, this.screenWidth - 70, this.screenHeight - 610);
            Rect lane3 = new Rect(100, this.screenHeight - 320, this.screenWidth - 70, this.screenHeight - 310);
            Rect lane4 = new Rect(100, this.screenHeight - 20, this.screenWidth - 70, this.screenHeight - 10);

            this.paintbrush.setColor(Color.GRAY);
            this.canvas.drawRect(lane1, paintbrush);
            this.canvas.drawRect(lane2, paintbrush);
            this.canvas.drawRect(lane3, paintbrush);
            this.canvas.drawRect(lane4, paintbrush);

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
        /*
        Y positions for dino = 250 / 550 / 850 / 1150 OR  + - 300
         */
        if (userAction == MotionEvent.ACTION_DOWN)
        {

        }
        else if (userAction == MotionEvent.ACTION_UP)
        {

        }

        return true;
    }
}