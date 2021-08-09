package com.c2c.Notes.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c2c.Notes.R;
import com.c2c.Notes.entities.Note;
import com.c2c.Notes.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{

    private List<Note> notes;

    private NotesListener notesListener;

    private Timer timer;
    private List<Note> notesSources;

    public NotesAdapter(List<Note> notes, NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;
        notesSources = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(view -> notesListener.onNoteClicked(notes.get(position), position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtSubtitle;
        TextView txtDateTime;

        LinearLayout layoutNote;

        RoundedImageView imgNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtSubtitle = itemView.findViewById(R.id.txtSubtitle);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imgNote = itemView.findViewById(R.id.imgNote);
        }

        void setNote(Note note) {
            txtTitle.setText(note.getTitle());
            if (note.getSubTitle().trim().isEmpty()) {
                txtSubtitle.setVisibility(View.GONE);
            } else {
                txtSubtitle.setText(note.getSubTitle());
            }
            txtDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (note.getImagePath() != null) {
                imgNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imgNote.setVisibility(View.VISIBLE);
            } else {
                imgNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                   notes = notesSources;
                } else {
                    ArrayList<Note> searchedTempNote = new ArrayList<>();
                    for (Note note : notesSources) {
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || note.getSubTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            searchedTempNote.add(note);
                        }
                    }

                    notes = searchedTempNote;
                }

                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
