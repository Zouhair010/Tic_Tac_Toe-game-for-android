package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

// Main activity class for the Tic-Tac-Toe game.
public class MainActivity extends AppCompatActivity {
    // Array to hold the 9 Button objects that represent the board.
    private Button[] board;
    // A dynamic list of lists representing potential winning lines that are still available.
    // Each inner list holds the board indices (0-8) of a current potential winning path.
    private static ArrayList<ArrayList<Integer>> availableCases = new ArrayList<>()
    {{
        add(new ArrayList<>(Arrays.asList(0, 1, 2))); // Top row
        add(new ArrayList<>(Arrays.asList(3, 4, 5))); // Middle row
        add(new ArrayList<>(Arrays.asList(6, 7, 8))); // Bottom row
        add(new ArrayList<>(Arrays.asList(0, 3, 6))); // Left column
        add(new ArrayList<>(Arrays.asList(1, 4, 7))); // Middle column
        add(new ArrayList<>(Arrays.asList(2, 5, 8))); // Right column
        add(new ArrayList<>(Arrays.asList(0, 4, 8))); // Diagonal (top-left to bottom-right)
        add(new ArrayList<>(Arrays.asList(2, 4, 6))); // Diagonal (top-right to bottom-left)
    }};
    // A list of all possible positions on the board (0-8). Used primarily for draw check.
    final static ArrayList<Integer> allPositions = new ArrayList<>(){{
        add(0);add(1);add(2);
        add(3);add(4);add(5);
        add(6);add(7);add(8);
    }};
    // Stores the board indices (positions) taken by the computer ('O').
    private static ArrayList<Integer> computeMoves = new ArrayList<>();
    // Stores the board indices (positions) taken by the player ('X').
    private static ArrayList<Integer> playerMoves = new ArrayList<>();
    // Stores all moves made by both player and computer.
    private static ArrayList<Integer> totalMoves = new ArrayList<>();
    // Flag to indicate if the game has ended (win or draw).
    private static boolean gameOver = false;
    // A static array of arrays defining all possible winning combinations by board index.
    final private static int[][] winCases = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};


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

    /**
     * Checks if all elements of arrList2 are present in arrList1.
     * Used mainly to check for a draw (if allPositions are in totalMoves).
     * @param arrList1 The list to check for the presence of elements.
     * @param arrList2 The list containing the elements to check.
     * @return true if all elements in arrList2 are present in arrList1, false otherwise.
     */
    private boolean in(ArrayList<Integer> arrList1, ArrayList<Integer> arrList2){
        int counter = 0;
        for (int i : arrList2) {
            if (in(arrList1,i)){
                counter++;
            }
        }
        // If the count of matching elements equals the size of arrList2, all elements are present.
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
        int counter = 0;

        // Check for a draw by seeing if all positions have been filled.
        if (in(totalMoves,allPositions)){
            // If it's a draw, set all buttons to blue and end the game.
            for (Button button : board){
                button.setBackgroundColor(Color.parseColor("#0000FF")); // Blue for Draw
            }
            gameOver = true;
            return; // Exit as draw takes precedence over a win check for the current symbol.
        }

        // Check if the player ('X') has won.
        if (symbol=='X'){
            // Iterate through all possible winning cases.
            for (int[] cs : winCases) {
                counter = 0;
                // Check how many positions in the current winning case (cs) are held by the player.
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
                    gameOver = true;
                    return; // Exit after a win is found.
                }
            }
        }
        // Check if the computer ('O') has won.
        else{
            // Iterate through all possible winning cases.
            for (int[] cs : winCases) {
                counter = 0;
                // Check how many positions in the current winning case (cs) are held by the computer.
                for (int pos : cs) {
                    if (in(computeMoves, pos)){
                        counter++;
                    }
                }
                // If the computer holds all 3 positions, it wins.
                if(counter==3){
                    // Highlight the winning buttons in red.
                    board[cs[0]].setBackgroundColor(Color.parseColor("#FF0000"));
                    board[cs[1]].setBackgroundColor(Color.parseColor("#FF0000"));
                    board[cs[2]].setBackgroundColor(Color.parseColor("#FF0000"));
                    // Disable all buttons to stop the game.
                    for (Button button : board){
                        button.setEnabled(false);
                    }
                    gameOver = true;
                    return; // Exit after a win is found.
                }
            }
        }
    }

    /**
     * Updates the UI for a move: sets the button text and disables it.
     * @param button The Button object that was chosen.
     * @param symbol The symbol ('X' or 'O') to display.
     */
    private void move(Button button, String symbol){
        button.setText(symbol);
        button.setEnabled(false);
    }

    /**
     * Gets the index (0-8) of a given Button within the 'board' array.
     * @param button The Button to find the index for.
     * @return The integer index (0-8) or -1 if not found.
     */
    private int getIndex(Button button){
        for (int i=0 ; i<board.length ; i++) {
            if (board[i] == button){
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the player's turn.
     * @param button The Button clicked by the player.
     */
    private void playerMove(Button button){
        // String buttonText = button.getText().toString(); // Not used
        int choice = getIndex(button);
        playerMoves.add(choice);
        totalMoves.add(choice);

        // A simple check to see if the spot is already taken by the computer.
        // This check is redundant if the button is correctly disabled/unavailable in the UI,
        // but it provides an extra layer of logic check.
        if(in(computeMoves,choice)){return;}

        // Remove the chosen position from the available winning paths.
        // This is part of the custom AI logic's scoring/availability system.
        for (ArrayList<Integer> cs : availableCases) {
            if(in(cs,choice)){
                cs.remove(Integer.valueOf(choice));
            }
        }
        move(button, "X");
        checkWiner('X'); // Check if the player won.
        computerMove(); // Start the computer's turn.
    }

    /**
     * Handles the computer's turn with basic AI logic.
     */
    private void computerMove(){
        if (gameOver){return;} // Don't move if the game is over.

        int maxLigth = 3; // Max number of spots left in a potential win line
        Integer computeChoice = null; // The final chosen position.

        // AI Logic Part 1: Find a move in the shortest available winning path (prioritizing offense).
        for (ArrayList<Integer> cs : availableCases) {
            // Check paths with 1, 2, or 3 spots remaining (3 is initial, 1 is a winning move).
            if(0 < cs.size() && cs.size() <= maxLigth){
                for (int i : cs){
                    // Check that the spot is not already taken by the computer ('O').
                    if ( !in(computeMoves, i) ){
                        maxLigth=cs.size(); // Update maxLigth to prioritize shorter paths
                        computeChoice=i;
                    }
                }
            }
        }

        // AI Logic Part 2: Check if the computer can win in this turn (highest priority offensive check).
        for (int[] cs : winCases) {
            int counter=0;
            for (int i : cs) {
                if (in(computeMoves,i)){
                    counter++; // Count 'O' marks in this winning line.
                }
            }
            // If two spots in a winning line are taken by the computer, take the third one if it's free.
            if (counter==2){
                for (int it : cs) {
                    // Check if the third spot is not taken by 'O' or 'X'.
                    if(!in(computeMoves,it) && !in(playerMoves,it)){
                        computeChoice=it; // This move guarantees a win.
                        break; // Found a winning move, break inner loop.
                    }
                }
            }
        }

        // AI Logic Part 3: Prioritize taking the center square if available (standard Tic-Tac-Toe strategy).
        if (!in(playerMoves,4) && !in(computeMoves,4)){
            computeChoice = 4; // Center square index.
        }

        // Defensive Logic is implicitly covered by Part 1 and 2, but a dedicated check to block player wins is missing.

        // Execute the chosen move.
        computeMoves.add(computeChoice);
        totalMoves.add(computeChoice);
        move(board[computeChoice], "O");
        checkWiner('O'); // Check if the computer won.
    }

    // --- Android Lifecycle Methods ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display (system UI takes up the full screen).
        EdgeToEdge.enable(this);
        // Set the content view to the main layout XML file.
        setContentView(R.layout.activity_main);

        // Handle system bar insets to adjust padding for the main view.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the 'board' array by finding all Button views from the layout.
        board = new Button[]{
                findViewById(R.id.button0), findViewById(R.id.button1), findViewById(R.id.button2),
                findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5),
                findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8)
        };

        // Set up OnClickListeners for all 9 board buttons to handle player moves.
        findViewById(R.id.button0).setOnClickListener(v -> {playerMove(findViewById(R.id.button0));});
        findViewById(R.id.button1).setOnClickListener(v -> {playerMove(findViewById(R.id.button1));});
        findViewById(R.id.button2).setOnClickListener(v -> {playerMove(findViewById(R.id.button2));});
        findViewById(R.id.button3).setOnClickListener(v -> {playerMove(findViewById(R.id.button3));});
        findViewById(R.id.button4).setOnClickListener(v -> {playerMove(findViewById(R.id.button4));});
        findViewById(R.id.button5).setOnClickListener(v -> {playerMove(findViewById(R.id.button5));});
        findViewById(R.id.button6).setOnClickListener(v -> {playerMove(findViewById(R.id.button6));});
        findViewById(R.id.button7).setOnClickListener(v -> {playerMove(findViewById(R.id.button7));});
        findViewById(R.id.button8).setOnClickListener(v -> {playerMove(findViewById(R.id.button8));});

    }
}