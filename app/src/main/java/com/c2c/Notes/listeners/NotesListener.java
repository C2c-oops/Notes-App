package com.c2c.Notes.listeners;

import com.c2c.Notes.entities.Note;

public interface NotesListener {

    void onNoteClicked(Note note, int position);

}
