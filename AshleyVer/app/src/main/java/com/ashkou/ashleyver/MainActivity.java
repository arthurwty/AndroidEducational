package com.ashkou.ashleyver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameContent.class);
        startActivity(intent);
    }

//    public void chooseMode(View view) {
//    }
//
//    public void showHelp(View view) {
//    }
//
//    public void showSettings(View view) {
//    }
}