package com.qarea.mlfw.activity;

import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qarea.mlfw.DBHelper;
import com.qarea.mlfw.R;

public class CreateNewDictActivity extends DictionaryBaseActivity {
    private String[] parts;

    public void getPartsOfSpeech() {
        for (String part : getResources().getStringArray(R.array.parts_of_speech)) {
            parts_of_speech.add(part);
        }
    }

    private void createDictionary() {
        String name = et_name.getText().toString();
        if (checkIfNoSuch(name)) {
            dataProvider.insertFile("file", name, DBHelper.DICTIONARY_TYPE.CUSTOM);
            dataProvider.insertTypes(parts, dataProvider.getDictionaryIdByName(name));
            dataProvider.setEnabled(name);
            finish();
        } else {
            Toast.makeText(this, "There is already dictionary with this name.", Toast.LENGTH_LONG)
                            .show();
        }
    }

    public void pressSave() {
        if (et_name.getText().toString().equals("")) {
            Toast.makeText(CreateNewDictActivity.this, "Please, enter the dictionary name",
                            Toast.LENGTH_LONG).show();
        } else {
            getChosenParts();
            if (parts.length > 0) {
                createDictionary();
            } else {
                Toast.makeText(CreateNewDictActivity.this,
                                "Please, choose at least one part of speech", Toast.LENGTH_LONG)
                                .show();
            }
        }
    }

    public void getChosenParts() {
        SparseBooleanArray checked = list_parts.getCheckedItemPositions();
        for (int i = 0; i < list_parts.getCount(); i++) {
            if (checked.get(i)) {
                parts_of_speech_checked.add(list_parts.getItemAtPosition(i).toString());
            }
        }
        parts = new String[parts_of_speech_checked.size()];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts_of_speech_checked.get(i);
        }
    }

    public void setAdapter() {
        list_parts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,
                        parts_of_speech);
        list_parts.setAdapter(adapter);
    }
}
