package com.qarea.mlfw.activity;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qarea.mlfw.R;

public class EditDictionaryActivity extends DictionaryBaseActivity {

    private int dictionaryId;
    private String dictionaryName;
    protected String[] parts;

    @Override
    public void initialize() {
        super.initialize();
        dictionaryName = getIntent().getStringExtra("dictionaryName");
        et_name.setText(dictionaryName);
        et_name.setEnabled(false);
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("Add new parts of speech");
    }

    public void pressSave() {
        getChosenParts();
        saveDictionaryParts();
        finish();
    }

    @Override
    public void getPartsOfSpeech() {
        dictionaryId = getIntent().getIntExtra("dictionaryId", 0);
        parts_of_speech = dataProvider.getTypes(dictionaryId);
    }

    private void saveDictionaryParts() {
        dataProvider.insertTypes(parts, dictionaryId);
    }

    public void setAdapter() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        parts_of_speech);
        list_parts.setAdapter(adapter);
    }

    @Override
    public void getChosenParts() {
        parts = new String[newParts.size()];
        for (int i = 0; i < newParts.size(); i++) {
            parts[i] = newParts.get(i);
        }
    }
}
