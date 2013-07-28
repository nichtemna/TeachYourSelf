package com.qarea.mlfw.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.SelectedDictionary;

public class AddNewWordActivity extends BaseActivity implements OnClickListener {

	private ListView words_list;
	private String word;
	public ArrayList<String> words = new ArrayList<String>();
	public ArrayList<String> chosen_types = new ArrayList<String>();
	private ArrayAdapter<String> adapter_dialog, adapter_list;
	private String chosen_type;
	private HashMap<String, String> word_translation = new HashMap<String, String>();
	private NewDictionary dictionary;
	private ArrayList<String> types;
	private Button btn_more, btn_save, btn_chose;
	private EditText et_translate, et_word;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_word);

		initialization();

		adapter_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words);

		words_list.setAdapter(adapter_list);
	}

	private void initialization() {
		dictionary = LocalDataProvider.getInstance(this).getDictionary(this);
		word = getIntent().getStringExtra("word");
		et_word = (EditText) findViewById(R.id.editText1);
		et_word.setText(word);
		et_translate = (EditText) findViewById(R.id.editText2);
		btn_more = (Button) findViewById(R.id.button1);
		btn_more.setOnClickListener(this);
		btn_save = (Button) findViewById(R.id.button2);
		btn_save.setOnClickListener(this);
		btn_chose = (Button) findViewById(R.id.button3);
		btn_chose.setOnClickListener(this);
		words_list = (ListView) findViewById(R.id.listView1);
		spinner = (Spinner) findViewById(R.id.spinner1);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			// if user don't chose type - show Toast
			if (btn_chose.getText().toString().equals(getResources().getString(R.string.enter_type))) {
				Toast.makeText(AddNewWordActivity.this, getResources().getString(R.string.enter_type), Toast.LENGTH_LONG).show();
			} else {
				// remove chosen type from dialog and add word to list
				chosen_types.remove(chosen_type);
				adapter_dialog.notifyDataSetChanged();

				word_translation.put(chosen_type, et_translate.getText().toString());
				words.add(chosen_type + "\n" + et_translate.getText().toString());
				adapter_list.notifyDataSetChanged();
				btn_chose.setText(getResources().getString(R.string.enter_type));
				et_translate.setText("");
			}
			break;
		case R.id.button2:
			// save word's translation to db and word to day
			if (!et_translate.getText().toString().equals("")) {
				if (btn_chose.getText().toString().equals(getResources().getString(R.string.enter_type))) {
					Toast.makeText(AddNewWordActivity.this, getResources().getString(R.string.enter_type), Toast.LENGTH_LONG).show();
					break;
				} else {
					word_translation.put(chosen_type, et_translate.getText().toString());
				}
			}

			addNewWordToDictionary();
			addWordToDay();
			finish();
			break;
		case R.id.button3:
			// show dialog parts of speech
			showDialog(0);
			break;
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		switch (id) {

		case 0:
			types = dataProvider.getTypes();
			adapter_dialog = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, types);
			adb.setAdapter(adapter_dialog, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// set type name on button and remove this type from
					// the list
					chosen_type = adapter_dialog.getItem(which);
					btn_chose.setText(chosen_type);
					types.remove(types.indexOf(chosen_type));
					adapter_dialog.notifyDataSetChanged();
				}
			});
			break;

		}
		return adb.create();
	}

	private void addNewWordToDictionary() {

		Iterator<Map.Entry<String, String>> iter = word_translation.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> pairs = iter.next();
			dictionary.addWordInDictionary(et_word.getText().toString(), pairs.getKey(), pairs.getValue() + "*");
		}
	}

	private void addWordToDay() {
		calendar.setTimeInMillis(getIntent().getLongExtra("currentDate", 0));
		clearCalendar();
		try {
			dataProvider.insert(SelectedDictionary.getDictionaryID(), et_word.getText().toString(), calendar.getTimeInMillis());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
