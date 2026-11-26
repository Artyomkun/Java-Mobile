package ru.homework.notes;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class NotesSpacingDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;

    public NotesSpacingDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.bottom = spacing;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spacing;
        }
    }
}
