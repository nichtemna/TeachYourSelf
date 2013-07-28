package com.qarea.mlfw.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.EditWordAdapter;
import com.qarea.mlfw.fragment.AddTranslationDialogFragment;
import com.qarea.mlfw.fragment.AddTranslationDialogFragment.OnAddNewTranslationListener;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.util.LocalDataProvider;

public class EditWordActivity extends BaseActivity implements OnClickListener, OnAddNewTranslationListener {
	private TextView tv_word;
	private ListView words_list;
	private Button btn_save, btn_add;
	private Word word;
	private NewDictionary dictionary;
	private String[] words;
	private ArrayList<String> parts = new ArrayList<String>();
	private EditWordAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_word);
		Intent intent = getIntent();
		word = (Word) intent.getSerializableExtra(BaseActivity.MOVE_WORD);
		initViews();
		initialize();
	}

	private void initViews() {
		tv_word = (TextView) findViewById(R.id.textView1);
		words_list = (ListView) findViewById(R.id.listView1);
		btn_save = (Button) findViewById(R.id.button1);
		btn_add = (Button) findViewById(R.id.button2);
	}

	private void initialize() {
		dictionary = LocalDataProvider.getInstance(this).getDictionary(this);
		updateList();
		tv_word.setText(word.getWord());
		btn_save.setOnClickListener(this);
		btn_add.setOnClickListener(this);
	}

	protected void showAddTranslationDialog() {
		FragmentManager fm = getSupportFragmentManager();
		AddTranslationDialogFragment mDialogFragment = AddTranslationDialogFragment.getInstance(parts);
		mDialogFragment.show(fm, "fragment_add_translatiopn");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			updateWords();
			finish();
			break;
		case R.id.button2:
			updateWords();
			showAddTranslationDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void addNewWord(String translation, String type) {
		dictionary.addWordInDictionary(tv_word.getText().toString(), type, translation + "*");
		updateList();
	}

	private void updateList() {
		words = dictionary.findTranslateArray(word.getWord());
		adapter = new EditWordAdapter(this, words);
		words_list.setAdapter(adapter);
		parts.clear();
		for (int i = 0; i < words.length; i++) {
			parts.add(words[i].substring(0, words[i].indexOf(":")));
		}
	}

	private void updateWords() {
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = words_list.getChildAt(i);
			EditText et = (EditText) view.findViewWithTag("translation");
			TextView tv = (TextView) view.findViewWithTag("part");
			dictionary.updateWordInDictionary(word.getWord(), tv.getText().toString(), et.getText().toString());
		}
	}

}
