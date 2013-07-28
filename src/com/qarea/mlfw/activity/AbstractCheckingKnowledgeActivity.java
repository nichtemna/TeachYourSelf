package com.qarea.mlfw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Spinner;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.viewpager.ViewPagerActivity;

public abstract class AbstractCheckingKnowledgeActivity extends BaseActivity {
	protected Spinner sDictionary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		chechIfWords();
	}

	protected void chechIfWords() {
		if (!isWords()) {
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Extras.SHOW_ADD_DIALOG, true);
			editor.commit();

			finish();

			Intent intent = new Intent(this, ViewPagerActivity.class);
			intent.putExtra(DBSchedule.DATE, calendar.getTimeInMillis());
			startActivity(intent);
		}
	}

	protected void showChoseDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getResources().getString(R.string.empty_dictionaty));
		alertDialogBuilder.setMessage(getResources().getString(R.string.choose_dict)).setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						sDictionary.performClick();
						dialog.dismiss();
					}
				}).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
						dialog.dismiss();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
