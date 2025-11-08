package com.example.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SecondActivity extends AppCompatActivity {
    TextView player1ScoursTextView;
    TextView player2ScoursTextView;

    // Array to hold the 9 Button objects that represent the board.
    private Button[] board;
    private static ArrayList<Integer> availableCases =  new ArrayList<>(){{
        add(0);add(1);add(2);
        add(3);add(4);add(5);
        add(6);add(7);add(8);
    }};
    final private static int[][] winCases = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8}
            ,{0,4,8},{2,4,6}
    };
    private static ArrayList<Integer> player1Moves = new ArrayList<>();
    private static ArrayList<Integer> player2Moves = new ArrayList<>();
    final static String player1Symbol = "X";
    final static String player2Symbol = "O";
    private static String turn = player1Symbol;
    private static int player1Scours = 0;
    private static int player2Scours = 0;

    /**
     * Checks if a single integer item is present in a given ArrayList of integers.
     * @param arr The ArrayList to search within.
     * @param item The integer to search for.
     * @return true if the item is found, false otherwise.
     */
    private boolean in(ArrayList<Integer> arr, int item){
        for (int i : arr) {
            if (i == item){
                return true;
            }
        }
        return false;
    }
    private int getIndex(Button button){
        for (int i=0 ; i<board.length ; i++) {
            if (board[i] == button){
                return i;
            }
        }
        return -1;
    }
    /**
     * Checks for a win condition or a draw.
     * @param symbol The symbol ('X' or 'O') of the player whose win is being checked.
     */
    private void checkWiner(ArrayList<Integer> playerMoves, String symbol){
        int counter = 0;
        // Iterate through all possible winning cases.
        for (int[] cs : winCases) {
            counter = 0;
            for (int pos : cs) {
                if (in(playerMoves, pos)){
                    counter++;
                }
            }
            // If the player holds all 3 positions, they win.
            if(counter==3){
                // Highlight the winning buttons in green.
                board[cs[0]].setBackgroundColor(Color.parseColor("#00FF00"));
                board[cs[1]].setBackgroundColor(Color.parseColor("#00FF00"));
                board[cs[2]].setBackgroundColor(Color.parseColor("#00FF00"));
                // Disable all buttons to stop the game.
                for (Button button : board){
                    button.setEnabled(false);
                }
                Toast.makeText(this,"the winer is: "+symbol,Toast.LENGTH_LONG).show();
                if (symbol.equals(player1Symbol)){
                    player1Scours++;
                    runOnUiThread(() -> player1ScoursTextView.setText(
                            "player 1 (\"X\") Scour: 0"+player1Scours
                    ));
                }
                else {
                    player2Scours++;
                    runOnUiThread(() -> player2ScoursTextView.setText(
                            "player 2 (\"O\") Scour: 0"+player2Scours
                    ));
                }
                return; // Exit after a win is found.
            }
        }
        // Check for a draw (all positions filled).
        if (availableCases.size()<1){
            // If it's a draw, set all buttons to blue and end the game.
            for (Button button : board){
                button.setBackgroundColor(Color.parseColor("#0000FF")); // Blue for Draw
            }
            Toast.makeText(this,"it's a draw!",Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void move(Button button){
        if (turn.equals(player1Symbol)){
            player1Moves.add(getIndex(button));
            button.setText(turn);
            button.setBackgroundColor(Color.parseColor("#4676FB"));
            checkWiner(player1Moves,turn);
            turn = player2Symbol;
        }
        else {
            player2Moves.add(getIndex(button));
            button.setText(turn);
            button.setBackgroundColor(Color.parseColor("#F26CE6"));
            checkWiner(player2Moves,turn);
            turn = player1Symbol;
        }
        button.setEnabled(false);
    }
    /**
     * Resets the game board and all internal game state variables for a new round.
     */
    private void restart(){
        // Reset the UI of all board buttons.
        for (Button button : board){
            button.setBackgroundColor(Color.parseColor("#E4E3E3")); // Set to a default background color
            button.setText(""); // Clear the 'X' or 'O' text
            button.setEnabled(true); // Re-enable the button
        }

        // Reset the availableCases list to its initial state (all 8 winning paths).
        availableCases =  new ArrayList<>(){{
            add(0);add(1);add(2);
            add(3);add(4);add(5);
            add(6);add(7);add(8);
        }};
        // Clear all move tracking lists.
        player1Moves = new ArrayList<>();
        player2Moves = new ArrayList<>();
        turn = player1Symbol;
    }

    private void onpress(View v){
        Button button = (Button) v;
        move(button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display.
        EdgeToEdge.enable(this);
        // Set the content view to the main layout XML file.
        setContentView(R.layout.second_activity);

        // Handle system bar insets to adjust padding for the main view.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        player1ScoursTextView = findViewById(R.id.player1ScourTextView);
        player2ScoursTextView = findViewById(R.id.player2ScourTextView);

        // Initialize the 'board' array by finding all 9 Button views from the layout.
        board = new Button[]{
                findViewById(R.id.button00), findViewById(R.id.button01), findViewById(R.id.button02),
                findViewById(R.id.button03), findViewById(R.id.button04), findViewById(R.id.button05),
                findViewById(R.id.button06), findViewById(R.id.button07), findViewById(R.id.button08)
        };

        for (Button button : board){
            button.setOnClickListener(v -> {onpress(v);});
        }

        findViewById(R.id.restarBtn2).setOnClickListener(v -> {
            restart();
        });

        findViewById(R.id.backBtn2).setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }
}
