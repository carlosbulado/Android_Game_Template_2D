package com.carlosbulado.gametemplate;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    GameEngine gameTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get size of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Initialize the GameEngine object
        // Pass it the screen size (height & width)
        // 256 is the height escape for landscape
        // 283 is the height escape for portrait
        gameTemplate = new GameEngine(this, size.x, size.y - 256);

        // Make GameEngine the view of the Activity
        setContentView(gameTemplate);
    }

    // Android Lifecycle function
    @Override
    protected void onResume()
    {
        super.onResume();
        gameTemplate.startGame();
    }

    // Stop the thread in gameEngine
    @Override
    protected void onPause()
    {
        super.onPause();
        gameTemplate.pauseGame();
    }
}
