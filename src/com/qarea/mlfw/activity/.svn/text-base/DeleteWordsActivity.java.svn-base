package com.qarea.mlfw.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.DeleteWordAdapter;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.util.SelectedDictionary;

public class DeleteWordsActivity extends BaseActivity implements OnClickListener {
    public static final int TAG_FOR_CALENDAR = 1;
    public static final String DATE = "date";

    private ListView checkbox_list;
    private Cursor dayWords;
    private ArrayList<Word> words = new ArrayList<Word>();
    private ArrayList<Word> words_delete = new ArrayList<Word>();
    private long currentDate;
    private Button btn_left, btn_right;
    private DeleteWordAdapter adapter;
    private String textForButtonLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_words);
        setTitle(getResources().getString(R.string.app_name));

        Intent intent = getIntent();
        currentDate = intent.getLongExtra("currentDate", 0);
        textForButtonLeft = getResources().getString(R.string.delete);
        checkbox_list = (ListView) findViewById(R.id.listView1);
        btn_left = (Button) findViewById(R.id.button_left);
        btn_right = (Button) findViewById(R.id.button_rigth);
        btn_left.setOnClickListener(this);
        btn_left.setText(textForButtonLeft + "(0)");
        btn_right.setOnClickListener(this);

        refreshData();
        setList();

        checkbox_list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btn_left.setText(textForButtonLeft + "(" + getCheckedItemsCount(checkbox_list)
                                + ")");

            }
        });
    }

    private int getCheckedItemsCount(ListView listView) {
        int checked_count = 0;
        for (int i = 0; i < checkbox_list.getCount(); i++) {
            if (checkbox_list.isItemChecked(i)) {
                checked_count++;
            }
        }
        return checked_count;
    }

    private void setList() {
        checkbox_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new DeleteWordAdapter(this, R.id.listView1, words);
        checkbox_list.setAdapter(adapter);

    }

    private void refreshData() {
        if (dayWords != null)
            dayWords.close();
        dayWords = dataProvider.getDayWords(currentDate, SelectedDictionary.getDictionaryID());
        if (dayWords.getCount() != 0) {
            for (dayWords.moveToFirst(); !dayWords.isAfterLast(); dayWords.moveToNext()) {
                // words.add(dayWords.getString(1));
                words.add(new Word(dayWords));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem check = menu.add(R.string.check_all).setIcon(R.drawable.ic_check);
        check.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for (int i = 0; i < checkbox_list.getCount(); i++) {
                    checkbox_list.setItemChecked(i, true);
                }
                btn_left.setText(textForButtonLeft + "(" + getCheckedItemsCount(checkbox_list)
                                + ")");
                return false;
            }
        });
        MenuItem uncheck = menu.add(R.string.uncheck_all).setIcon(R.drawable.ic_uncheck);
        uncheck.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for (int i = 0; i < checkbox_list.getCount(); i++) {
                    checkbox_list.setItemChecked(i, false);
                }
                btn_left.setText(textForButtonLeft + "(" + getCheckedItemsCount(checkbox_list)
                                + ")");
                return false;
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_left:
                if (getCheckedItemsCount(checkbox_list) == 0) {
                    Toast.makeText(DeleteWordsActivity.this,
                                    getResources().getString(R.string.chose_word),
                                    Toast.LENGTH_LONG).show();
                } else {
                    SparseBooleanArray checked = checkbox_list.getCheckedItemPositions();
                    for (int i = 0; i < checked.size(); i++) {
                        // Item position in adapter
                        int pos = checked.keyAt(i);
                        // Add category if it is checked i.e.) == TRUE
                        if (checked.valueAt(i)) {
                            words_delete.add((Word) adapter.getItem(pos));
                        }
                    }
                    for (int i = 0; i < words_delete.size(); i++) {
                        dataProvider.deleteDayWords(currentDate, words_delete.get(i));
                    }
                    finish();
                }
                break;
            case R.id.button_rigth:
                finish();
                break;
        }
    }
}
