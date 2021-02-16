package com.example.a2048;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {

    private static final String TAG = "GameBoard";

    Map<String, Boolean> column_tracker;    // each tile cannot be combined twice in each round

    Map<String, TextView> myMap;
    boolean tileMoved;  // keep track of whether to add tile or not

    public GameBoard(Map<String, TextView> myMap) {
        this.myMap = myMap;
        tileMoved = false;
        column_tracker = new HashMap<>();
        column_tracker.put("column_tracker1", false);
        column_tracker.put("column_tracker2", false);
        column_tracker.put("column_tracker3", false);
        column_tracker.put("column_tracker4", false);
    }

    /*
     * Start the game
     */
    public void startGame() {
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

    /*
     * A helper method:
     * Add a new tile with text "2" into the game board after shifting
     */
    public void addTile() {
        ArrayList<String> remainTile = new ArrayList<>();
        String curr_value;

        for (Map.Entry<String, TextView> pair: myMap.entrySet()) {
            curr_value = pair.getValue().getText().toString();
            if (curr_value.equals("0")){
                remainTile.add(pair.getKey());
            }
        }

        int randomIndex = (int) (Math.random() * remainTile.size());
        String randomTile = remainTile.get(randomIndex);
        myMap.get(randomTile).setText("2");

        tileMoved = false;
    }


    /*
     * Handle the swipe up case
     */
    public void handleUp() {
        String curr_tile;
        String up_tile;
        for (Map.Entry<String, Boolean> pair: column_tracker.entrySet()) {
            pair.setValue(false);
        }

        // three rounds
        for (int j = 17; j > 8; j -= 4) {
            for (int i = 5; i < j; i++){
                curr_tile = "view" + i;
                up_tile = "view" + (i-4);
                shiftUp(curr_tile, up_tile, i);
            }
        }

        // TODO:if game is not over, add one more tile
        // if nothing is moved, don't add
        if (tileMoved == true) {
            addTile();
        }
    }

    /*
     * shift a tile up
     */
    public void shiftUp(String curr_tile, String up_tile, int view_num){
        while (view_num > 4) {
            view_num -= 4;
        }
        String column = "column_tracker" + view_num;    // get the current column

        String curr_value = myMap.get(curr_tile).getText().toString();
        String up_value = myMap.get(up_tile).getText().toString();

        // shift up
        if (!curr_value.equals("0") && up_value.equals("0")) {
            myMap.get(up_tile).setText(curr_value);
            myMap.get(curr_tile).setText("0");
            tileMoved = true;
        }

        // combine if values are the same
        // don't combine if the up_tile is already combined before
        else if (!up_value.equals("0") && up_value.equals(curr_value)) {
            if (!column_tracker.get(column)) {
                int new_value = Integer.parseInt(curr_value) + Integer.parseInt(up_value);
                myMap.get(up_tile).setText(String.valueOf(new_value));
                myMap.get(curr_tile).setText("0");
                tileMoved = true;
                column_tracker.put(column, true);
            }
        }
    }



}
