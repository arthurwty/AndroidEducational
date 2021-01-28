package com.example.a2048;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    String TAG = "GameActivity";

    TableLayout myTable;
    Map<String, TextView> myMap;

    // region declaration

    TextView view1;
    TextView view2;
    TextView view3;
    TextView view4;

    TextView view5;
    TextView view6;
    TextView view7;
    TextView view8;

    TextView view9;
    TextView view10;
    TextView view11;
    TextView view12;

    TextView view13;
    TextView view14;
    TextView view15;
    TextView view16;


    // endregion

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myTable = findViewById(R.id.table);

        // region initialization

        view1 = findViewById(R.id.textView1);
        view2 = findViewById(R.id.textView2);
        view3 = findViewById(R.id.textView3);
        view4 = findViewById(R.id.textView4);

        view5 = findViewById(R.id.textView5);
        view6 = findViewById(R.id.textView6);
        view7 = findViewById(R.id.textView7);
        view8 = findViewById(R.id.textView8);

        view9 = findViewById(R.id.textView9);
        view10 = findViewById(R.id.textView10);
        view11 = findViewById(R.id.textView11);
        view12 = findViewById(R.id.textView12);

        view13 = findViewById(R.id.textView13);
        view14 = findViewById(R.id.textView14);
        view15 = findViewById(R.id.textView15);
        view16 = findViewById(R.id.textView16);

        myMap = new HashMap<>();
        myMap.put("view1", view1);
        myMap.put("view2", view2);
        myMap.put("view3", view3);
        myMap.put("view4", view4);
        myMap.put("view5", view5);
        myMap.put("view6", view6);
        myMap.put("view7", view7);
        myMap.put("view8", view8);
        myMap.put("view9", view9);
        myMap.put("view10", view10);
        myMap.put("view11", view11);
        myMap.put("view12", view12);
        myMap.put("view13", view13);
        myMap.put("view14", view14);
        myMap.put("view15", view15);
        myMap.put("view16", view16);

        // endregion

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


        // set the onSwipeTouchListener
        myTable.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this){
            public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "top", Toast.LENGTH_SHORT).show();
                handleUp(myMap);
            }
            public void onSwipeRight() {
                Toast.makeText(GameActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(GameActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*
     * Add a new tile with text "2" into the game board
     */
    public void addTile(Map<String, TextView> map) {
        ArrayList<String> remainTile = new ArrayList<>();
        String curr_value;

        for (Map.Entry<String, TextView> pair: map.entrySet()) {
            curr_value = pair.getValue().getText().toString();
            if (curr_value.equals("0")){
                remainTile.add(pair.getKey());
            }
        }

        int randomIndex = (int) (Math.random() * remainTile.size());
        String randomTile = remainTile.get(randomIndex);
        map.get(randomTile).setText("2");

    }


    /*
     * shift a tile up
     */
    public void shiftUp(String curr_tile, String up_tile, Map<String, TextView> map){
        String curr_value = map.get(curr_tile).getText().toString();
        String up_value = map.get(up_tile).getText().toString();

        // shift up
        if (!curr_value.equals("0") && up_value.equals("0")) {
            map.get(up_tile).setText(curr_value);
            map.get(curr_tile).setText("0");
        }
        // combine if values are the same
        else if (!up_value.equals("0") && up_value.equals(curr_value)) {
            int new_value = Integer.parseInt(curr_value) + Integer.parseInt(up_value);
            map.get(up_tile).setText(String.valueOf(new_value));
            map.get(curr_tile).setText("0");
        }
    }

    /*
     * Handle the swipe up case
     */
    public void handleUp(Map<String, TextView> map) {
        String curr_tile;
        String up_tile;

        for (int j = 17; j > 8; j -= 4) {
            for (int i = 5; i < j; i++){
                curr_tile = "view" + i;
                up_tile = "view" + (i-4);
                shiftUp(curr_tile, up_tile, map);
            }
        }

        // if game is not over, add one more tile TODO: need to modify this later
        addTile(map);
    }
}