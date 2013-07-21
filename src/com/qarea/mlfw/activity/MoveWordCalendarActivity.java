package com.qarea.mlfw.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.AddWordCalendarAdapter;
import com.qarea.mlfw.adapter.GridCalendarAdapter;
import com.qarea.mlfw.model.Word;

/*
 * Get data for moving words and communicate with adapters
 * 
 * */
public class MoveWordCalendarActivity extends BaseActivity {

    private Calendar _calendar;
    private int month, year;
    private GridView calendarView;
    private GridCalendarAdapter gridCalendarAdapter;
    private AddWordCalendarAdapter addWordCalendarAdapter;
    private ArrayList<Word> words_move = new ArrayList<Word>();
    private int dictionaryId;
    private long currentDate;
    private String mode;

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_word_calendar);
        Intent intent = getIntent();
        if (intent.hasExtra(BaseActivity.MOVE_ARRAY_WORDS)) {
            words_move = (ArrayList<Word>) this.getIntent().getSerializableExtra(
                            BaseActivity.MOVE_ARRAY_WORDS);
        }
        if (intent.hasExtra(BaseActivity.MOVE_WORD)) {
            words_move.add((Word) this.getIntent().getSerializableExtra(BaseActivity.MOVE_WORD));
        }
        currentDate = intent.getLongExtra(BaseActivity.MOVE_TIME, -1L);
        mode = intent.getStringExtra(BaseActivity.REPEATE_WORD);
        dictionaryId = intent.getIntExtra(BaseActivity.MOVE_DICTIONARY, -1);
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        calendarView = (GridView) this.findViewById(R.id.calendar_Grid_View);

        if (mode.equals(BaseActivity.REPEATE_WORD_ADD)) {
            addWordCalendarAdapter = new AddWordCalendarAdapter(this, R.id.calendar_day_gridcell,
                            month, year, words_move, dictionaryId, currentDate);
            calendarView.setAdapter(addWordCalendarAdapter);
        } else if (mode.equals(BaseActivity.REPEATE_WORD_MOVE)) {
            gridCalendarAdapter = new GridCalendarAdapter(this, R.id.calendar_day_gridcell, month,
                            year, words_move, dictionaryId, currentDate);
            calendarView.setAdapter(gridCalendarAdapter);
        }
    }
}
