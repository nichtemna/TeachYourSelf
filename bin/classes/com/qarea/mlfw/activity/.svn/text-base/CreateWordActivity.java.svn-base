package com.qarea.mlfw.activity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.SelectedDictionary;

public class CreateWordActivity extends BaseActivity implements OnClickListener {

	private AutoCompleteTextView inputWord;
	private long currentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_word);
		currentDate = this.getIntent().getLongExtra(DBSchedule.DATE, 0);
		calendar.setTime(new Date(currentDate));
		clearCalendar();
		calendar.add(Calendar.SECOND, 1);
		inputWord = (AutoCompleteTextView) findViewById(R.id.new_word_text);
		inputWord.setAdapter((ArrayAdapter<String>) null);
		inputWord.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 2) {
					try {
						setDropDownList(s.toString());
						wait(1);
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void setDropDownList(String wordPart) {
		Set<String> treeSet = new TreeSet<String>(dataProvider.getDictionary(
				this).getAllWords(wordPart));

		ArrayAdapter<String> wordsArray = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>(
						treeSet));
		inputWord.setAdapter(wordsArray);
		inputWord.showDropDown();
	}

	private String newWord;

	@Override
	public void onClick(View arg0) {

		newWord = inputWord.getText().toString().trim();// .toLowerCase();
		if (newWord.equals("")) {
			Toast.makeText(CreateWordActivity.this,
					getResources().getString(R.string.enter_word),
					Toast.LENGTH_LONG).show();
		} else {
			boolean wasContain = dataProvider.getDictionary(this).containWord(
					newWord);
			if (!wasContain) {
				showWordDialog();
			} else {
				calendar.setTimeInMillis(currentDate);
				clearCalendar();
				try {
					Log.d("tag", "create word " + newWord + "dicrt"
							+ SelectedDictionary.getDictionaryID());
					dataProvider.insert(SelectedDictionary.getDictionaryID(),
							newWord, calendar.getTimeInMillis());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// return word to fragment to move listview to this word
				Intent intent = new Intent();
				intent.putExtra("newWord", newWord);
				setResult(RESULT_OK, intent);
				finish();
			}
			calendar.setTimeInMillis(currentDate);
		}
	}

	private void showWordDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setTitle(getResources().getString(R.string.new_word));
		alertDialogBuilder
				.setMessage(
						getResources().getString(R.string.add_word_question))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(
										CreateWordActivity.this,
										AddNewWordActivity.class);
								intent.putExtra("word", inputWord.getText()
										.toString());
								intent.putExtra("currentDate", currentDate);
								startActivity(intent);
								CreateWordActivity.this.finish();
							}
						})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
