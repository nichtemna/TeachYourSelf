package com.qarea.mlfw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.EditWordAdapter;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.util.LocalDataProvider;

public class EditWordActivity extends BaseActivity {
    private TextView tv_word;
    private ListView words_list;
    private Button btn_save;
    private Word word;
    private NewDictionary dictionary;
    private String[] words;
    private EditWordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);
        Intent intent = getIntent();
        word = (Word) intent.getSerializableExtra(BaseActivity.MOVE_WORD);

        tv_word = (TextView) findViewById(R.id.textView1);
        tv_word.setText(word.getWord());
        dictionary = LocalDataProvider.getInstance(this).getDictionary(this);
        words = dictionary.findTranslateArray(word.getWord());

        words_list = (ListView) findViewById(R.id.listView1);
        adapter = new EditWordAdapter(this, words);
        words_list.setAdapter(adapter);
        btn_save = (Button) findViewById(R.id.button1);
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    View view = words_list.getChildAt(i);
                    EditText et = (EditText) view.findViewWithTag("translation");
                    TextView tv = (TextView) view.findViewWithTag("part");
                    dictionary.updateWordInDictionary(word.getWord(), tv.getText().toString(), et
                                    .getText().toString());
                    finish();
                }
            }
        });

    }
}
