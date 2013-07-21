package com.qarea.mlfw.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.qarea.mlfw.R;
import com.qarea.mlfw.model.WordWithTranslation;

public class ShowCorrectnessDialogFragment extends DialogFragment implements
		android.content.DialogInterface.OnDismissListener {
	public static final String CORRECT = "correct";
	public static final String WORD = "word";

	private boolean mCorrect;
	private WordWithTranslation mWord;
	private TextView tv_correct, tv_word, tv_trans;
	private OnDismissListener mActivity;

	public static ShowCorrectnessDialogFragment newInstance(boolean correct,
			WordWithTranslation word) {
		ShowCorrectnessDialogFragment dialog = new ShowCorrectnessDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean(CORRECT, correct);
		args.putSerializable(WORD, word);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCorrect = getArguments().getBoolean(CORRECT);
		mWord = (WordWithTranslation) getArguments().getSerializable(WORD);
		if (getActivity() instanceof OnDismissListener) {
			mActivity = (OnDismissListener) getActivity();
		} else {
			Log.d("tag", getActivity().getClass()
					+ " must implement OnDismissListener");
			dismiss();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.show_correctness_dialog,
				container);
		initViews(view);
		populate();

		return view;
	}

	private void initViews(View view) {
		tv_correct = (TextView) view.findViewById(R.id.sc_dialog_tv_correct);
		tv_word = (TextView) view.findViewById(R.id.sc_dialog_tv_word);
		tv_trans = (TextView) view.findViewById(R.id.sc_dialog_tv_trans);
	}

	private void populate() {
		if (mCorrect) {
			tv_correct.setText(R.string.you_are_right);
			tv_correct.setTextColor(getResources().getColor(
					R.color.check_knowledge_green));
		} else {
			tv_correct.setText(R.string.you_are_wrong);
			tv_correct.setTextColor(getResources().getColor(
					R.color.check_knowledge_red));
		}
		tv_word.setText(mWord.getWord());
		tv_trans.setText(mWord.getTranslation());
	}

	public interface OnDismissListener {
		void onDismiss();

		boolean isRefreshing();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (!mActivity.isRefreshing()) {
			mActivity.onDismiss();
		}
		super.onDismiss(dialog);
	}

}
