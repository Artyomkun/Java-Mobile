package ru.homework.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String PREFERENCES_NAME = "notes";
    private static final String KEY_NOTES = "saved_notes";
    private static final String TAG = "NotesActivity";
    private static final String CATEGORY_PERSONAL = "ЛИЧНОЕ";
    private static final int[] CARD_COLORS = {
            Color.rgb(255, 255, 255),
            Color.rgb(249, 246, 238),
            Color.rgb(239, 247, 244),
            Color.rgb(246, 243, 249)
    };

    private final List<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private TextView notesCount;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        notesCount = findViewById(R.id.notes_count);
        RecyclerView recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new androidx.recyclerview.widget.DefaultItemAnimator());
        recyclerView.addItemDecoration(new NotesSpacingDecoration(
                (int) (12 * getResources().getDisplayMetrics().density)));
        adapter = new NoteAdapter(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note, int position) {
                showNote(note);
            }

            @Override
            public void onNoteLongClick(Note note, int position) {
                confirmDelete(note, position);
            }
        });
        recyclerView.setAdapter(adapter);

        loadNotes();
        if (notes.isEmpty()) {
            addSampleNotes();
        }
        updateList();
        findViewById(R.id.add_note_button).setOnClickListener(view -> showEditor());
    }

    private void addSampleNotes() {
        notes.add(new Note("Идеи для выходных",
                "Съездить за город, взять термос и фотоаппарат. Посмотреть маршрут до озера.",
                "ЛИЧНОЕ", "СЕГОДНЯ · 10:42", CARD_COLORS[1]));
        notes.add(new Note("Купить домой",
                "Лампы для кухни, кофе, свежие цветы и корм для кота.",
                "ПОКУПКИ", "ВЧЕРА · 18:20", CARD_COLORS[2]));
        notes.add(new Note("Планы на неделю",
                "Закончить проект, записаться к стоматологу и не забыть позвонить маме.",
                "ПЛАНЫ", "24 НОЯБРЯ", CARD_COLORS[3]));
        saveNotes();
    }

    private void showEditor() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(24), dp(12), dp(24), 0);

        EditText titleInput = new EditText(this);
        titleInput.setSingleLine(true);
        titleInput.setHint(R.string.note_title_hint);
        titleInput.setTextSize(17);
        form.addView(titleInput);

        EditText bodyInput = new EditText(this);
        bodyInput.setGravity(Gravity.TOP | Gravity.START);
        bodyInput.setHint(R.string.note_body_hint);
        bodyInput.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        bodyInput.setMinLines(3);
        form.addView(bodyInput, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(112)));

        new AlertDialog.Builder(this)
                .setTitle(R.string.new_note)
                .setView(form)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String body = bodyInput.getText().toString().trim();
                    if (title.isEmpty()) {
                        title = getString(R.string.untitled_note);
                    }
                    notes.add(0, new Note(title, body, CATEGORY_PERSONAL, currentDate(),
                            CARD_COLORS[notes.size() % CARD_COLORS.length]));
                    saveNotes();
                    updateList();
                })
                .show();
    }

    private void showNote(Note note) {
        new AlertDialog.Builder(this)
                .setTitle(note.getTitle())
                .setMessage(note.getBody().isEmpty()
                        ? getString(R.string.empty_note) : note.getBody())
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private void confirmDelete(Note note, int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_note_title)
                .setMessage(getString(R.string.delete_note_message, note.getTitle()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    notes.remove(position);
                    saveNotes();
                    updateList();
                })
                .show();
    }

    private void updateList() {
        adapter.setNotes(notes);
        notesCount.setText(getResources().getQuantityString(
                R.plurals.notes_count, notes.size(), notes.size()));
    }

    private void loadNotes() {
        String saved = preferences.getString(KEY_NOTES, null);
        if (saved == null) {
            return;
        }
        try {
            JSONArray array = new JSONArray(saved);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                notes.add(new Note(
                        item.getString("title"),
                        item.getString("body"),
                        item.getString("category"),
                        item.getString("date"),
                        item.getInt("color")));
            }
        } catch (JSONException exception) {
            Log.e(TAG, "Could not read saved notes", exception);
            Toast.makeText(this, R.string.notes_load_error, Toast.LENGTH_LONG).show();
            notes.clear();
            preferences.edit().remove(KEY_NOTES).apply();
        }
    }

    private void saveNotes() {
        JSONArray array = new JSONArray();
        for (Note note : notes) {
            JSONObject item = new JSONObject();
            try {
                item.put("title", note.getTitle());
                item.put("body", note.getBody());
                item.put("category", note.getCategory());
                item.put("date", note.getDate());
                item.put("color", note.getColor());
                array.put(item);
            } catch (JSONException exception) {
                throw new IllegalStateException("Could not save note", exception);
            }
        }
        preferences.edit().putString(KEY_NOTES, array.toString()).apply();
    }

    private String currentDate() {
        return new SimpleDateFormat("d MMMM · HH:mm", new Locale("ru"))
                .format(new Date()).toUpperCase(new Locale("ru"));
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
