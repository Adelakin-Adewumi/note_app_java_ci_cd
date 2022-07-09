package com.example.noteapp;

public class Note {
    String info;
    String category;
    String date;

    public Note(String info, String category
            , String date) {
        this.info = info;
        this.category = category;
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public String getCategory() {
        return category;
   }

    public String getDate() {
        return date;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
