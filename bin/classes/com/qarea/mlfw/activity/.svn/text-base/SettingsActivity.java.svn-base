package com.qarea.mlfw.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper.DICTIONARY_TYPE;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.adapter.DictionaryListAdapter;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.service.NotificationService;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.SelectedDictionary;

public class SettingsActivity extends BaseActivity implements OnInitListener,
		OnClickListener {

	public static final String filterExtension = "ua.com.vassiliev.androidfilebrowser.filterExtension";
	private final int REQUEST_CODE_PICK_DIR = 1;
	private final int REQUEST_CODE_PICK_FILE = 2;
	public final static int CHECK_KNOWLEDGE_TAP = 1;
	public final static int CHECK_KNOWLEDGE_SWIPE = 2;

	private Button btn_newDict, btn_importDict;
	private Spinner sHoursStart, sHoursEnd, sHoursTimeout;
	private CheckBox notRemindCheckBox, voiceCheckBox;
	private String hours[], timeout[];
	private int hoursStartPicked, hoursEndPicked, hoursTimeoutPicked;
	private boolean notRemind, voiceEnabled;
	private ListView dictionaryList;
	private DictionaryListAdapter adapter;
	private ServiceConnection sConn;
	private boolean bound = false;
	private NotificationService myService;
	private RadioGroup checkModeGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initialize();

		bindNotifService();

		spinerListeners();
	}

	private void spinerListeners() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		final SharedPreferences.Editor editor = settings.edit();

		sHoursStart.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				hoursStartPicked = position + 1;
				editor.putInt(HOURS_START, hoursStartPicked);
				editor.commit();
				myService.getFromPreferences();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sHoursEnd.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				hoursEndPicked = position + 1;
				editor.putInt(HOURS_END, hoursEndPicked);
				editor.commit();

				myService.getFromPreferences();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		sHoursTimeout.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				hoursTimeoutPicked = position + 1;
				editor.putInt(TIMEOUT, hoursTimeoutPicked);
				editor.commit();

				myService.getFromPreferences();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		notRemindCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						notRemind = isChecked;
						editor.putBoolean(NOT_REMIND, notRemind);
						editor.commit();

						myService.getFromPreferences();
					}
				});

		voiceCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked && !isPackageExists("com.svox.pico")) {
					showAskDialog();
				}
				voiceEnabled = isChecked;
				editor.putBoolean(Extras.VOICE_ENABLED, voiceEnabled);
				editor.commit();

			}
		});

		checkModeGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkModeGroup.getCheckedRadioButtonId()) {
						case R.id.settings_radio0:
							editor.putInt(Extras.CHECK_MODE,
									CHECK_KNOWLEDGE_TAP);
							break;
						case R.id.settings_radio1:
							editor.putInt(Extras.CHECK_MODE,
									CHECK_KNOWLEDGE_SWIPE);
							break;
						}
						editor.commit();
					}
				});
	}

	private void showAskDialog() {
		AlertDialog.Builder askDialog = new AlertDialog.Builder(this);
		askDialog
				.setTitle("Do you agree to install voice support library from Google Play?");
		askDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						voiceCheckBox.setChecked(false);
						SharedPreferences settings = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean(Extras.VOICE_ENABLED, false);
						editor.commit();
					}
				});
		askDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent checkTTSIntent = new Intent();
						checkTTSIntent
								.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
						startActivityForResult(checkTTSIntent,
								Extras.VOICE_CHECK_CODE);
						dialogInterface.dismiss();
					}
				});
		askDialog.setCancelable(true);
		askDialog.show();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			myTts.setLanguage(Locale.ENGLISH);
		} else if (status == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
		if (myTts.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
			myTts.setLanguage(Locale.ENGLISH);
		}
	}

	private void initialize() {
		hours = getResources().getStringArray(R.array.hours);
		timeout = getResources().getStringArray(R.array.timeout);

		SharedPreferences settings2 = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		int timeoutTmp = settings2.getInt(TIMEOUT, 1);
		int startHoursTmp = settings2.getInt(HOURS_START, 8);
		int endHoursTmp = settings2.getInt(HOURS_END, 20);
		notRemind = settings2.getBoolean(NOT_REMIND, false);
		voiceEnabled = settings2.getBoolean(VOICE_ENABLED, false);
		int checkMode = settings2.getInt(Extras.CHECK_MODE, 1);

		notRemindCheckBox = (CheckBox) findViewById(R.id.Not_remind_Settings_checkBox);
		voiceCheckBox = (CheckBox) findViewById(R.id.voice_Settings_checkBox);
		sHoursStart = (Spinner) findViewById(R.id.sStartHours);
		sHoursEnd = (Spinner) findViewById(R.id.sEndHours);
		sHoursTimeout = (Spinner) findViewById(R.id.sTimeout);

		notRemindCheckBox.setChecked(notRemind);
		voiceCheckBox.setChecked(voiceEnabled);

		// initialize spiner
		ArrayAdapter<String> sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, hours);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sHoursStart.setAdapter(sadapter);
		sHoursStart.setSelection(startHoursTmp - 1);

		sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, hours);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sHoursEnd.setAdapter(sadapter);
		sHoursEnd.setSelection(endHoursTmp - 1);

		sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, timeout);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sHoursTimeout.setAdapter(sadapter);
		sHoursTimeout.setSelection(timeoutTmp - 1);

		dictionaryList = (ListView) findViewById(R.id.dictionary_list);
		Cursor allDictionaries = dataProvider.getAllDictionary(false);

		adapter = new DictionaryListAdapter(this, getBaseContext(),
				allDictionaries, dataProvider);
		dictionaryList.setAdapter(adapter);
		LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (adapter.getCount() * 70 + 30));
		dictionaryList.setLayoutParams(mParam);

		btn_newDict = (Button) findViewById(R.id.button1);
		btn_newDict.setOnClickListener(this);
		btn_importDict = (Button) findViewById(R.id.button2);
		btn_importDict.setOnClickListener(this);

		checkModeGroup = (RadioGroup) findViewById(R.id.settings_radioGroup1);
		RadioButton checkedRadioButton = (RadioButton) checkModeGroup
				.getChildAt(checkMode);
		int checkedButtonId;
		if (checkMode == CHECK_KNOWLEDGE_TAP) {
			checkedButtonId = R.id.settings_radio0;
		} else {
			checkedButtonId = R.id.settings_radio1;
		}
		checkModeGroup.check(checkedButtonId);
	}

	/*
	 * bind notification service to this activity
	 */
	private void bindNotifService() {
		Intent intent = new Intent(this, NotificationService.class);
		sConn = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder binder) {
				myService = ((NotificationService.MyBinder) binder)
						.getService();
				bound = true;
			}

			public void onServiceDisconnected(ComponentName name) {
				bound = false;
			}
		};
		bindService(intent, sConn, BIND_AUTO_CREATE);
	}

	/*
	 * unbind notification service from this activity
	 */
	protected void onDestroy() {
		super.onDestroy();
		// unbind Notification service
		if (!bound)
			return;
		unbindService(sConn);
		bound = false;

		// destroy text to speech variable
		if (myTts != null) {
			myTts.stop();
			myTts.shutdown();
		}
	}

	/*
	 * Update dictionary list when added new dictionary
	 */
	@Override
	protected void onResume() {
		Cursor allDictionaries = dataProvider.getAllDictionary(true);
		((CursorAdapter) dictionaryList.getAdapter())
				.changeCursor(allDictionaries);
		LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (dictionaryList.getAdapter()
						.getCount() * 80 + 30));
		dictionaryList.setLayoutParams(mParam);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(SettingsActivity.this,
					CreateNewDictActivity.class);
			startActivity(intent);
			break;

		case R.id.button2:
			Intent fileExploreIntent = new Intent(
					com.qarea.mlfw.activity.FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
					null, SettingsActivity.this,
					com.qarea.mlfw.activity.FileBrowserActivity.class);
			fileExploreIntent
					.putExtra(
							com.qarea.mlfw.activity.FileBrowserActivity.startDirectoryParameter,
							Environment.getExternalStorageDirectory());
			fileExploreIntent.putExtra(filterExtension, "txt");
			startActivityForResult(fileExploreIntent, REQUEST_CODE_PICK_FILE);
			break;
		}
	}

	/*
	 * to get result from file picker
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_FILE) {
			if (resultCode == RESULT_OK) {
				// choosen file path and name
				String oneFile = data
						.getStringExtra(com.qarea.mlfw.activity.FileBrowserActivity.returnFileParameter);
				String name = oneFile.substring(oneFile.lastIndexOf("/") + 1,
						oneFile.indexOf("."));
				if (checkIfNoSuch(name)) {
					try {
						if (checkIfDictionary(oneFile)) {
							dataProvider.insertFile(oneFile, name,
									DICTIONARY_TYPE.INSERTED);
							int dictionaryId = dataProvider
									.getDictionaryIdByName(name);
							dataProvider.insertTypes(getFileTypes(oneFile),
									dictionaryId);
						} else {
							Toast.makeText(this,
									"This file is not dictionary.",
									Toast.LENGTH_LONG).show();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(this,
							"There is already dictionary with this name.",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		if (requestCode == REQUEST_CODE_PICK_DIR) {
			if (resultCode == RESULT_OK) {
				String folder = data
						.getStringExtra(com.qarea.mlfw.activity.FileBrowserActivity.returnDirectoryParameter);
				MyTask mt = new MyTask();
				mt.execute(folder);
			}
		}
		if (requestCode == Extras.VOICE_CHECK_CODE) {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = settings.edit();
			Log.d("tag",
					(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
							+ " check");
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				myTts = new TextToSpeech(this, this);
				voiceCheckBox.setChecked(true);
				editor.putBoolean(Extras.VOICE_ENABLED, true);
				Toast.makeText(SettingsActivity.this, "Already installed",
						Toast.LENGTH_LONG).show();
			} else {
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);

				voiceCheckBox.setChecked(false);
				editor.putBoolean(Extras.VOICE_ENABLED, false);
			}
			editor.commit();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getPreferences() {
		SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		String dictionaryName = sPref.getString(
				DictionaryListAdapter.SHARE_DICT, "");
		return dictionaryName;
	}

	/*
	 * get parts of speech from inserted file
	 */
	private String[] getFileTypes(String oneFile) throws IOException {
		TreeSet<String> types = new TreeSet<String>();
		InputStream in = new FileInputStream(oneFile);
		InputStreamReader fileStream = new InputStreamReader(in);
		BufferedReader readerStream = new BufferedReader(fileStream);
		String tmpString, type;
		int firstStar, secondStar;
		while ((tmpString = readerStream.readLine()) != null) {
			try {
				if (tmpString != null && tmpString.length() != 0) {
					firstStar = tmpString.indexOf('*', 0);
					secondStar = tmpString.indexOf('*', firstStar + 1);
					type = tmpString.substring(firstStar + 1, secondStar);
					types.add(type);
				}
			} catch (Exception e) {
				String error = e.toString();
				error = error + "";
			}
		}
		readerStream.close();
		String[] only_types = new String[types.size()];
		int i = 0;
		for (String string : types) {
			only_types[i] = string;
			i++;
		}
		return only_types;
	}

	/*
	 * chech if this file is dictionary
	 */
	private boolean checkIfDictionary(String oneFile) throws IOException {
		InputStream in = new FileInputStream(oneFile);
		InputStreamReader fileStream = new InputStreamReader(in);
		BufferedReader preReaderStream = new BufferedReader(fileStream);
		String firstLine = preReaderStream.readLine();
		in.close();
		if (firstLine != null) {
			if (firstLine.contains("*")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * check if no such dictionary name when inserting new dict
	 */
	private boolean checkIfNoSuch(String name) {
		String[] names = LocalDataProvider.getInstance(this)
				.getAllDictionaryNames();
		for (String one_name : names) {
			if (name.equals(one_name)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * export dicitionary
	 */
	class MyTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... folder) {
			String dictionaryName = getPreferences();

			NewDictionary.getInstance(SettingsActivity.this).exportDictionary(
					folder[0], dictionaryName);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(SettingsActivity.this,
					"Dictionary was successfully exported! ", Toast.LENGTH_LONG)
					.show();
			super.onPostExecute(result);
		}
	}

	// check if voice support package already installed
	public boolean isPackageExists(String targetPackage) {
		List<ApplicationInfo> packages;
		PackageManager pm;
		pm = getPackageManager();
		packages = pm.getInstalledApplications(0);
		for (ApplicationInfo packageInfo : packages) {
			if (packageInfo.packageName.equals(targetPackage))
				return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem settings = menu.add(R.string.menu_home).setIcon(
				R.drawable.ic_home);
		settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent homeIntent = new Intent(SettingsActivity.this,
						MainMenuActivity.class);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(homeIntent);
				return false;
			}
		});
		MenuItem translator = menu.add(R.string.menu_translator).setIcon(
				R.drawable.ic_translate);
		translator.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (SelectedDictionary.getDictionaryID() == -1)
					Toast.makeText(SettingsActivity.this, "Select dictionary",
							Toast.LENGTH_SHORT).show();
				else {
					Intent translator = new Intent(SettingsActivity.this,
							TranslatorActivity.class);
					startActivity(translator);
				}
				return false;
			}
		});
		MenuItem info = menu.add(R.string.menu_info).setIcon(R.drawable.ic_inf);
		info.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent info = new Intent(SettingsActivity.this,
						InfoActivity.class);
				startActivity(info);
				return false;
			}
		});

		return true;
	}

}
