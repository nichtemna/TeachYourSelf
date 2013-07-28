package com.qarea.mlfw.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.LocalDataProvider;

public class AddTranslationDialogFragment extends DialogFragment implements OnClickListener {
	private EditText mEditText;
	private Button btn_save, btn_choose;

	private LocalDataProvider dataProvider;
	private ArrayAdapter<String> adapter_dialog;
	private ArrayList<String> types;
	private String chosen_type;

	public static AddTranslationDialogFragment getInstance(ArrayList<String> usedParts) {
		AddTranslationDialogFragment dialogFragment = new AddTranslationDialogFragment();
		Bundle args = new Bundle();
		args.putStringArrayList(Extras.USED_PARTS, usedParts);
		dialogFragment.setArguments(args);
		return dialogFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		dataProvider = LocalDataProvider.getInstance(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(getActivity().getString(R.string.enter_translation));
		View view = inflater.inflate(R.layout.new_word_part_dialog, container);
		initialize(view);
		return view;
	}

	private void initialize(View view) {
		btn_choose = (Button) view.findViewById(R.id.button1);
		btn_choose.setOnClickListener(this);
		btn_save = (Button) view.findViewById(R.id.button2);
		btn_save.setOnClickListener(this);
		mEditText = (EditText) view.findViewById(R.id.editText1);

		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			showPartsDialog();
			break;
		case R.id.button2:
			if (mEditText.getText().toString().length() == 0) {
				Toast.makeText(getActivity(), getActivity().getString(R.string.enter_some_translation), Toast.LENGTH_LONG).show();
			} else if (btn_choose.getText().toString().equals(getActivity().getString(R.string.enter_type))) {
				Toast.makeText(getActivity(), getActivity().getString(R.string.choose_some_part), Toast.LENGTH_LONG).show();
			} else {
				if (getActivity() instanceof OnAddNewTranslationListener) {
					OnAddNewTranslationListener listener = (OnAddNewTranslationListener) getActivity();
					listener.addNewWord(mEditText.getText().toString(), btn_choose.getText().toString());
					dismiss();
				} else {
					Log.d("tag", getActivity().getClass() + " must implement OnAddNewTranslationListener");
				}
			}
			break;
		default:
			break;
		}
	}

	private void showPartsDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		types = dataProvider.getTypes();
		types.removeAll(getArguments().getStringArrayList(Extras.USED_PARTS));
		adapter_dialog = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, types);
		adb.setAdapter(adapter_dialog, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// set type name on button and remove this type from
				// the list
				chosen_type = adapter_dialog.getItem(which);
				btn_choose.setText(chosen_type);
				types.remove(types.indexOf(chosen_type));
				adapter_dialog.notifyDataSetChanged();
			}
		});
		adb.show();
	}

	public interface OnAddNewTranslationListener {
		public void addNewWord(String translation, String type);
	}

}
