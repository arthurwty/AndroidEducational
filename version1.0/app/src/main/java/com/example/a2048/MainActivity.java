package com.example.a2048;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button new_game_button;
    Button continue_button;
    Button mode_button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new_game_button = findViewById(R.id.New_game_button);
        continue_button = findViewById(R.id.continue_button);
        mode_button = findViewById(R.id.mode_button);

        new_game_button.setBackgroundTintList(null);
        continue_button.setBackgroundTintList(null);
        mode_button.setBackgroundTintList(null);

        new_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("Continue", false);
                startActivity(i);
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("Continue", true);
                startActivity(i);
            }
        });
    }
}