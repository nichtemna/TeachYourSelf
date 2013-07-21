package com.qarea.mlfw.model;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.qarea.mlfw.database.entities.Words;

public class Word extends MlfwData {

    /**
     * Local variable is serialVersionUID with type long
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private int dictionary_id;
    private String word;

    public Word() {
    }

    public Word(Cursor cursor) {
        super(cursor);
    }

    public Word(int id, int dictionary_id, String word) {
        super();
        this.id = id;
        this.dictionary_id = dictionary_id;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDictionary_id() {
        return dictionary_id;
    }

    public void setDictionary_id(int dictionary_id) {
        this.dictionary_id = dictionary_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    protected void fromCursor(Cursor cursor) {
        int i = cursor.getColumnIndex(BaseColumns._ID);
        if (i != -1) {
            id = cursor.getInt(i);
        }
        i = cursor.getColumnIndex(Words.DICTIONARY_ID);
        if (i != -1) {
            dictionary_id = cursor.getInt(i);
        }

        i = cursor.getColumnIndex(Words.WORD);
        if (i != -1) {
            word = cursor.getString(i);
        }
    }

}
