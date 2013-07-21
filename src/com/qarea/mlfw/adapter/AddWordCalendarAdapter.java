package com.qarea.mlfw.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.qarea.mlfw.R;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.util.SelectedDictionary;

/*
 * This adapter for repeating words, creates word in date
 * 
 * */
public class AddWordCalendarAdapter extends CalendarAdapter {

    public AddWordCalendarAdapter(Context context, int textViewResourceId, int month, int year,
                    ArrayList<Word> words, int dictionaryId, long currentDate) {
        super(context, textViewResourceId, month, year, words, dictionaryId, currentDate);
    }

    @Override
    protected void changeDate(long newDate) {
        if (newDate != 0) {
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i).getWord();
                try {
                    dataProvider.insert(SelectedDictionary.getDictionaryID(), word, newDate);
                } catch (Exception e) {
                    if (e.toString().equals(R.string.this_word_exist)) {
                        Toast.makeText(context, "Can't move to this date", Toast.LENGTH_SHORT)
                                        .show();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}
