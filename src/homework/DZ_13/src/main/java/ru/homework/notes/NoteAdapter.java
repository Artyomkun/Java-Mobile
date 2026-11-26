package ru.homework.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    public interface OnNoteClickListener {
        void onNoteClick(Note note, int position);

        void onNoteLongClick(Note note, int position);
    }

    private final List<Note> notes = new ArrayList<>();
    private final OnNoteClickListener listener;

    public NoteAdapter(OnNoteClickListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.body.setText(note.getBody());
        holder.category.setText(note.getCategory());
        holder.date.setText(note.getDate());
        holder.card.setCardBackgroundColor(note.getColor());
        holder.itemView.setOnClickListener(view -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onNoteClick(notes.get(currentPosition), currentPosition);
            }
        });
        holder.itemView.setOnLongClickListener(view -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onNoteLongClick(notes.get(currentPosition), currentPosition);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        final CardView card;
        final TextView title;
        final TextView body;
        final TextView category;
        final TextView date;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (CardView) itemView;
            title = itemView.findViewById(R.id.note_title);
            body = itemView.findViewById(R.id.note_body);
            category = itemView.findViewById(R.id.note_category);
            date = itemView.findViewById(R.id.note_date);
        }
    }
}
