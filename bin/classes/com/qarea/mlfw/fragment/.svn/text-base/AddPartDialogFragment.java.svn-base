package com.qarea.mlfw.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.qarea.mlfw.R;

public class AddPartDialogFragment extends DialogFragment implements OnEditorActionListener,
                OnClickListener {
    private EditText mEditText;
    private Button btn_save;

    public interface AddPartDialogListener {
        void onFinishAddPartDialog(String inputText);
    }

    public AddPartDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_part_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.editText1);
        btn_save = (Button) view.findViewById(R.id.button1);
        btn_save.setOnClickListener(this);
        getDialog().setTitle("Create new part of speech");

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            returnToActivity();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (mEditText.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please, enter the name for new part of speech",
                                    Toast.LENGTH_LONG).show();
                } else {
                    returnToActivity();
                }
                break;
        }
    }

    private void returnToActivity() {
        AddPartDialogListener activity = (AddPartDialogListener) getActivity();
        activity.onFinishAddPartDialog(mEditText.getText().toString());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        this.dismiss();
    }

}
