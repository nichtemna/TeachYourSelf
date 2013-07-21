package com.qarea.mlfw.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.qarea.mlfw.R;
import com.qarea.mlfw.fragment.AddPartDialogFragment;
import com.qarea.mlfw.fragment.AddPartDialogFragment.AddPartDialogListener;
import com.qarea.mlfw.util.LocalDataProvider;

public abstract class DictionaryBaseActivity extends FragmentActivity implements
		OnClickListener, AddPartDialogListener {
	protected Button btn_save, btn_create;
	protected ListView list_parts;
	protected EditText et_name;
	protected ArrayList<String> parts_of_speech;
	protected ArrayList<String> parts_of_speech_checked = new ArrayList<String>();
	protected ArrayAdapter<String> adapter;
	protected ArrayList<String> newParts = new ArrayList<String>();
	protected LocalDataProvider dataProvider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_dict);
		initialize();
	}

	protected void initialize() {
		dataProvider = LocalDataProvider.getInstance(this);
		
		parts_of_speech = new ArrayList<String>();
		getPartsOfSpeech();

		btn_save = (Button) findViewById(R.id.button1);
		btn_save.setOnClickListener(this);

		btn_create = (Button) findViewById(R.id.button2);
		btn_create.setOnClickListener(this);

		list_parts = (ListView) findViewById(R.id.listView1);
		setAdapter();
		setChecked();
		et_name = (EditText) findViewById(R.id.editText1);
		
		
	}

	public abstract void setAdapter();

	public abstract void getPartsOfSpeech();

	public abstract void pressSave();

	public abstract void getChosenParts();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button2:
			pressSave();
			break;

		case R.id.button1:
			showAddPartDialog();
		}
	}

	protected boolean checkIfNoSuch(String name) {
		String[] names = LocalDataProvider.getInstance(this)
				.getAllDictionaryNames();
		for (String one_name : names) {
			if (name.equals(one_name)) {
				return false;
			}
		}
		return true;
	}

	public void setChecked() {
		for (int i = 0; i < list_parts.getCount(); i++) {
			list_parts.setItemChecked(i, true);
		}
	}

	protected void showAddPartDialog() {
		FragmentManager fm = getSupportFragmentManager();
		AddPartDialogFragment addPartDialog = new AddPartDialogFragment();
		addPartDialog.show(fm, "fragment_add_part");
	}

	@Override
	public void onFinishAddPartDialog(String inputText) {
		parts_of_speech.add(inputText);
		newParts.add(inputText);
		adapter.notifyDataSetChanged();
		list_parts.setItemChecked(list_parts.getCount() - 1, true);
	}
}
