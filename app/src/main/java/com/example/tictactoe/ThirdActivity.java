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
import java.util.Random;

/**
 * This activity manages the Tic-Tac-Toe game logic for a player against a computer opponent.
 */
public class ThirdActivity extends AppCompatActivity {
    // TextViews to display the scores for the player and the computer.
    TextView playerScoursTextView;
    TextView computerScoursTestView;
    // Array to hold the 9 Button objects that represent the board.
    private Button[] board;
    // A dynamic list of lists representing potential winning lines that are still available.
    // This is used by the computer, is reset in the restart() method.
    private static ArrayList<Integer> availableCases = new ArrayList<>(){{
        add(0);add(1);add(2);
        add(3);add(4);add(5);
        add(6);add(7);add(8);
    }};
    // Stores the board indices (positions) occupied by the computer ('O').
    private static ArrayList<Integer> computeMoves = new ArrayList<>();
    // Stores the board indices (positions) occupied by the player ('X').
    private static ArrayList<Integer> playerMoves = new ArrayList<>();
    // Flag to indicate if the game has ended (win or draw).
    private static boolean gameOver = false;
    // A static array of arrays defining all possible winning combinations by board index.
    final private static int[][] winCases = {
            // Winning combinations for rows.
            {0,1,2},{3,4,5},{6,7,8},
            // Winning combinations for columns.
            {0,3,6},{1,4,7},{2,5,8},
            // Winning combinations for diagonals.
            {0,4,8},{2,4,6}
    };
    // Constants for player and computer symbols.
    private static String playerSymbol = "X";
    private static String computerSymbol = "O";
    // A string to keep track of the current player's turn. It starts with the player.
    private static String turn = playerSymbol;
    // Static variables to hold the scores for the player and computer across rounds.
    private static int playerScours = 0;
    private static int computerScours = 0;


