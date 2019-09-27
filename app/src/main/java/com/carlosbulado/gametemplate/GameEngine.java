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

public class GameEngine extends SurfaceView implements Runnable
{
    private final String TAG = "VECTOR-MATH";

    // game thread variables
    private Thread gameThread = null;
    private volatile boolean gameIsRunning;

    // drawing variables
    private Canvas canvas;
    private Paint paintbrush;
    private SurfaceHolder holder;

    // Screen resolution varaibles
    private int screenWidth;
    private int screenHeight;

    private int whPlayer = 100;
    private int whOthers = 70;

    // Sprites
    private Sprite player;
    private List<Sprite> allThingsOnScreen;

    public GameEngine(Context context, int screenW, int screenH) {
        super(context);

        // intialize the drawing variables
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        // set screen height and width
        this.screenWidth = screenW;
        this.screenHeight = screenH;

        this.player = new Sprite(this.getContext(), 1400, 200, this.whPlayer, this.whPlayer);

        this.allThingsOnScreen = new ArrayList<>();
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 150, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 300, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 450, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 600, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 750, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 900, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 1050, this.screenHeight - 200, this.whOthers, this.whOthers));
        this.allThingsOnScreen.add(new Sprite(this.getContext(), 1200, this.screenHeight - 200, this.whOthers, this.whOthers));
    }

    @Override
    public void run() {
        // @TODO: Put game loop in here
        while (gameIsRunning == true) {
            updateGame();
            drawGame();
            controlFPS();
        }
    }

    // Game Loop methods
    public void updateGame()
    {
        for (int i = 0 ; i < this.allThingsOnScreen.size() ; i++)
        {
            if(i == 0) this.allThingsOnScreen.get(i).setIsmoving(true);
            else if(this.allThingsOnScreen.get(i - 1).getHitbox().bottom < this.allThingsOnScreen.get(i).getHitbox().top) this.allThingsOnScreen.get(i).setIsmoving(true);
        }

        for (Sprite sp : this.allThingsOnScreen)
        {
            double a = (this.player.getX() - sp.getX());
            double b = (this.player.getY() - sp.getY());
            double distance = Math.sqrt((a*a) + (b*b));

            // 2. calculate the "rate" to move
            double xn = (a / distance);
            double yn = (b / distance);

            // 3. move the bullet
            sp.setX(sp.getX() + (int)(xn * 40));
            sp.setY(sp.getY() + (int)(yn * 40));

            sp.updateHitbox();
            //sp.updatePosition(0, -10);
            if(this.player.getHitbox().intersect(sp.getHitbox()))
            {
//                try {
//                    gameThread.sleep(1000);
//                }
//                catch (InterruptedException e) {
//
//                }
                sp.respawn();
                this.player.respawn();
            }
        }
    }

    public void drawGame() {
        if (holder.getSurface().isValid()) {

            // initialize the canvas
            canvas = holder.lockCanvas();
            // --------------------------------
            // @TODO: put your drawing code in this section

            // set the game's background color
            canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.BLUE);

            canvas.drawRect(this.player.getHitbox(), paintbrush);

            paintbrush.setColor(Color.RED);
            for (Sprite sp : this.allThingsOnScreen) canvas.drawRect(sp.getHitbox(),paintbrush);

            // --------------------------------
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void controlFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (InterruptedException e) {

        }
    }


    // Deal with user input


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_DOWN:
                this.allThingsOnScreen.add(new Sprite(this.getContext(), (int)event.getX(), (int)event.getY(), this.whOthers, this.whOthers));
                break;
        }
        return true;
    }

    // Game status - pause & resume
    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {

        }
    }
    public void  resumeGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
