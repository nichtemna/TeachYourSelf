package com.qarea.mlfw.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qarea.mlfw.DBHelper;
import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.SelectedDictionary;
import com.qarea.mlfw.util.WordStatistic;

public class CheckingKnowledgeActivity extends
		AbstractCheckingKnowledgeActivity implements OnInitListener,
		OnClickListener {

	private ArrayList<WordStatistic> alhmAllForList = new ArrayList<WordStatistic>();;

	private EditText etWordTranslate;
	private Button bNext, bStop;
	private ImageButton voice_btn;

	private int wordIndex;
	private String nowWord;
	private String[] delimiters = new String[] { "," };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checking_knowledge);
		initialize();
		setVoice();
		setDictionarySpinner();
	}

	private void initialize() {
		wordIndex = 0;
		alhmAllForList = new ArrayList<WordStatistic>();
		etWordTranslate = (EditText) findViewById(R.id.etWord);
		sDictionary = (Spinner) findViewById(R.id.sDictionary);

		bNext = (Button) findViewById(R.id.bNext);
		bNext.setOnClickListener(this);

		bStop = (Button) findViewById(R.id.bStop);
		bStop.setOnClickListener(this);

		voice_btn = (ImageButton) findViewById(R.id.voice_btn);
		voice_btn.setOnClickListener(this);
		voice_btn.setVisibility(showSound() ? View.VISIBLE : View.INVISIBLE);
	}

	private void setVoice() {
		myTts = new TextToSpeech(this, this);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			myTts.setLanguage(Locale.ENGLISH);
		} else if (status == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
		if (myTts.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
			myTts.setLanguage(Locale.ENGLISH);
		}
	}

	private void setDictionarySpinner() {
		ArrayList<String> dictionaryArray = new ArrayList<String>();

		Cursor allDictionary = dataProvider.getSelectedDictionary();
		while (allDictionary.moveToNext())
			dictionaryArray.add(allDictionary.getString(allDictionary
					.getColumnIndex(DBDictionary.NAME)));
		allDictionary.close();
		ArrayAdapter<String> sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dictionaryArray);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDictionary.setAdapter(sadapter);
		sDictionary.setSelection(SelectedDictionary.getDictionaryList()
				.indexOf(SelectedDictionary.getDictionaryID()));
		sDictionary.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				TextView raw = (TextView) arg1;
				int dictionaryID = dataProvider.getDictionaryIdByName(raw
						.getText().toString());
				SelectedDictionary.setDictionaryID(dictionaryID);
				wordIndex = 0;
				refreshWord();

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(SAVE_WORD, dictionaryID);
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void refreshWord() {
		alhmAllForList = getFillHashMap();
		if (alhmAllForList.size() > 0) {
			if (alhmAllForList.size() > wordIndex) {
				nowWord = alhmAllForList.get(wordIndex).getWordName();
				((TextView) findViewById(R.id.tvWord)).setText(nowWord);
				etWordTranslate.setText("");
				etWordTranslate.setFocusableInTouchMode(true);
				bNext.setEnabled(true);
			} else {
				finish();
			}
		} else {
			// if user choose empty dictionary - clear all text, disable next
			// button and show chose dictionary dialog
			((TextView) findViewById(R.id.tvWord)).setText("");
			etWordTranslate.setText("");
			etWordTranslate.setFocusable(false);
			bNext.setEnabled(false);
			showChoseDialog();
		}
	}

	private ArrayList<WordStatistic> getFillHashMap() {
		ArrayList<WordStatistic> hm = new ArrayList<WordStatistic>();
		calendar.setTime(new Date());
		clearCalendar();
		Long fromDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		Long toDate = calendar.getTimeInMillis();
		Cursor allIdAndWords = null;
		allIdAndWords = dataProvider.getAllWordsIdAndName(fromDate, toDate);
		if (allIdAndWords == null) {
			showChoseDialog();
		} else {
			if (allIdAndWords.getCount() > 0) {
				int id;
				float percent = 0;
				int allAttempts = 0;
				WordStatistic hmTmp;
				allIdAndWords.moveToFirst();
				do {
					id = allIdAndWords.getInt(0);
					percent = 0;
					allAttempts = dataProvider.getCountCorrectOrAllAnswer(id,
							false);
					int correctAnswerCount = dataProvider
							.getCountCorrectOrAllAnswer(id, true);
					if (allAttempts != 0) {
						percent = ((float) correctAnswerCount / (float) allAttempts) * 100;
					}
					hmTmp = new WordStatistic(
							id,
							allIdAndWords.getString(allIdAndWords
									.getColumnIndex(DBHelper.DBWords.WORD_NAME)),
							percent,
							allAttempts,
							correctAnswerCount,
							(new Date()).getTime(),
							allIdAndWords.getInt(allIdAndWords
									.getColumnIndex(DBHelper.DBWords.DICTIONARY_ID)));
					hm.add(hmTmp);
				} while (allIdAndWords.moveToNext());
			}
			allIdAndWords.close();
		}

		return hm;
	}

	@Override
	public void onClick(View v) {
		String wordTranslate = etWordTranslate.getText().toString().trim();
		switch (v.getId()) {
		case R.id.bNext:
			if (wordTranslate.equals("") || wordTranslate == null) {
				wordIndex++;
				refreshWord();
				return;
			}
			Date date = new Date();
			long today = date.getTime();
			int result = dataProvider.getDictionary(this)
					.checkOfTheCorrectness(nowWord, wordTranslate);

			if (result == 0) {
				dataProvider.insertStatus(alhmAllForList.get(wordIndex)
						.getWordId(), today, false);
				Toast.makeText(this, "Uncorrect", Toast.LENGTH_SHORT).show();
			} else if (result == 1) {
				dataProvider.insertStatus(alhmAllForList.get(wordIndex)
						.getWordId(), today, true);
				Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "No word in dictionary",
						Toast.LENGTH_SHORT).show();
			}
			wordIndex++;
			refreshWord();
			break;

		case R.id.bStop:
			if (wordTranslate.equals("") || wordTranslate == null) {
				wordIndex++;
				finish();
				break;
			}
			// parsedWordTranslate = translateWordParser(wordTranslate);
			date = new Date();
			today = date.getTime();
			result = dataProvider.getDictionary(this).checkOfTheCorrectness(
					nowWord, wordTranslate);

			if (result == 0) {
				dataProvider.insertStatus(alhmAllForList.get(wordIndex)
						.getWordId(), today, false);
				Toast.makeText(this, "Uncorrect", Toast.LENGTH_SHORT).show();
			} else if (result == 1) {
				dataProvider.insertStatus(alhmAllForList.get(wordIndex)
						.getWordId(), today, true);
				Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "No word in dictionary",
						Toast.LENGTH_SHORT).show();
			}
			finish();
			break;
		case R.id.voice_btn:
			myTts.speak(nowWord, TextToSpeech.QUEUE_ADD, null);
			break;
		}

	}

	private boolean showSound() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		return settings.getBoolean(Extras.VOICE_ENABLED, false);
	}

	@Override
	protected void onDestroy() {
		if (myTts != null) {
			myTts.stop();
			myTts.shutdown();
		}
		super.onDestroy();
	}
}
