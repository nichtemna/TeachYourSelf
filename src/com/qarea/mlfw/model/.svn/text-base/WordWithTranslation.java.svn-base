package com.qarea.mlfw.model;

import java.io.Serializable;

import com.qarea.mlfw.database.entities.Translate;
import com.qarea.mlfw.database.entities.Words;

import android.database.Cursor;
import android.provider.BaseColumns;

public class WordWithTranslation extends MlfwData implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int dictionary_id;
	private String word;
	private String translation;

	public WordWithTranslation() {
	}

	public WordWithTranslation(Cursor cursor) {
		super(cursor);
	}

	public WordWithTranslation(int id, int dictionary_id, String word,
			String translation) {
		super();
		this.id = id;
		this.dictionary_id = dictionary_id;
		this.word = word;
		this.translation = translation;
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

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
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

		i = cursor.getColumnIndex(Translate.TRANSLATES);
		if (i != -1) {
			translation = cursor.getString(i);
		}
	}
}
