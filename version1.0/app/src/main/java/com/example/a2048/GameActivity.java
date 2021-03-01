package com.example.a2048;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.os.Bundle;
//import android.os.FileUtils;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    GameBoard newBoard;
    List<String> textAndScore;      // used to save user data

    // declaration of the table view and the maps
    TableLayout myTable;
    Map<String, TextView> myMap;
    Map<String, TextView> scoreMap;
    boolean continueLastGame;       // indicate whether to continue last game

    Deque<Map<String,TextView>> myDeque;       // deque to store previous maps
    Deque<Integer> scoreDeque;       // deque to store previous scores
    Deque<Integer> bestScoreDeque;      // deque to store previous best scores

    // region declaration

    // Button undo and restart
    Button Undo_button;
    Button Restart_button;

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

    TextView score;
    TextView best_score;

    // endregion

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myTable = findViewById(R.id.table);
        continueLastGame = getIntent().getExtras().getBoolean("Continue");

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

        score = findViewById(R.id.score);
        best_score = findViewById(R.id.best_score);

        scoreMap = new HashMap<>();
        scoreMap.put("best_score", best_score);
        scoreMap.put("score", score);

        myDeque = new LinkedList<>();
        scoreDeque = new LinkedList<>();
        bestScoreDeque = new LinkedList<>();

        // endregion

        loadItems();
        int len = textAndScore.size();

        if (continueLastGame) {
            // retrieve the previous maps: first 16th are the tiles, 17th score, 18th best score
            for (int i = 0; i < len; ) {
                Map<String, TextView> curr_map = new HashMap<>();
                for (int j = 0; j < 18; j++, i++) {
                    // retrieve the previous scores
                    if (j == 16) {
                        scoreDeque.addLast(Integer.parseInt(textAndScore.get(i)));
                    } else if ( j == 17) {
                        bestScoreDeque.addLast(Integer.parseInt(textAndScore.get(i)));
                    } else {
                        TextView newText = new TextView(this);      // construct a new TextView
                        curr_map.put("view"+ (j+1), newText);
                        curr_map.get("view"+ (j+1)).setText(textAndScore.get(i));
                    }
                }
                myDeque.addLast(curr_map);      // inflate the map deque with all previous maps
            }

            // retrieve the last map and assign it to myMap
            for (int i = len - 18, j = 1; i < len-2; i++, j++){
                myMap.get("view"+j).setText(textAndScore.get(i));
            }

            // retrieve the score of the last map
            score.setText(textAndScore.get(len-2));
        }

        // retrieve the best score
        if (textAndScore.size() > 0) {
            best_score.setText(textAndScore.get(len-1));
        }

        newBoard = new GameBoard(myMap, scoreMap,this, myDeque, scoreDeque, bestScoreDeque);
        newBoard.startGame(score.getText().toString());

        // set the onSwipeTouchListener
        myTable.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "top", Toast.LENGTH_SHORT).show();
                newBoard.handleUp();
            }

            public void onSwipeRight() {
                Toast.makeText(GameActivity.this, "right", Toast.LENGTH_SHORT).show();
                newBoard.handleRight();
            }

            public void onSwipeLeft() {
                Toast.makeText(GameActivity.this, "left", Toast.LENGTH_SHORT).show();
                newBoard.handleLeft();
            }

            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                newBoard.handleDown();
            }
        });

        Undo_button = findViewById(R.id.undo_button);
        Restart_button = findViewById(R.id.restart_button);

        Restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBoard.startGame("");
            }
        });
        Undo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBoard.undo();
            }
        });
    }

    /**
     * Called when the game is exited.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "stop the activity");
        saveUponExit();
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            // retrieve data from the local file
            textAndScore = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            Log.i(TAG, "length of the loaded data file: " + textAndScore.size());
        } catch (IOException e) {
            Log.e("GameActivity", "Error reading items", e);
            textAndScore = new ArrayList<>();
        }
    }


    // save the games when the user exit the game
    private void saveUponExit() {
        // save the data from all Deque's into the textAndScore array list
        textAndScore = new ArrayList<>();
        while(newBoard.myDeque.peekFirst() != null) {
            Map<String, TextView> curr_map = newBoard.myDeque.removeFirst();
            for (int i = 1; i < curr_map.size() + 1; i++) {
                textAndScore.add(curr_map.get("view"+i).getText().toString());
            }
            textAndScore.add(String.valueOf(newBoard.scoreDeque.removeFirst()));
            textAndScore.add(String.valueOf(newBoard.bestScoreDeque.removeFirst()));
        }
        // write the updated data into the data file
        try {
            FileUtils.writeLines(getDataFile(), textAndScore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

