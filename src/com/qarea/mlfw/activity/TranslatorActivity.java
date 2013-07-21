package com.qarea.mlfw.activity;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.newdictionary.NewDictionary.DBDictionary;
import com.qarea.mlfw.util.SelectedDictionary;

public class TranslatorActivity extends BaseActivity implements
		OnClickListener, OnInitListener {

	private Spinner sDictionary;
	private EditText eDictionaty;
	private ListView lTranslate;
	private NewDictionary dictionary;
	private Context context;
	private ImageButton voice_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transator);

		initialize();
		setDictionarySpinner();
		setVoice();
	}

	private void setDictionarySpinner() {

		ArrayList<String> dictionaryArray = new ArrayList<String>();
		Cursor allDictionary = dataProvider.getSelectedDictionary();// .getAllDictionary(true);
		if (allDictionary.moveToFirst()) {
			do {
				dictionaryArray.add(allDictionary.getString(allDictionary
						.getColumnIndex(DBDictionary.NAME)));
			} while (allDictionary.moveToNext());
		}

		allDictionary.close();
		ArrayAdapter<String> sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dictionaryArray);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDictionary.setAdapter(sadapter);
		sDictionary.setSelection(0);
		sDictionary.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				TextView raw = (TextView) arg1;
				lTranslate.setVisibility(View.GONE);
				SelectedDictionary.setDictionaryID(dataProvider
						.getDictionaryIdByName(raw.getText().toString()));

				dictionary = dataProvider.getDictionary(context);

				String word = eDictionaty.getText().toString().trim();
				if (word != null && !word.equals("")) {
					/* WordValue */
					String translate = dictionary.findTranslate(word);
					
					if (translate == null  || translate.equals("")) {
						Toast.makeText(arg1.getContext(), R.string.no_such_word,
								Toast.LENGTH_SHORT).show();
						lTranslate.setAdapter(null);
						return;
					}
					ArrayList<String> translation = new ArrayList<String>();
					translation.add(translate);
					// translate.getTranslation(translation);
					if (translation.size() > 0) {
						lTranslate.setVisibility(View.VISIBLE);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								arg0.getContext(),
								android.R.layout.simple_list_item_1,
								translation);
						lTranslate.setAdapter(adapter);
					} else {
						lTranslate.setAdapter(null);
						Toast.makeText(TranslatorActivity.this,
								R.string.no_such_word, Toast.LENGTH_LONG)
								.show();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
	}

	private void initialize() {
		sDictionary = (Spinner) findViewById(R.id.sDictionary);
		eDictionaty = (EditText) findViewById(R.id.eWord);
		lTranslate = (ListView) findViewById(android.R.id.list);

		voice_btn = (ImageButton) findViewById(R.id.voice_btn);
		voice_btn.setOnClickListener(this);
		voice_btn.setVisibility(showSound() ? View.VISIBLE : View.INVISIBLE);

		context = this;
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

	@Override
	public void onClick(View v) {
		String word = eDictionaty.getText().toString().trim()
				.toLowerCase(Locale.getDefault());
		switch (v.getId()) {
		case R.id.bTranslate:
			lTranslate.setVisibility(View.GONE);
			String translate = dictionary.findTranslate(word);
			Log.d("tag", "trans "+ translate);
			if (translate == null  || translate.equals("")) {
				Toast.makeText(v.getContext(), R.string.no_such_word,
						Toast.LENGTH_SHORT).show();
				break;
			}
			ArrayList<String> translation = new ArrayList<String>();
			translation.add(translate);
			if (translation.size() != 0) {
				lTranslate.setVisibility(View.VISIBLE);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, translation);
				lTranslate.setAdapter(adapter);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);Log.d("tag", "translation.size() " + translation.size());
			}else{
				Toast.makeText(TranslatorActivity.this, R.string.no_such_word,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bClear:
			lTranslate.setAdapter(null);
			eDictionaty.setText("");
			break;
		case R.id.voice_btn:
			myTts.speak(word, TextToSpeech.QUEUE_ADD, null);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem settings = menu.add(R.string.menu_home).setIcon(
				R.drawable.ic_home);
		settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent homeIntent = new Intent(TranslatorActivity.this,
						MainMenuActivity.class);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(homeIntent);
				return false;
			}
		});
		MenuItem info = menu.add(R.string.menu_info).setIcon(R.drawable.ic_inf);
		info.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent info = new Intent(TranslatorActivity.this,
						InfoActivity.class);
				startActivity(info);
				return false;
			}
		});
		return true;
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
