package com.qarea.mlfw.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.CreateWordActivity;
import com.qarea.mlfw.activity.DeleteWordsActivity;
import com.qarea.mlfw.activity.EditWordActivity;
import com.qarea.mlfw.activity.MoveWordActivity;
import com.qarea.mlfw.activity.MoveWordCalendarActivity;
import com.qarea.mlfw.adapter.DayCursorAdapter;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.SelectedDictionary;
import com.qarea.mlfw.viewpager.ViewPagerActivity;

@SuppressLint("ValidFragment")
public class DayViewFragment extends SherlockFragment {
	public static final String DELETE = "delete";
	public static final String MOVE = "move";
	private static final String CURRENT_DATE = "current_date";
	public static final int CREATE_WORD_INT = 2;

	private long currentDate;
	private ListView weekList;
	private Cursor dayWords;
	private Spinner sDictionary;
	private int idDictionary;
	private ArrayList<Word> words;
	private static int viewDay;
	private int checkPosition;
	public String new_word = "";
	private AlertDialog.Builder deleteAlertDialog;
	private SharedPreferences sPrefDict;
	private Calendar calendar, real_calendar;
	private LocalDataProvider dataProvider;
	private static final long ONE_DAY = 86400000;
	private View view;
	private Date d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		d = new Date();
		setHasOptionsMenu(true);
	}

	public DayViewFragment(long currentDate) {
		this.currentDate = currentDate;
	}

	public long getCurrentDate() {
		long date = getArguments().getLong(CURRENT_DATE);
		return date;
	}

	public DayViewFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		d.setTime(currentDate);
		calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTimeInMillis(currentDate);
		viewDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;
		final long curDay = currentDate + viewDay * ONE_DAY
				- (calendar.get(Calendar.DAY_OF_MONTH) - 1) * ONE_DAY;
		if (compareDates(calendar)) {
			if (menu.size() < 6) {
				MenuItem add = menu.add(R.string.create_word).setIcon(
						R.drawable.ic_add);
				add.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent createWord = new Intent(view.getContext(),
								CreateWordActivity.class);

						createWord.putExtra(DBSchedule.DATE, curDay);
						startActivityForResult(createWord, CREATE_WORD_INT);
						return false;
					}
				});

				MenuItem delete = menu.add(R.string.menu_delete).setIcon(
						R.drawable.ic_delete);
				delete.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent delete = new Intent(view.getContext(),
								DeleteWordsActivity.class);
						delete.putExtra("currentDate", curDay);
						startActivity(delete);
						return false;
					}
				});
				MenuItem move = menu.add(R.string.menu_move).setIcon(
						R.drawable.ic_move);
				move.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent delete = new Intent(view.getContext(),
								MoveWordActivity.class);
						delete.putExtra("currentDate", curDay);
						startActivity(delete);
						return false;
					}
				});
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CREATE_WORD_INT:
				new_word = data.getStringExtra("newWord");
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * move listview to new word
	 */
	private void moveToNewWord(ListView listView) {
		Adapter week_adapter = listView.getAdapter();
		for (int i = 0; i < week_adapter.getCount(); i++) {
			View view = week_adapter.getView(i, null, null);
			TextView tv = (TextView) view.findViewWithTag("newWord");
			if (tv.getText().toString().equals(new_word)) {
				weekList.setSelection(i);
			}
		}
	}

	public static DayViewFragment newInstance(long currentDate) {
		DayViewFragment dvf = new DayViewFragment();
		Bundle args = new Bundle();
		args.putLong(CURRENT_DATE, currentDate);
		dvf.setArguments(args);

		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(currentDate);
		return dvf;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_day_view, container, false);

		this.dataProvider = LocalDataProvider.getInstance(view.getContext());
		this.currentDate = getCurrentDate();

		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(currentDate);

		calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTimeInMillis(currentDate);

		words = new ArrayList<Word>();
		checkPosition = -1;
		((TextView) view.findViewById(R.id.day_date))
				.setText((new SimpleDateFormat("dd MMMM yyyy", Locale.US)
						.format(currentDate)));

		weekList = (ListView) view.findViewById(android.R.id.list);
		weekList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.d("tag", " on item click " + position + " " + words.size());
				checkPosition = position;
				deleteAlertDialog = new AlertDialog.Builder(view.getContext());
				deleteAlertDialog.setItems(new String[] { "Delete",
						"Move to other date", "Edit translation" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									// delete word
									dataProvider.deleteDayWords(currentDate,
											words.get(checkPosition));
									onResume();
								} else if (which == 1) {
									// move to other date
									Intent getDateIntent = new Intent(view
											.getContext(),
											MoveWordCalendarActivity.class);
									getDateIntent.putExtra(
											BaseActivity.REPEATE_WORD,
											BaseActivity.REPEATE_WORD_MOVE);
									getDateIntent
											.putExtra(BaseActivity.MOVE_TIME,
													currentDate);
									getDateIntent.putExtra(
											BaseActivity.MOVE_WORD,
											words.get(checkPosition));
									startActivity(getDateIntent);
								} else if (which == 2) {
									// Edit word translation in dictionary
									Intent editIntent = new Intent(view
											.getContext(),
											EditWordActivity.class);
									editIntent.putExtra(BaseActivity.MOVE_WORD,
											words.get(checkPosition));
									editIntent.putExtra(
											BaseActivity.MOVE_DICTIONARY,
											idDictionary);
									startActivity(editIntent);
								}
							}
						});
				deleteAlertDialog.setCancelable(true);
				deleteAlertDialog.show();

			}
		});

		setDictionarySpinner();

		return view;
	}

	private boolean toShowDialog() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(view.getContext());
		if (settings.getBoolean(Extras.SHOW_ADD_DIALOG, false)) {
			real_calendar = Calendar.getInstance();
			real_calendar.set(Calendar.HOUR_OF_DAY, 0);
			real_calendar.set(Calendar.AM_PM, 0);
			real_calendar.set(Calendar.HOUR, 0);
			real_calendar.set(Calendar.MINUTE, 0);
			real_calendar.set(Calendar.SECOND, 0);
			real_calendar.set(Calendar.MILLISECOND, 0);
			if ((calendar.getTimeInMillis() - real_calendar.getTimeInMillis() == 0)) {

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void showDialog() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(view.getContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(Extras.SHOW_ADD_DIALOG, false);
		editor.commit();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder
				.setTitle(getResources().getString(R.string.no_words));
		alertDialogBuilder
				.setMessage(getResources().getString(R.string.add_check_words))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void setDictionarySpinner() {
		sDictionary = (Spinner) view.findViewById(R.id.sDictionary);
		Cursor selectedDictionaryCursor = dataProvider.getSelectedDictionary();// .getSelectedDictionary();
		ArrayList<String> selectedDictionaryArray = new ArrayList<String>();
		while (selectedDictionaryCursor.moveToNext())
			selectedDictionaryArray.add(selectedDictionaryCursor
					.getString(selectedDictionaryCursor
							.getColumnIndex(DBDictionary.NAME)));
		selectedDictionaryCursor.close();
		ArrayAdapter<String> sadapter = new ArrayAdapter<String>(
				view.getContext(), android.R.layout.simple_spinner_item,
				selectedDictionaryArray);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDictionary.setAdapter(sadapter);

		sDictionary.setSelection(SelectedDictionary.getDictionaryList()
				.indexOf(SelectedDictionary.getDictionaryID()));
		sPrefDict = DayViewFragment.this.getActivity().getSharedPreferences(
				"Dictionary", 0);
		Editor ed = sPrefDict.edit();
		ed.putString("Dict", sDictionary.getSelectedItem().toString());
		ed.commit();
		sDictionary.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {

				TextView raw = (TextView) view;
				idDictionary = dataProvider.getDictionaryIdByName(raw.getText()
						.toString());
				SelectedDictionary.setDictionaryID(idDictionary);

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(getActivity()
								.getBaseContext());
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(BaseActivity.SAVE_WORD, idDictionary);
				editor.commit();

				onResume();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/*
	 * Return true if app_date >= real_date
	 */
	private boolean compareDates(Calendar app_calendar) {
		real_calendar = Calendar.getInstance();
		int real_day = real_calendar.get(Calendar.DAY_OF_MONTH);
		int real_month = real_calendar.get(Calendar.MONTH) + 1;
		int app_day = app_calendar.get(Calendar.DAY_OF_MONTH);
		int app_month = app_calendar.get(Calendar.MONTH) + 1;
		if (real_month >= app_month) {
			if (real_day > app_day) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	private void refreshData() {
		words.clear();
		calendar.setTimeInMillis(currentDate);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Long fromDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		Long toDate = calendar.getTimeInMillis();
		if (dayWords != null)
			dayWords.close();
		dayWords = dataProvider.getAllWordsIdAndName(fromDate, toDate);
		if (dayWords.getCount() != 0) {
			for (dayWords.moveToFirst(); !dayWords.isAfterLast(); dayWords
					.moveToNext()) {
				words.add(new Word(dayWords));
			}
			((TextView) view.findViewById(R.id.no_data))
					.setVisibility(View.GONE);
		} else {
			((TextView) view.findViewById(R.id.no_data))
					.setVisibility(View.VISIBLE);
		}
		DayCursorAdapter adapter = new DayCursorAdapter(view.getContext(),
				dayWords, idDictionary, currentDate,
				((ViewPagerActivity) getActivity()).myTTS);
		weekList.setAdapter(adapter);
		if (!new_word.equals("") || new_word != null) {
			moveToNewWord(weekList);
		}
		// if we came from checkKnowledge activity - show this dialog
		if (toShowDialog()) {
			showDialog();
		}
	}

	public void onDestroy() {
		dayWords.close();
		super.onDestroy();
	}

	public void onResume() {
		this.dataProvider = LocalDataProvider.getInstance(view.getContext());
		this.currentDate = getCurrentDate();
		refreshData();
		super.onResume();
	}
}
