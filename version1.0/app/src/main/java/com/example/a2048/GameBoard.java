package com.example.a2048;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameBoard {

    private static final String TAG = "GameBoard";

    int score;          // keep track of the current score in the game board
    int best_score;         // keep track of the best score in the game board

    Map<String, Boolean> wasCombined;    // each tile cannot be combined twice in each round
    Map<String, TextView> scoreMap;     // contains score and best_score TextViews
    Map<String, TextView> myMap;        // contains the TextViews of the 16 tiles
    boolean tileMoved;      // keep track of whether to add tile or not

    Deque<Map<String,TextView>> myDeque;        // deque to store previous maps
    Deque<Integer> scoreDeque;          // deque to store previous scores
    Deque<Integer> bestScoreDeque;      // deque to store previous best scores

    int count;      // counter to count how many steps the player played / how many elements in the deque
    Context context;

    /**
     * Constructor of the GameBoard class
     * @param myMap - a map of 16 pairs of <String, TextView>, 1 for each tile
     * @param scoreMap -  a map of 2 pairs of <String, TextView>, score & best_score
     * @param context - current context (should be Game Activity)
     * @param myDeque - explained above
     * @param scoreDeque - explained above
     * @param bestScoreDeque - explained above
     */
    public GameBoard(Map<String, TextView> myMap, Map<String, TextView> scoreMap, Context context,
                     Deque<Map<String,TextView>> myDeque, Deque<Integer> scoreDeque, Deque<Integer> bestScoreDeque) {
        this.myMap = myMap;
        this.context = context;
        tileMoved = false;
        wasCombined = new HashMap<>();
        for (int i = 1; i < 17; i++){
            wasCombined.put("view" + i, false);
        }
        this.scoreMap = scoreMap;
        this.myDeque = myDeque;
        this.scoreDeque = scoreDeque;
        this.bestScoreDeque = bestScoreDeque;
    }

    /**
     * Start/continue the game
     * @param score - start/continue the game with this score
     */
    public void startGame(String score) {
        // if the score is not empty, continue last game
        if (!score.equals("")) {
            // the 16 TextViews, score, and best score TextViews should already be rendered
            // set the score and best score in the game board class
            this.score = Integer.parseInt(score);
            best_score = Integer.parseInt(scoreMap.get("best_score").getText().toString());
            count = scoreDeque.size();
        }
        // else, start a new game
        else {
            this.score = 0;
            // assign all tiles to be 0 at the beginning
            String curr_tile;
            for (int i = 1; i < 17; i++){
                curr_tile = "view" + i;
                myMap.get(curr_tile).setText("");
            }

            // randomly assign two tiles to start the game
            int randomNum1 = (int)(Math.random() * 16) + 1;
            int randomNum2 = (int)(Math.random() * 16) + 1;
            while (randomNum1 == randomNum2){
                randomNum2 = (int)(Math.random() * 16) + 1;
            }

            myMap.get("view" + randomNum1).setText("2");
            myMap.get("view" + randomNum2).setText("2");

            // clear all the Deque's
            scoreDeque.clear();
            myDeque.clear();
            bestScoreDeque.clear();

            // render the score TextView
            scoreMap.get("score").setText(String.valueOf(this.score));

            if (!scoreMap.get("best_score").getText().toString().equals("")) {
                best_score = Integer.parseInt(scoreMap.get("best_score").getText().toString());
            } else {
                best_score = 0;
                scoreMap.get("best_score").setText(String.valueOf(best_score));
            }

            // save the starting map, starting score, best score
            saveMap(myMap);
            saveScore();
            count = 1;
        }
        checkNum();
    }

    /**
     * set the color of different number
     */
    public void checkNum() {
        String curr_tile;
        for (int i = 1; i < 17; i++){
            curr_tile = "view" + i;
            if (myMap.get(curr_tile).getText().toString().equals("")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#16D3D3"));
            } else if (myMap.get(curr_tile).getText().toString().equals("2")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#00C4B4"));
            } else if (myMap.get(curr_tile).getText().toString().equals("4")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#00B3A6"));
            } else if (myMap.get(curr_tile).getText().toString().equals("8")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#01A299"));
            } else if (myMap.get(curr_tile).getText().toString().equals("16")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#019592"));
            } else if (myMap.get(curr_tile).getText().toString().equals("32")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#80FC3509"));
            } else if (myMap.get(curr_tile).getText().toString().equals("64")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#99FF2E00"));
            } else if (myMap.get(curr_tile).getText().toString().equals("128")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#BFFF2E00"));
            } else if (myMap.get(curr_tile).getText().toString().equals("256")) {
                myMap.get(curr_tile).setBackgroundColor( Color.parseColor("#FF2E00"));
            }
        }
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
            if (curr_value.equals("")){
                remainTile.add(pair.getKey());
            }
        }

        // if no remaining tiles, GAME OVER!!!!!
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
        if (!curr_value.equals("") && next_value.equals("")) {
            myMap.get(next_tile).setText(curr_value);
            myMap.get(curr_tile).setText("");
            tileMoved = true;
        }

        // combine if values are the same
        // don't combine if the tile was combined before
        else if (!next_value.equals("") && next_value.equals(curr_value)) {
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
                myMap.get(curr_tile).setText("");
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

        // three rounds
        for (int j = 17; j > 8; j -= 4) {
            for (int i = 5; i < j; i++){
                curr_tile = "view" + i;
                up_tile = "view" + (i-4);
                shift(curr_tile, up_tile);
            }
        }
        afterSwipe();
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

        // three rounds
        for (int j = 0; j < 11; j += 4) {
            for (int i = 12; i > j; i--){
                curr_tile = "view" + i;
                down_tile = "view" + (i+4);
                shift(curr_tile, down_tile);
            }
        }
        afterSwipe();
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
        afterSwipe();
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
        afterSwipe();
    }

    /**
     * Helper method that will run every time after the user swipes
     */
    public void afterSwipe() {
        // if game is not over, add one more tile; if nothing is moved, don't add
        // Will determine game over inside addTile()
        if (tileMoved) addTile();
        saveMap(myMap);
        count++;
        saveScore();
        checkNum();
    }

    /**
     * Helper method that will save the current map to the map Deque
     * Only save the 10 latest changes
     * @param map - the input current map
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
        if (myDeque.size() == 10) {
            myDeque.removeFirst();
        }
        myDeque.addLast(newMap);
    }

    /**
     * Helper method to save the 10 latest scores
     */
    public void saveScore() {
        if (scoreDeque.size() == 10) {
            scoreDeque.removeFirst();
            bestScoreDeque.removeFirst();
            count = 10;
        }
        scoreDeque.addLast(score);
        bestScoreDeque.addLast(best_score);
    }

    /**
     * Undo method
     */
    public void undo(){
        if (count > 1) {
            Log.i(TAG, "count: " + count);
            myDeque.removeLast();
            for (Map.Entry<String, TextView> entry : myDeque.peekLast().entrySet()) {
                String k = entry.getKey();
                TextView v = entry.getValue();
                this.myMap.get(k).setText(v.getText());
            }
            count--;
            // reverse score
            scoreDeque.removeLast();
            score = scoreDeque.peekLast();
            scoreMap.get("score").setText(String.valueOf(score));
            bestScoreDeque.removeLast();
            best_score = bestScoreDeque.peekLast();
            scoreMap.get("best_score").setText(String.valueOf(best_score));
            checkNum();
        }
    }

}
