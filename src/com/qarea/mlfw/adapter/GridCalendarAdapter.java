package com.qarea.mlfw.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.qarea.mlfw.model.Word;

/*
 * This adapter for moving words to other date
 * 
 * */
public class GridCalendarAdapter extends CalendarAdapter implements OnClickListener {
  

    public GridCalendarAdapter(Context context, int textViewResourceId, int month, int year,
                    ArrayList<Word> words, int dictionaryId, long currentDate) {
        super(context, textViewResourceId, month, year, words, dictionaryId, currentDate);     
    }

  
    @Override
    protected void changeDate(long newDate) {
        if (newDate != 0) {
            for (int i = 0; i < words.size(); i++) {
                Word word = words.get(i);
                boolean success = dataProvider.changeDateInWords(currentDate, word, newDate);
                if (!success) {
                    Toast.makeText(context,"Word " + word.getWord() + " already exist in this day.",
                                    Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
