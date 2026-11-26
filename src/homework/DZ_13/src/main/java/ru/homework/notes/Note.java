package ru.homework.notes;

public class Note {
    private final String title;
    private final String body;
    private final String category;
    private final String date;
    private final int color;

    public Note(String title, String body, String category, String date, int color) {
        this.title = title;
        this.body = body;
        this.category = category;
        this.date = date;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public int getColor() {
        return color;
    }
}
