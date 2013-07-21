package com.qarea.mlfw.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.EditDictionaryActivity;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.SelectedDictionary;

public class DictionaryListAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private ArrayList<Integer> selectedDictionary;
    private LocalDataProvider dataProvider;
    private Context myContext;
    private Context baseContext;
    private AlertDialog.Builder alertDialog;
    private final int REQUEST_CODE_PICK_DIR = 1;
    public final static String SHARE_DICT = "share_dict_name";

    public DictionaryListAdapter(Context context, Context baseContext, Cursor cursor,
                    LocalDataProvider dataProvider) {
        super(context, cursor, 0);
        inflater = LayoutInflater.from(context);
        selectedDictionary = SelectedDictionary.getDictionaryList();
        this.myContext = context;
        this.baseContext = baseContext;
        this.dataProvider = dataProvider;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int dictId = cursor.getInt(cursor.getColumnIndex(DBDictionary._ID));

        final String dictName = cursor.getString(cursor.getColumnIndex(DBDictionary.NAME));

        TextView dictionaryName = (TextView) view.findViewById(R.id.tvDictionaryName);
        dictionaryName.setText(cursor.getString(cursor.getColumnIndex(DBDictionary.NAME)));
        CheckBox dictionaryCheck = (CheckBox) view.findViewById(android.R.id.checkbox);

        if (selectedDictionary.contains(dictId)) {
            dictionaryCheck.setChecked(true);
        } else {
            dictionaryCheck.setChecked(false);
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showMenuFull(dictId, dictName);
            }
        });

        dictionaryCheck.setOnClickListener(new OnClickListener() {

            private ProgressDialog progressDilaog = null;

            class DownloadTask extends AsyncTask<String, Void, Object> {
                protected Object doInBackground(String... args) {
                    if (!LocalDataProvider.getDictionaryNameById(dictId).equals("file")) {
                        LocalDataProvider.getInstance(myContext).getDictionary(myContext)
                                        .fillBase(myContext);
                    } else {

                        LocalDataProvider.getInstance(myContext).getDictionary(myContext)
                                        .createNewDict(myContext);
                        LocalDataProvider.getInstance(myContext).setCheked(dictId, true);
                    }
                    return "";
                }

                protected void onPostExecute(Object result) {
                    if (progressDilaog != null) {
                        progressDilaog.dismiss();
                    }
                }
            }

            @Override
            public void onClick(View v) {
                ArrayList<Integer> dicts = SelectedDictionary.getDictionaryList();
                if (!dicts.contains(dictId)) {
                    SelectedDictionary.addDictionary(dictId);
                    SelectedDictionary.setDictionaryID(dictId);
                    dataProvider.setCheked(dictId, true);
                    this.progressDilaog = ProgressDialog.show(myContext, "Downloading..",
                                    "Downloading Data...", true, false);
                    new DownloadTask().execute();
                    SharedPreferences settings = PreferenceManager
                                    .getDefaultSharedPreferences(baseContext);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(BaseActivity.SAVE_WORD, dictId);
                    editor.commit();
                } else {
                    dataProvider.setCheked(dictId, false);
                    SelectedDictionary.removeDictionary(dictId);
                    if (SelectedDictionary.getDictionaryID() == dictId) {
                        if (SelectedDictionary.getDictionaryList().size() == 0) {
                            SelectedDictionary.setDictionaryID(-1);
                        } else {
                            SelectedDictionary.setDictionaryID(selectedDictionary.get(0));
                        }

                        SharedPreferences settings = PreferenceManager
                                        .getDefaultSharedPreferences(baseContext);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(BaseActivity.SAVE_WORD, SelectedDictionary.getDictionaryID());
                        editor.commit();
                    }
                }
            }
        });

    }

    private void showMenuFull(final int dictionaryId, final String dictionaryName) {
        alertDialog = new AlertDialog.Builder(myContext);
        alertDialog.setItems(new String[] { "Edit dictionary", "Export dictionary" },
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(myContext,
                                                    EditDictionaryActivity.class);
                                    intent.putExtra("dictionaryId", dictionaryId);
                                    intent.putExtra("dictionaryName", dictionaryName);
                                    myContext.startActivity(intent);
                                } else if (which == 1) {
                                    SelectedDictionary.setDictionaryID(dictionaryId);
                                    SharedPreferences settings = PreferenceManager
                                                    .getDefaultSharedPreferences(baseContext);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putInt(BaseActivity.SAVE_WORD, dictionaryId);
                                    editor.commit();

                                    Intent fileExploreIntent = new Intent(
                                                    com.qarea.mlfw.activity.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                                                    null,
                                                    myContext,
                                                    com.qarea.mlfw.activity.FileBrowserActivity.class);
                                    fileExploreIntent
                                                    .putExtra(com.qarea.mlfw.activity.FileBrowserActivity.startDirectoryParameter,
                                                                    Environment.getExternalStorageDirectory());
                                    ((Activity) myContext).startActivityForResult(
                                                    fileExploreIntent, REQUEST_CODE_PICK_DIR);

                                    savePreferences(dictionaryName);
                                }
                            }
                        });
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void savePreferences(String dictionaryName) {
        SharedPreferences sPref = ((Activity) myContext).getPreferences(Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString(SHARE_DICT, dictionaryName);
        ed.commit();
    }

    @Override
    public View newView(Context view, Cursor context, ViewGroup groupView) {
        return inflater.inflate(R.layout.raw_dictionary_list, null);
    }
}