    /**
     * Checks if a single integer item is present in a given ArrayList of integers.
     * @param arr The ArrayList to search within.
     * @param item The integer to search for.
     * @return true if the item is found, false otherwise.
     */
    private boolean in(ArrayList<Integer> arr, int item){
        // Iterate through each element in the ArrayList.
        for (int i : arr) {
            // If the current element matches the item, return true.
            if (i == item){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all elements of arrList2 are present in arrList1.
     * Used to check for a draw by seeing if all board positions are in totalMoves.
     * @param arrList1 The list to check for the presence of elements.
     * @param arrList2 The list containing the elements to check.
     * @return true if all elements in arrList2 are present in arrList1, false otherwise.
     */
    private boolean in(ArrayList<Integer> arrList1, ArrayList<Integer> arrList2){
        // Counter to track the number of elements from arrList2 found in arrList1.
        int counter = 0;
        // Iterate through each element in the second list.
        for (int i : arrList2) {
            // Use the single-item 'in' method to check for presence.
            if (in(arrList1,i)){
                counter++;
            }
        }
        // If the counter matches the size of the second list, it means all its elements were found.
        if(counter == arrList2.size()){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks for a win condition or a draw.
     * @param symbol The symbol ('X' or 'O') of the player whose win is being checked.
     */
    private void checkWiner(char symbol){
        // Counter to track how many positions in a winning line are held by a player.
        int counter = 0;
        // Check if the player ('X') has won.
        if (symbol=='X'){
            // Iterate through all possible winning cases.
            for (int[] cs : winCases) {
                counter = 0;
                // Count how many positions in the current winning line are held by the player.
                for (int pos : cs) { // 'pos' is an index on the board, e.g., 0, 1, 2...
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
                    // Set the game over flag, show a toast, and update the player's score.
                    gameOver = true;
                    Toast.makeText(this,"Good job!! you win",Toast.LENGTH_LONG).show();
                    playerScours++;
                    runOnUiThread(() -> playerScoursTextView.setText(
                            "your Scour: "+playerScours
                    ));
                    return; // Exit after a win is found.
                }
            }
        }
        // Check if the computer ('O') has won.
        else{
            // Iterate through all possible winning cases.
            for (int[] cs : winCases) {
                counter = 0;
                // Count how many positions in the current winning line are held by the computer.
                for (int pos : cs) { // 'pos' is an index on the board.
                    if (in(computeMoves, pos)){
                        counter++;
                    }
                }
                // If the computer holds all 3 positions, it wins.
                if(counter==3){
                    // Highlight the winning buttons in red.
                    for (int index : cs){
                        board[index].setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    // Disable all buttons to stop the game.
                    for (Button button : board){
                        button.setEnabled(false);
                    }
                    // Set the game over flag, show a toast, and update the computer's score.
                    gameOver = true;
                    Toast.makeText(this,"you lose!!",Toast.LENGTH_LONG).show();
                    computerScours++;
                    runOnUiThread(() -> computerScoursTestView.setText(
                            "computer Scour: "+computerScours
                    ));
                    return; // Exit after a win is found.
                }
            }
        }
        // First, check for a draw condition (all positions are filled).
        if (availableCases.size()==0){
            // If it's a draw, set all buttons to blue and end the game.
            for (Button button : board){
                button.setBackgroundColor(Color.parseColor("#0000FF")); // Blue for Draw
            }
            // Set the game over flag and show a toast message.
            gameOver = true;
            Toast.makeText(this,"It's a draw!!",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Updates the UI for a move: sets the button text and disables it.
     * Also changes the button color based on whose turn it is.
     * @param button The Button object that was chosen.
     * @param symbol The symbol ('X' or 'O') to display.
     */
    private void move(Button button, String symbol){
        // Change button color and switch the turn.
        // If it was the player's turn, change color for player move and set turn to computer.
        if (turn.equals(playerSymbol)){
            button.setBackgroundColor(Color.parseColor("#F26CE6"));
            turn = computerSymbol;
        }
        else {
            // If it was the computer's turn, change color for computer move and set turn to player.
            button.setBackgroundColor(Color.parseColor("#4676FB"));
            turn = playerSymbol;
        }
        // Set the symbol on the button and disable it.
        button.setText(symbol);
        button.setEnabled(false);
    }

    /**
     * Gets the index (0-8) of a given Button within the 'board' array.
     * @param button The Button to find the index for.
     * @return The integer index (0-8) or -1 if not found.
     */
    private int getIndex(Button button){
        // Loop through the board array to find the matching button.
        for (int i=0 ; i<board.length ; i++) {
            if (board[i] == button){
                return i;
            }
        }
        // Return -1 if the button is not found (should not happen in normal gameplay).
        return -1;
    }

    /**
     * Handles the player's turn.
     * @param button The Button clicked by the player.
     */
    private void playerMove(Button button){
        // Get the index of the clicked button.
        int choice = getIndex(button);
        // Add the move to the player's list of moves.
        playerMoves.add(choice);
        // Remove the chosen position from the list of available cases.
        availableCases.remove(Integer.valueOf(choice));
        // Update the button's appearance and state.
        move(button, "X");
        // Check if this move resulted in a win for the player.
        checkWiner('X'); // Check if the player won.
    }

    /**
     * Handles the computer's turn by making a random move from the available spots.
     */
    private void computerMove(){
        // Only proceed if the game is not over.
        if (!gameOver) {
            Random random = new Random();
            // Pick a random index from the list of available board positions.
            int computeChoice = random.nextInt(availableCases.size());
            // Add the chosen board position to the computer's moves.
            computeMoves.add(availableCases.get(computeChoice));
            // Update the UI for the chosen button.
            move(board[availableCases.get(computeChoice)], "O");
            // Remove the position from available cases.
            availableCases.remove(computeChoice);
            // Check if the computer won with this move.
            checkWiner('O'); // Check if the computer won.
        }
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

        // Reset the availableCases list to its initial state (all 9 board positions).
        availableCases = new ArrayList<>(){{
            add(0);add(1);add(2);
            add(3);add(4);add(5);
            add(6);add(7);add(8);
        }};
        // Clear all move tracking lists.
        computeMoves = new ArrayList<>();
        playerMoves = new ArrayList<>();
        // Reset the game over flag.
        gameOver = false;
    }

    /**
     * A common click handler method for all board buttons, referenced from the XML layout.
     * @param v The View (Button) that was clicked.
     */
    public void onclick(View v){
        // Cast the generic View to a Button.
        Button button = (Button) v;
        // Pass the clicked button to the player move logic.
        playerMove(button);
        // After the player's move, the computer makes its move.
        computerMove(); // Immediately trigger the computer's turn.
    }

    // --- Android Lifecycle Methods ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display.
        EdgeToEdge.enable(this);
        // Set the content view to the main layout XML file.
        setContentView(R.layout.third_activity);

        // Handle system bar insets to adjust padding for the main view.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.third), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize TextViews for displaying scores.
        playerScoursTextView = findViewById(R.id.playerScourTextView2);
        computerScoursTestView = findViewById(R.id.computerScourTextView2);

        runOnUiThread(() -> playerScoursTextView.setText(
                "your Scour: "+playerScours
        ));
        runOnUiThread(() -> computerScoursTestView.setText(
                "computer Scour: "+computerScours
        ));

        // Initialize the 'board' array by finding all 9 Button views from the layout.
        board = new Button[]{
                findViewById(R.id.button000), findViewById(R.id.button001), findViewById(R.id.button002),
                findViewById(R.id.button003), findViewById(R.id.button004), findViewById(R.id.button005),
                findViewById(R.id.button006), findViewById(R.id.button007), findViewById(R.id.button008)
        };

        for (Button button : board){
            button.setBackgroundColor(Color.parseColor("#F885C0C8"));
        }

        // Set up OnClickListener for the restart button.
        findViewById(R.id.restarBtn3).setOnClickListener(v-> {restart();});

        // Set up OnClickListener for the back button.
        findViewById(R.id.backBtn3).setOnClickListener(v -> { // Lambda for click listener.
            // Restart the game state before navigating back to the main menu.
            restart();
            Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });

        // Note: The 'onclick' method is linked to each board button via the 'android:onClick' attribute in the layout XML file.
    }
}
