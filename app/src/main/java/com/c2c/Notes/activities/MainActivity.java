package com.c2c.Notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.c2c.Notes.R;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgAddNoteMain = findViewById(R.id.imgAddNoteMain);
        imgAddNoteMain.setOnClickListener(view -> startActivityForResult(
                new Intent(getApplicationContext(), CreateNoteActivity.class),
                REQUEST_CODE_ADD_NOTE
        ));
    }
}