package com.c2c.Notes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.c2c.Notes.R;
import com.c2c.Notes.adapters.NotesAdapter;
import com.c2c.Notes.database.NotesDatabase;
import com.c2c.Notes.entities.Note;
import com.c2c.Notes.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTE = 3;

    private RecyclerView notesRecyclerView;
    List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgAddNoteMain = findViewById(R.id.imgAddNoteMain);
        imgAddNoteMain.setOnClickListener(view -> startActivityForResult(
                new Intent(getApplicationContext(), CreateNoteActivity.class),
                REQUEST_CODE_ADD_NOTE
        ));

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        // when the activity is launched all notes will be displayed from DB
        getNotes(REQUEST_CODE_SHOW_NOTE);
    }

    private void getNotes(final int requestCode) {

        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                Log.d("My_Notes: ", notes.toString());

                /*//if note list is empty --> add data from DB
                //else adding new note to adapter
                if (noteList.size() == 0) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                }
                //scroll to top of list
                notesRecyclerView.smoothScrollToPosition(0);*/

                if (requestCode == REQUEST_CODE_SHOW_NOTE) {
                    //adding all notes from DB to noteList for display
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    //adding first note(new) from DB to noteList
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    //removing note from clicked position and adding the latest updated note
                    //from same position at DB and notifying adapter for change at same positon
                    noteList.remove(noteClickedPosition);
                    noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);
                }


            }
        }

        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            //new note will added and result is sent back to activity
            getNotes(REQUEST_CODE_ADD_NOTE);
        } else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if(data != null) {
                //already available note will update and sent back to activity
                getNotes(REQUEST_CODE_UPDATE_NOTE);
            }
        }


    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }
}