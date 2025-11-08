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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

// Main activity class for the Tic-Tac-Toe game.
public class FirstActivity extends AppCompatActivity {
    TextView playerScoursTextView;
    TextView computerScoursTestView;
    // Array to hold the 9 Button objects that represent the board.
    private Button[] board;
    // A dynamic list of lists representing potential winning lines that are still available.
    // This is reset in the restart() method.
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
    final private static int[][] winCases = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
    };
    private static String playerSymbol = "X";
    private static String computerSymbol = "O";
    private static String turn = playerSymbol;
    private static int playerScours = 0;
    private static int computerScours = 0;


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

        // Check for a draw (all positions filled).
        if (in(totalMoves,allPositions)){
            // If it's a draw, set all buttons to blue and end the game.
            for (Button button : board){
                button.setBackgroundColor(Color.parseColor("#0000FF")); // Blue for Draw
            }
            gameOver = true;
            Toast.makeText(this,"It's a draw!!",Toast.LENGTH_LONG).show();
            return;
        }

        // Check if the player ('X') has won.
        if (symbol=='X'){
            // Iterate through all possible winning cases.
            for (int[] cs : winCases) {
                counter = 0;
                // Count how many spots in the current winning line are held by 'X'.
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
                // Count how many spots in the current winning line are held by 'O'.
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
                    Toast.makeText(this,"you lose!!",Toast.LENGTH_LONG).show();
                    computerScours++;
                    runOnUiThread(() -> computerScoursTestView.setText(
                            "computer Scour: "+computerScours
                    ));
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
        if (turn.equals(playerSymbol)){
            button.setBackgroundColor(Color.parseColor("#F26CE6"));
            turn = computerSymbol;
        }
        else {
            button.setBackgroundColor(Color.parseColor("#4676FB"));
            turn = playerSymbol;
        }
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
        String buttonText = button.getText().toString(); // Not used
        int choice = getIndex(button);

        // Check if the spot is already taken (redundant if UI is correct, but safe) or if the game is over.
        if(in(computeMoves,choice) || buttonText.length() > 0 || gameOver){return;}

        playerMoves.add(choice);
        totalMoves.add(choice);

        // Remove the chosen position from the available winning paths (part of AI's logic).
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
        if (gameOver){return;}
        int maxLigth = 3;
        Integer computeChoice = null;

        // AI Logic Part 1: Find a move in the shortest available winning path (prioritizes offense/blocking).
        for (ArrayList<Integer> cs : availableCases) {
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

        // Execute the chosen move.
        // Needs a null check for computeChoice in case no valid move was found (shouldn't happen in a non-full board)
        if (computeChoice != null) {
            computeMoves.add(computeChoice);
            totalMoves.add(computeChoice);
            move(board[computeChoice], "O");
            checkWiner('O'); // Check if the computer won.
        }
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
        availableCases = new ArrayList<>()
        {{
            add(new ArrayList<>(Arrays.asList(0, 1, 2)));
            add(new ArrayList<>(Arrays.asList(3, 4, 5)));
            add(new ArrayList<>(Arrays.asList(6, 7, 8)));
            add(new ArrayList<>(Arrays.asList(0, 3, 6)));
            add(new ArrayList<>(Arrays.asList(1, 4, 7)));
            add(new ArrayList<>(Arrays.asList(2, 5, 8)));
            add(new ArrayList<>(Arrays.asList(0, 4, 8)));
            add(new ArrayList<>(Arrays.asList(2, 4, 6)));
        }};

        // Clear all move tracking lists.
        computeMoves = new ArrayList<>();
        playerMoves = new ArrayList<>();
        totalMoves = new ArrayList<>();

        // Reset the game over flag.
        gameOver = false;
    }

    /**
     * A common click handler method for all board buttons, typically referenced
     * from the XML layout's android:onClick attribute (if the individual listeners were removed).
     * @param v The View (Button) that was clicked.
     */
    public void onclick(View v){
        // Cast the generic View to a Button.
        Button button = (Button) v;
        // Pass the clicked button to the player move logic.
        playerMove(button);
    }

    // --- Android Lifecycle Methods ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display.
        EdgeToEdge.enable(this);
        // Set the content view to the main layout XML file.
        setContentView(R.layout.first_activity);

        // Handle system bar insets to adjust padding for the main view.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.first), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playerScoursTextView = findViewById(R.id.playerScourTextView);
        computerScoursTestView = findViewById(R.id.computerScourTextView);

        // Initialize the 'board' array by finding all 9 Button views from the layout.
        board = new Button[]{
                findViewById(R.id.button0), findViewById(R.id.button1), findViewById(R.id.button2),
                findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5),
                findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8)
        };

        // Set up OnClickListener for the restart button.
        findViewById(R.id.restarBtn).setOnClickListener(v-> {restart();});

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });

        // Note: The previous individual click listeners for buttons (button0 to button8)
        // have been replaced, likely with the single 'onclick' method being referenced
        // in the layout XML of the 9 board buttons.

        // code version addresses the incomplete nature of the first by adding a functional game reset and
        // optimizing the event handling for the 9 board buttons.
    }
}
