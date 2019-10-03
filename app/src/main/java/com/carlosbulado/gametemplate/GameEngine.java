package com.carlosbulado.gametemplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
    int highScore = 0;
    boolean enemyUp = true;
    int yPosEnemy = 250;
    int newPlayerY;

    int poop = R.drawable.poop64;
    int rainbow = R.drawable.rainbow64;
    int candy = R.drawable.candy64;

    // ------------------------------------------------
    // ## SPRITES
    // ------------------------------------------------
    Player player;
    List<Item> enemies;

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
        newPlayerY = this.screenHeight - 850;
        this.player = new Player(context, this.screenWidth - 50, newPlayerY, R.drawable.dino64);
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
        if(this.highScore < this.score) this.highScore = this.score;
    }

    public void startGame()
    {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void restartGame()
    {
        this.score = 0;
        this.hp = 5;
        gameIsRunning = true;
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
        this.player.moveJustY(newPlayerY);

        for (Item enemy : this.enemies) enemy.moveJustX();

        // @TODO: Logic of the game
        if(this.fpsEnemyCount == 20)
        {
            this.fpsEnemyCount = 0;
            int itemNow = poop;
            switch ((r.nextInt(4) + 1) % 3)
            {
                case 1:
                    itemNow = candy;
                    break;
                case 2:
                    itemNow = rainbow;
                    break;
            }
            Item enemy = new Item(this.getContext(), 0, this.screenHeight - this.yPosEnemy, itemNow);
            if(enemyUp && yPosEnemy == 1150) enemyUp = false;
            if(!enemyUp && yPosEnemy == 250) enemyUp = true;
            yPosEnemy += enemyUp ? 300 : (-300);
            enemy.setSpeed(r.nextInt(100) + 30);
            this.enemies.add(enemy);
        }
        List<Item> toBeRemoved = new ArrayList<>();
        for (Item enemy : this.enemies)
        {
            if((enemy.getspriteDrawableImage() == candy
                    || enemy.getspriteDrawableImage() == rainbow)
                    && this.player.getHitBox().intersect(enemy.getHitBox()))
            {
                this.score++;
                toBeRemoved.add(enemy);
            }
            if(enemy.getspriteDrawableImage() == poop
                    && this.player.getHitBox().intersect(enemy.getHitBox()))
            {
                this.hp--;
                toBeRemoved.add(enemy);
            }
            if(enemy.getHitBox().left > this.screenWidth + 100) toBeRemoved.add(enemy);
        }
        //@TODO: Test lose conditions
        if(this.hp < 0)
        {
            this.pauseGame();
        }
        //@TODO: Test win conditions

        // Removing all Items that need to be removed
        this.enemies.removeAll(toBeRemoved);
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
            if(gameIsRunning)
            {
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

                for (Item enemy : this.enemies) this.canvas.drawBitmap(enemy.getSpriteImage(), enemy.getX(), enemy.getY(), paintbrush);

                //@TODO: Draw game stats
                this.paintbrush.setTextSize(80);
                this.paintbrush.setColor(Color.BLUE);
                this.canvas.drawText("LIVES: " + this.hp, this.screenWidth / 2, 100, this.paintbrush);
                this.canvas.drawText("SCORE: " + this.score + " ( " + this.highScore + " )", this.screenWidth - 500, 100, this.paintbrush);

                // --------------- DEBUG ---------------
                // If you want to know if the enemies are being removed, uncomment this line below
                //this.canvas.drawText("ENEMIES: " + this.enemies.size(), 100, 100, this.paintbrush);

            }
            else
            {
                this.paintbrush.setTextSize(150);
                this.paintbrush.setColor(Color.BLACK);
                this.canvas.drawText("-->      YOU LOST!     <--", 300, 300, this.paintbrush);
                this.canvas.drawText("--> TAP TO RESTART <--", 300, 600, this.paintbrush);
                this.paintbrush.setColor(Color.BLUE);
                this.canvas.drawText("Score: " + this.score, 300, 800, this.paintbrush);
                this.canvas.drawText("Highest Score: " + this.highScore, 300, 1000, this.paintbrush);
            }

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
            if(gameIsRunning)
            {
                float y = event.getY();
                if(y > this.screenHeight / 2 && this.player.getY() < this.screenHeight - 250) newPlayerY = (this.player.getY() + 300);
                if(y < this.screenHeight / 2 && this.player.getY() > this.screenHeight - 1150) newPlayerY = (this.player.getY() - 300);
            }
            else
            {
                this.restartGame();
            }
        }

        return true;
    }
}