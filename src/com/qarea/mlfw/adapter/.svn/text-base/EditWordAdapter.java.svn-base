package com.qarea.mlfw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qarea.mlfw.R;

public class EditWordAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private final String[] values;

    public EditWordAdapter(Context context, String[] values) {
        super(context, R.layout.row_edit_word, values);
        this.values = values;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rawLayout = (LinearLayout) inflater.inflate(R.layout.row_edit_word, parent,
                        false);

        TextView tv_part = (TextView) rawLayout.findViewById(R.id.textView1);
        tv_part.setTag("part");
        EditText et_trans = (EditText) rawLayout.findViewById(R.id.editText1);
        et_trans.setTag("translation");

        tv_part.setText(values[position].substring(0, values[position].indexOf(":")));
        et_trans.setText(values[position].substring(values[position].indexOf(":") + 1));

        return rawLayout;
    }

}
