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
    // TextViews to display the scores for Player 1 and Player 2.
    TextView player1ScoursTextView;
    TextView player2ScoursTextView;

    // An array of Button objects representing the 9 cells of the Tic-Tac-Toe board.
    private Button[] board;
    // A list of available (empty) cells on the board, identified by their index (0-8).
    private static ArrayList<Integer> availableCases =  new ArrayList<>(){{
        add(0);add(1);add(2);
        add(3);add(4);add(5);
        add(6);add(7);add(8);
    }};
    // A 2D array defining all possible winning combinations (rows, columns, and diagonals).
    final private static int[][] winCases = {
            // Rows
            {0,1,2},{3,4,5},{6,7,8},
            // Columns
            {0,3,6},{1,4,7},{2,5,8}
            // Diagonals
            ,{0,4,8},{2,4,6}
    };
    // Lists to store the moves made by Player 1 and Player 2, respectively.
    private static ArrayList<Integer> player1Moves = new ArrayList<>();
    private static ArrayList<Integer> player2Moves = new ArrayList<>();
    // Constants for player symbols.
    final static String player1Symbol = "X";
    final static String player2Symbol = "O";
    // A string to keep track of the current player's turn. It starts with Player 1.
    private static String turn = player1Symbol;
    // Static variables to hold the scores for Player 1 and Player 2 across rounds.
    private static int player1Scours = 0;
    private static int player2Scours = 0;

    /**
     * Checks if a single integer item is present in a given ArrayList of integers.
     * This is a helper method for checking player moves against winning combinations.
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

    /**
     * Gets the index (0-8) of a button on the board.
     * @param button The button whose index is to be found.
     * @return The index of the button, or -1 if not found.
     */
    private int getIndex(Button button){
        for (int i = 0; i < board.length; i++) {
            if (board[i] == button){
                return i;
            }
        }
        return -1;
    }
    /**
     * Checks for a win condition or a draw after each move.
     * @param playerMoves The list of moves made by the current player.
     * @param symbol The symbol ('X' or 'O') of the player whose win is being checked.
     */
    private void checkWiner(ArrayList<Integer> playerMoves, String symbol){
        int counter = 0;
        // Iterate through all possible winning cases.
        for (int[] cs : winCases) {
            counter = 0; // Reset counter for each winning case.
            for (int pos : cs) {
                // Check if the player's moves include a position from the current winning case.
                if (in(playerMoves, pos)){
                    counter++;
                }
            }
            // If the player holds all 3 positions, they win.
            if(counter==3){
                // Highlight the winning buttons in green.
                for (int index : cs){
                    board[index].setBackgroundColor(Color.parseColor("#00FF00"));
                }
                // Disable all buttons to stop the game.
                for (Button button : board){
                    button.setEnabled(false);
                }
                // Show a toast message announcing the winner.
                Toast.makeText(this,"the winer is: "+symbol,Toast.LENGTH_LONG).show();
                // Update the score for the winning player.
                if (symbol.equals(player1Symbol)){
                    player1Scours++;
                    runOnUiThread(() -> player1ScoursTextView.setText(
                            "player 1 (\"X\") Scour: "+player1Scours
                    ));
                }
                else { // Player 2 wins.
                    player2Scours++;
                    runOnUiThread(() -> player2ScoursTextView.setText(
                            "player 2 (\"O\") Scour: "+player2Scours
                    ));
                }
                return; // Exit after a win is found.
            }
        }
        // If no winner is found, check for a draw (i.e., all cells are filled).
        if (availableCases.size() == 0){
            // If it's a draw, highlight all buttons in blue.
            for (Button button : board){
                button.setBackgroundColor(Color.parseColor("#0000FF")); // Blue for Draw
            }
            // Show a toast message for the draw.
            Toast.makeText(this,"it's a draw!",Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * Handles the logic for a player's move.
     * @param button The button that was clicked.
     */
    public void move(Button button){
        // Determine which player's turn it is.
        if (turn.equals(player1Symbol)){
            // Player 1's move.
            player1Moves.add(getIndex(button));
            availableCases.remove(Integer.valueOf(getIndex(button)));
            button.setText(turn);
            button.setBackgroundColor(Color.parseColor("#4676FB"));
            // Check for a winner after the move.
            checkWiner(player1Moves,turn);
            turn = player2Symbol; // Switch turn to Player 2.
        }
        else {
            // Player 2's move.
            player2Moves.add(getIndex(button));
            availableCases.remove(Integer.valueOf(getIndex(button)));
            button.setText(turn);
            button.setBackgroundColor(Color.parseColor("#F26CE6"));
            // Check for a winner after the move.
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
            button.setBackgroundColor(Color.parseColor("#F885C0C8")); // Set to a default background color
            button.setText(""); // Clear the 'X' or 'O' text
            button.setEnabled(true); // Re-enable the button
        }

        // Reset the availableCases list to its initial state (all 9 cells available).
        availableCases =  new ArrayList<>(){{
            add(0);add(1);add(2);
            add(3);add(4);add(5);
            add(6);add(7);add(8);
        }};
        // Clear all move tracking lists.
        player1Moves = new ArrayList<>();
        player2Moves = new ArrayList<>();
        // Reset the turn to Player 1.
        turn = player1Symbol;
    }

    /**
     * Handles the click event for the game board buttons.
     * @param v The View that was clicked (a Button).
     */
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

        // Initialize TextViews for displaying scores.
        player1ScoursTextView = findViewById(R.id.player1ScourTextView);
        player2ScoursTextView = findViewById(R.id.player2ScourTextView);

        runOnUiThread(() -> player1ScoursTextView.setText(
                "player 1 (\"X\") Scour: "+player1Scours
        ));
        runOnUiThread(() -> player2ScoursTextView.setText(
                "player 2 (\"O\") Scour: "+player2Scours
        ));

        // Initialize the 'board' array by finding all 9 Button views from the layout.
        board = new Button[]{
                findViewById(R.id.button00), findViewById(R.id.button01), findViewById(R.id.button02),
                findViewById(R.id.button03), findViewById(R.id.button04), findViewById(R.id.button05),
                findViewById(R.id.button06), findViewById(R.id.button07), findViewById(R.id.button08)
        };

        // Set a click listener for each button on the board.
        for (Button button : board){
            button.setOnClickListener(v -> {onpress(v);});
            button.setBackgroundColor(Color.parseColor("#F885C0C8"));
        }

        // Set a click listener for the "Restart" button.
        findViewById(R.id.restarBtn2).setOnClickListener(v -> {
            restart();
        });

        // Set a click listener for the "Back" button.
        findViewById(R.id.backBtn2).setOnClickListener(v -> {
            // Restart the game state before going back to the main menu.
            restart();
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }
}
