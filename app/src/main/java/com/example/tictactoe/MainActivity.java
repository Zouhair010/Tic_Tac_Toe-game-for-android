package com.example.tictactoe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;


/**
 * The main entry point of the application. This activity serves as a menu screen,
 * allowing the user to choose between playing against the computer or against another player.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display for a more immersive UI that uses the full screen.
        EdgeToEdge.enable(this);
        // Set the user interface layout for this activity from the activity_main.xml file.
        setContentView(R.layout.activity_main);

        // Set a listener on the main layout view to handle window insets.
        // This ensures that UI elements are not obscured by system bars (like the status bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Get the insets for the system bars.
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as padding to the view, preventing content from overlapping with system UI.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set a click listener for the "Player vs Computer" button.
        findViewById(R.id.vsComputeEasyLevelBtn).setOnClickListener(v -> {
            // Create an Intent to start the FirstActivity (Player vs Computer game).
            Intent intent = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(intent);
            // Apply a slide animation for the activity transition.
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            // Finish the current MainActivity so the user cannot navigate back to it with the back button.
            finish();
        });

        // Set a click listener for the "Player vs Player" button.
        findViewById(R.id.playerVsPlayerModeBtn).setOnClickListener(v -> {
            // Create an Intent to start the SecondActivity (Player vs Player game).
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            // Apply a slide animation for the activity transition.
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            // Finish the current MainActivity.
            finish();
        });

        findViewById(R.id.vsComputeHardLevelBtn).setOnClickListener(v -> {
            // Create an Intent to start the SecondActivity (Player vs Player game).
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(intent);
            // Apply a slide animation for the activity transition.
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            // Finish the current MainActivity.
            finish();
        });
    }
}