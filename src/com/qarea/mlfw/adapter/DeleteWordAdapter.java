package com.qarea.mlfw.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qarea.mlfw.model.Word;

public class DeleteWordAdapter extends ArrayAdapter<Word> {
    private ArrayList<Word> words;
    private Activity activity;

    public DeleteWordAdapter(Activity activity, int textViewResourceId, ArrayList<Word> words) {
        super(activity, textViewResourceId, words);
        this.words = words;
        this.activity = activity;
    }

    public static class ViewHolder {
        public TextView item1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) activity
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(android.R.layout.simple_list_item_multiple_choice, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(android.R.id.text1);
            v.setTag(holder);
        } else
            holder = (ViewHolder) v.getTag();
        final Word word = words.get(position);
        if (word != null) {
            holder.item1.setText(word.getWord());
        }
        return v;
    }

}
