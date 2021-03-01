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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    List<String> textAndScore;      // used to save user data

    // declaration of the table view and the maps
    TableLayout myTable;
    Map<String, TextView> myMap;
    Map<String, TextView> scoreMap;
    boolean continueLastGame;

    // Button undo and restart
    Button Undo_button;
    Button Restart_button;

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

        score = findViewById(R.id.score);
        best_score = findViewById(R.id.best_score);

        scoreMap = new HashMap<>();
        scoreMap.put("best_score", best_score);
        scoreMap.put("score", score);

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


        if (continueLastGame) {
            loadItems();
            // restore the last map
            for (int i = 0; i < 16; i++) {
                myMap.get("view"+ (i+1)).setText(textAndScore.get(i));
            }
            score.setText(textAndScore.get(16));
            best_score.setText(textAndScore.get(17));
        } else {
            try {
                // retrieve the best score
                loadItems();
                if (textAndScore.size() > 0) {
                    best_score.setText(textAndScore.get(17));
                }
                // then clear the local file
                FileUtils.write(getDataFile(), "", Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GameBoard newBoard = new GameBoard(myMap, scoreMap,this);
        newBoard.startGame(score.getText().toString());


        // endregion


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
        saveItems();
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            // retrieve the last map from the local file
            textAndScore = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            textAndScore = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            // store the current map, write to the local file
            textAndScore = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                String curr_view = myMap.get("view"+(i+1)).getText().toString();
                textAndScore.add(curr_view);
            }
            textAndScore.add(score.getText().toString());
            textAndScore.add(best_score.getText().toString());
            FileUtils.writeLines(getDataFile(), textAndScore);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}

