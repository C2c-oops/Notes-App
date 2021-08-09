package com.c2c.Notes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c2c.Notes.R;
import com.c2c.Notes.database.NotesDatabase;
import com.c2c.Notes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle;
    private EditText inputNoteSubtitle;
    private EditText inputNote;

    private TextView txtDateTime;

    private View viewSubtitleIndicator;

    private String selectedNoteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> onBackPressed());

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNote = findViewById(R.id.inputNote);

        txtDateTime = findViewById(R.id.txtDateTime);
        txtDateTime.setText(
                new SimpleDateFormat(
                        "EEEE, dd MMMM yyyy HH:mm a",
                        Locale.getDefault())
                .format(new Date())
        );

        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        ImageView imgSaveNote = findViewById(R.id.imgSave);
        imgSaveNote.setOnClickListener(view -> saveNote());

        //default color
        selectedNoteColor = "#333333";

        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    private void saveNote() {

        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't be empty..!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteSubtitle.getText().toString().trim().isEmpty()
                && inputNote.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note can't be empty..!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Note's Object
        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubTitle(inputNoteSubtitle.getText().toString());
        note.setNoteText(inputNote.getText().toString());
        note.setDateTime(txtDateTime.getText().toString());
        note.setColor(selectedNoteColor);

        //Async task to save note (Room doesn't allow DB operation on Main Thread)
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNoteTask().execute();
    }

    private void initMiscellaneous() {

        final LinearLayout layoutMisc = findViewById(R.id.layoutMisc);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMisc);
        layoutMisc.findViewById(R.id.textMisc).setOnClickListener(view -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final ImageView imgColor1 = layoutMisc.findViewById(R.id.imgColor1);
        final ImageView imgColor2 = layoutMisc.findViewById(R.id.imgColor2);
        final ImageView imgColor3 = layoutMisc.findViewById(R.id.imgColor3);
        final ImageView imgColor4 = layoutMisc.findViewById(R.id.imgColor4);
        final ImageView imgColor5 = layoutMisc.findViewById(R.id.imgColor5);

        layoutMisc.findViewById(R.id.viewColor1).setOnClickListener(view -> {
            selectedNoteColor = "#333333";
            imgColor1.setImageResource(R.drawable.ic_baseline_done_24);
            imgColor2.setImageResource(0);
            imgColor3.setImageResource(0);
            imgColor4.setImageResource(0);
            imgColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMisc.findViewById(R.id.viewColor2).setOnClickListener(view -> {
            selectedNoteColor = "#FDBE3B";
            imgColor1.setImageResource(0);
            imgColor2.setImageResource(R.drawable.ic_baseline_done_24);
            imgColor3.setImageResource(0);
            imgColor4.setImageResource(0);
            imgColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMisc.findViewById(R.id.viewColor3).setOnClickListener(view -> {
            selectedNoteColor = "#FF4842";
            imgColor1.setImageResource(0);
            imgColor2.setImageResource(0);
            imgColor3.setImageResource(R.drawable.ic_baseline_done_24);
            imgColor4.setImageResource(0);
            imgColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMisc.findViewById(R.id.viewColor4).setOnClickListener(view -> {
            selectedNoteColor = "#3A52Fc";
            imgColor1.setImageResource(0);
            imgColor2.setImageResource(0);
            imgColor3.setImageResource(0);
            imgColor4.setImageResource(R.drawable.ic_baseline_done_24);
            imgColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMisc.findViewById(R.id.viewColor5).setOnClickListener(view -> {
            selectedNoteColor = "#000000";
            imgColor1.setImageResource(0);
            imgColor2.setImageResource(0);
            imgColor3.setImageResource(0);
            imgColor4.setImageResource(0);
            imgColor5.setImageResource(R.drawable.ic_baseline_done_24);
            setSubtitleIndicatorColor();
        });

    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }


}