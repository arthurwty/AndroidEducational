package com.example.a2048;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GameBoard {

    private static final String TAG = "GameBoard";

    int score;          // keep track of the current score in the game board
    int best_score;

    Map<String, Boolean> wasCombined;    // each tile cannot be combined twice in each round
    Map<String, TextView> scoreMap;     // contains score and best_score TextViews
    Map<String, TextView> myMap;        // contains the TextViews of the 16 tiles
    boolean tileMoved;      // keep track of whether to add tile or not

    // stack to store previous stages
    Stack<Map<String,TextView>> mystack = new Stack<>();
    // stack to store previous scores
    Stack<Integer> scoreStack = new Stack<>();

    // counter to count how many steps the player played
    int count;

    // Context
    Context context;

    /**
     * Constructor of the GameBoard class
     * @param myMap - a map of 16 pairs of <String, TextView>, 1 for each tile
     * @param scoreMap -  a map of 2 pairs of <String, TextView>, score & best_score
     * @param context
     */
    public GameBoard(Map<String, TextView> myMap, Map<String, TextView> scoreMap, Context context) {
        this.myMap = myMap;
        this.context = context;
        tileMoved = false;
        wasCombined = new HashMap<>();
        for (int i = 1; i < 17; i++){
            wasCombined.put("view" + i, false);
        }
        this.scoreMap = scoreMap;
    }

    /**
     * Start the game
     * @param score - start the game with this score
     */
    public void startGame(String score) {
        // if the score is not empty, continue last game
        if (!score.equals("")) {
            this.score = Integer.parseInt(score);
        }
        // else, start a new game
        else {
            this.score = 0;
            // assign all tiles to be 0 at the beginning
            String curr_tile;
            for (int i = 1; i < 17; i++){
                curr_tile = "view" + i;
                myMap.get(curr_tile).setText("0");
            }

            // randomly assign two tiles to start the game
            int randomNum1 = (int)(Math.random() * 16) + 1;
            int randomNum2 = (int)(Math.random() * 16) + 1;
            while (randomNum1 == randomNum2){
                randomNum2 = (int)(Math.random() * 16) + 1;
            }

            String firstTile = "view" + randomNum1;
            String secondTile = "view" + randomNum2;
            myMap.get(firstTile).setText("2");
            myMap.get(secondTile).setText("2");
        }
        // render the score TextView
        scoreMap.get("score").setText(String.valueOf(this.score));

        if (!scoreMap.get("best_score").getText().toString().equals("")) {
            best_score = Integer.parseInt(scoreMap.get("best_score").getText().toString());
        } else {
            best_score = 0;
            scoreMap.get("best_score").setText(String.valueOf(best_score));
        }


        // clear the scoreStack and push the starting score
        scoreStack.clear();
        scoreStack.push(this.score);

        // save the starting map
        saveMap(myMap);
        count = 0;
    }

    /**
     * A helper method:
     * Add a new tile with text "2" into the game board after shifting
     */
    public void addTile() {
        ArrayList<String> remainTile = new ArrayList<>();
        String curr_value;

        // gather the remaining tiles
        for (Map.Entry<String, TextView> pair: myMap.entrySet()) {
            curr_value = pair.getValue().getText().toString();
            if (curr_value.equals("0")){
                remainTile.add(pair.getKey());
            }
        }

        // of no remaining tiles, GAME OVER!!!!!
        // TODO: handle game over
        if (remainTile.size() == 0) {
            return;
        }

        int randomIndex = (int) (Math.random() * remainTile.size());
        String randomTile = remainTile.get(randomIndex);
        myMap.get(randomTile).setText("2");

        tileMoved = false;
    }

    /**
     * A helper method:
     * Determine whether to shift a tile
     */
    public void shift(String curr_tile, String next_tile) {
        String curr_value = myMap.get(curr_tile).getText().toString();
        String next_value = myMap.get(next_tile).getText().toString();

        // simply shift
        if (!curr_value.equals("0") && next_value.equals("0")) {
            myMap.get(next_tile).setText(curr_value);
            myMap.get(curr_tile).setText("0");
            tileMoved = true;
        }

        // combine if values are the same
        // don't combine if the tile was combined before
        else if (!next_value.equals("0") && next_value.equals(curr_value)) {
            if (!wasCombined.get(curr_tile) && !wasCombined.get(next_tile) ) {
                int new_value = Integer.parseInt(curr_value) + Integer.parseInt(next_value);

                // compute score
                score += new_value;
                scoreMap.get("score").setText(String.valueOf(score));

                // update best score
                if (score > best_score) {
                    best_score = score;
                    scoreMap.get("best_score").setText(String.valueOf(score));
                }

                myMap.get(next_tile).setText(String.valueOf(new_value));
                myMap.get(curr_tile).setText("0");
                tileMoved = true;
                wasCombined.put(next_tile, true);
            }
        }
    }


    /**
     * Handle the swipe up case
     */
    public void handleUp() {
        String curr_tile;
        String up_tile;
        for (Map.Entry<String, Boolean> pair: wasCombined.entrySet()) {
            pair.setValue(false);
        }

        score = scoreStack.peek();
        // three rounds
        for (int j = 17; j > 8; j -= 4) {
            for (int i = 5; i < j; i++){
                curr_tile = "view" + i;
                up_tile = "view" + (i-4);
                shift(curr_tile, up_tile);
            }
        }
        // if game is not over, add one more tile; if nothing is moved, don't add
        // Will determine game over inside addTile()
        if (tileMoved) addTile();
        saveMap(myMap);
        scoreStack.push(score);
        count++;
    }

    /**
     * Handle the swipe down case
     */
    public void handleDown() {
        String curr_tile;
        String down_tile;
        for (Map.Entry<String, Boolean> pair: wasCombined.entrySet()) {
            pair.setValue(false);
        }

        score = scoreStack.peek();
        // three rounds
        for (int j = 0; j < 11; j += 4) {
            for (int i = 12; i > j; i--){
                curr_tile = "view" + i;
                down_tile = "view" + (i+4);
                shift(curr_tile, down_tile);
            }
        }
        // if game is not over, add one more tile; if nothing is moved, don't add
        // Will determine game over inside addTile()
        if (tileMoved) addTile();
        saveMap(myMap);
        scoreStack.push(score);
        count++;
    }

    /**
     * Handle the swipe right case
     */
    public void handleRight() {
        String curr_tile;
        String right_tile;
        for (Map.Entry<String, Boolean> pair: wasCombined.entrySet()) {
            pair.setValue(false);
        }

        score = scoreStack.peek();
        // three rounds
        for (int j = 1; j < 4; j++) {
            for (int i = 15; ; ){
                curr_tile = "view" + i;
                right_tile = "view" + (i+1);
                shift(curr_tile, right_tile);

                // determine the next view number
                if (i < 4) {
                    if (i == j) break;
                    i += 11;
                } else i -= 4;
            }
        }
        // if game is not over, add one more tile; if nothing is moved, don't add
        // Will determine game over inside addTile()
        if (tileMoved) addTile();
        saveMap(myMap);
        scoreStack.push(score);
        count++;
    }

    /**
     * Handle the swipe left case
     */
    public void handleLeft() {
        String curr_tile;
        String left_tile;
        for (Map.Entry<String, Boolean> pair: wasCombined.entrySet()) {
            pair.setValue(false);
        }

        score = scoreStack.peek();
        // three rounds
        for (int j = 16; j > 13; j--) {
            for (int i = 2; ; ){
                curr_tile = "view" + i;
                left_tile = "view" + (i-1);
                shift(curr_tile, left_tile);

                // determine the next view number
                if (i > 13) {
                    if (i == j) break;
                    i -= 11;
                } else i += 4;
            }
        }
        // if game is not over, add one more tile; if nothing is moved, don't add
        // Will determine game over inside addTile()
        if (tileMoved) addTile();
        saveMap(myMap);
        scoreStack.push(score);
        count++;
    }

    /**
     * Helper method to copy map and push to the stack
     */
    public void saveMap(Map<String, TextView> map){
        Map<String, TextView> newMap = new HashMap<>();
        for (Map.Entry<String, TextView> entry : map.entrySet()) {
            String k = entry.getKey();
            TextView v = entry.getValue();
            TextView newText = new TextView(context);
            newMap.put(k,newText);
            newMap.get(k).setText(v.getText());
        }
        mystack.push(newMap);
    }
    /**
     * Undo method
     */
    public void undo(){
        if (count > 0) {
            mystack.pop();
            for (Map.Entry<String, TextView> entry : mystack.peek().entrySet()) {
                String k = entry.getKey();
                TextView v = entry.getValue();
                this.myMap.get(k).setText(v.getText());
            }
            count--;
            // reverse score
            scoreStack.pop();
            scoreMap.get("score").setText(String.valueOf(scoreStack.peek()));
        }
        // else TBD

    }



}
