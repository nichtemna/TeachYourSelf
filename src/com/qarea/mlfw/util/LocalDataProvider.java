package com.qarea.mlfw.util;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.qarea.mlfw.DBHelper;
import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.DBHelper.DBStatistic;
import com.qarea.mlfw.DBHelper.DBTypes;
import com.qarea.mlfw.DBHelper.DBWords;
import com.qarea.mlfw.DBHelper.DICTIONARY_TYPE;
import com.qarea.mlfw.R;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.newdictionary.DataProvider;
import com.qarea.mlfw.newdictionary.NewDictionary;

public class LocalDataProvider implements DataProvider {

	public final static String ASSETS_ENGLISH_RUSSIAN = "english_russian.txt";
	public final static String ASSETS_RUSSIAN_ENGLISH = "russian_english.txt";
	public final static int COUNT_DICTIONARY_IN_ASSETS = 2;
	public final static long MILLSINDAY = 24 * 60 * 60 * 1000;

	private static LocalDataProvider instance;
	public static DBHelper db;
	private Context context;

	private LocalDataProvider(Context context) {
		db = new DBHelper(context);
		this.context = context;
	}

	public static LocalDataProvider getInstance(Context context) {
		if (instance == null)
			instance = new LocalDataProvider(context);
		return instance;
	}

	public NewDictionary getDictionary(Context context) {

		return NewDictionary.getInstance(context);
	}

	public void deleteDayWords(long currentDate, Word word) {
		int word_id = word.getId();
		if (word_id > 0) {
			String where = DBSchedule.WORD + "=" + word_id + " AND "
					+ DBSchedule.DATE + " < " + currentDate + MILLSINDAY;
			db.getWritableDatabase().delete(DBSchedule.TABLE_NAME, where, null);
		}
	}

	// return false if word already existed in this day
	public boolean changeDateInWords(long currentDate, Word word, long newDate) {
		int word_id = word.getId();
		String where = DBSchedule.WORD + "=" + word_id + " AND "
				+ DBSchedule.DATE + " = " + newDate;
		Cursor cursor = db.getWritableDatabase().query(DBSchedule.TABLE_NAME,
				new String[] { DBSchedule._ID }, where, null, null, null, null);
		if (cursor.getCount() == 0) {
			where = DBSchedule.WORD + "=" + word_id + " AND " + DBSchedule.DATE
					+ " = " + currentDate;
			ContentValues values = new ContentValues();
			values.put(DBSchedule.DATE, newDate);
			db.getWritableDatabase().update(DBSchedule.TABLE_NAME, values,
					where, null);
			return true;
		} else {
			return false;
		}
	}

	// return false if word already existed in this day
	public void changeDictionaryName(String name, int dictionaryId) {
		String where = DBDictionary._ID + "=" + dictionaryId;
		ContentValues values = new ContentValues();
		values.put(DBDictionary.NAME, name);
		db.getWritableDatabase().update(DBDictionary.TABLE_NAME, values, where,
				null);

	}

	public Cursor getDayWords(long currentDate, int dictionaryId) {

		return db.getReadableDatabase().rawQuery(
				"SELECT DISTINCT a." + DBWords._ID + " , a."
						+ DBWords.WORD_NAME + " , a." + DBWords.DICTIONARY_ID
						+ " , " + DBSchedule.DATE + " FROM "
						+ DBWords.TABLE_NAME + " a INNER JOIN "
						+ DBSchedule.TABLE_NAME + " b ON a." + DBWords._ID
						+ " = b." + DBSchedule.WORD + " WHERE b."
						+ DBSchedule.DATE + " < " + (currentDate + MILLSINDAY)
						+ " AND (b." + DBSchedule.DATE + " + " + MILLSINDAY
						+ ") > " + currentDate + " AND a."
						+ DBWords.DICTIONARY_ID + " = " + dictionaryId
						+ " GROUP BY a." + DBWords._ID + ";", null);
	}

	public void insert(int DictionaryId, String word, long date)
			throws Exception {
		Log.d("tag", "insert " + word + " dict " + DictionaryId);
		word = word.trim();// .toLowerCase();
		if (word == null || word.equals(""))
			throw new Exception("Enter some word to add");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		Cursor sameWord = alreadyExistWord(word, DictionaryId, date);
		Log.d("tag", "sameWord.getCount() " + sameWord.getCount());
		if (sameWord.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(DBWords.DICTIONARY_ID, DictionaryId);
			values.put(DBWords.WORD_NAME, word);
			db.getWritableDatabase().insert(DBWords.TABLE_NAME, null, values);
		}
		// else {
		// throw new Exception(context.getResources().getString(
		// R.string.this_word_exist));
		// }
		sameWord.close();
		int wordId = getWordIdByName(word);
		addSchedule(wordId, date);
	}

	public int getWordIdByName(String word) {
		StringBuilder selection = new StringBuilder();
		selection.append(DBWords.WORD_NAME).append(" = '").append(word)
				.append("'");
		selection.append(" AND ").append(DBWords.DICTIONARY_ID).append(" = ")
				.append(SelectedDictionary.getDictionaryID());
		Cursor wordCursor = db.getReadableDatabase().query(DBWords.TABLE_NAME,
				new String[] { DBWords._ID }, selection.toString(), null, null,
				null, null);
		wordCursor.moveToFirst();
		int wordId = wordCursor.getInt(0);
		wordCursor.close();
		return wordId;
	}

	public int getStatisticCountForPeriod(int dictionaryId, Long dateFrom,
			Long dateTo, boolean onlyCorrect) {
		StringBuilder selection = new StringBuilder();
		selection.append(DBWords.DICTIONARY_ID).append(" = ")
				.append(dictionaryId);
		Cursor allWordsId = db.getReadableDatabase().query(DBWords.TABLE_NAME,
				new String[] { DBWords._ID }, selection.toString(), null, null,
				null, null);
		int resultCount = 0;
		while (allWordsId.moveToNext()) {
			selection = new StringBuilder();
			selection
					.append(DBStatistic.WORD)
					.append(" = ")
					.append(allWordsId.getInt(allWordsId
							.getColumnIndex(DBWords._ID)));
			if (onlyCorrect)
				selection.append(" AND ").append(DBStatistic.RESULT)
						.append(" = 1");
			Cursor statisticCout = db.getReadableDatabase().query(
					DBStatistic.TABLE_NAME, new String[] { DBStatistic._ID },
					selection.toString(), null, null, null, null);
			resultCount += statisticCout.getCount();
			statisticCout.close();
		}
		allWordsId.close();
		return resultCount;
	}

	private Cursor alreadyExistWord(String word, int dictionaryId, long date) {
		Log.d("tag", "word " + word + " dictt " + dictionaryId);
		return db.getReadableDatabase().rawQuery(
				"SELECT a." + DBWords._ID + " FROM " + DBWords.TABLE_NAME
						+ " a INNER JOIN " + DBSchedule.TABLE_NAME + " b ON a."
						+ DBWords._ID + " = b." + DBSchedule._ID + " WHERE "
						+ DBWords.DICTIONARY_ID + " = " + dictionaryId
						+ " AND " + DBWords.WORD_NAME + " = '" + word + "'",
				null);
		// "SELECT a." + DBWords._ID + " FROM " + DBWords.TABLE_NAME
		// + " a INNER JOIN " + DBSchedule.TABLE_NAME + " b ON a."
		// + DBWords._ID + " = b." + DBSchedule._ID + " WHERE "
		// + DBWords.DICTIONARY_ID + " = " + dictionaryId + " AND ("
		// + DBSchedule.DATE + " + " + MILLSINDAY + ") >= " + date
		// + " AND " + DBSchedule.DATE + " < " + (date + MILLSINDAY)
		// + " AND " + DBWords.WORD_NAME + " = '" + word + "'", null);

	}

	// get pare name and id from file in hashmap. Used in
	// CheckingAllDictionary.java for in order to know are there file
	public Cursor getAllDictionary(boolean enabled) {
		String[] columns = new String[] { DBDictionary._ID, DBDictionary.FILE,
				DBDictionary.NAME, DBDictionary.ENABLED };
		String where = null;
		if (enabled) {
			where = DBDictionary.ENABLED + " = 1";
		}
		return db.getReadableDatabase().query(DBDictionary.TABLE_NAME, columns,
				where, null, null, null, null);

	}

	public Cursor getSelectedDictionary() {
		String[] columns = new String[] { DBDictionary._ID, DBDictionary.NAME };
		StringBuilder selection = new StringBuilder();
		selection.append(DBDictionary.ENABLED).append(" = 1");
		selection.append(" AND ").append(DBDictionary.SELECTED).append(" = 1");
		return db.getReadableDatabase().query(DBDictionary.TABLE_NAME, columns,
				selection.toString(), null, null, null, null);

	}

	public int getAllDictionaryCount() {
		String[] columns = new String[] { DBDictionary._ID };
		Cursor cursor = db.getReadableDatabase().query(DBDictionary.TABLE_NAME,
				columns, null, null, null, null, null);
		return cursor.getCount();
	}

	public String[] getAllDictionaryNames() {
		String[] columns = new String[] { DBDictionary.NAME };
		Cursor cursor = db.getReadableDatabase().query(DBDictionary.TABLE_NAME,
				columns, null, null, null, null, null);
		String[] names = new String[cursor.getCount()];
		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				names[i] = cursor.getString(0);
				i++;
			} while (cursor.moveToNext());
		}
		return names;
	}

	// insert in Dicinary
	public void insertFile(String file, String name, DICTIONARY_TYPE dict_type) {
		ContentValues values = new ContentValues();
		values.put(DBDictionary.FILE, file);
		values.put(DBDictionary.NAME, name);
		values.put(DBDictionary.ENABLED, true);
		values.put(DBDictionary.DICT_TYPE, dict_type.toString());
		db.getWritableDatabase().insert(DBDictionary.TABLE_NAME, null, values);
	}

	public void setEnabled(String name) {
		ContentValues values = new ContentValues();
		values.put(DBDictionary.NAME, name);
		values.put(DBDictionary.ENABLED, true);
		db.getWritableDatabase().update(DBDictionary.TABLE_NAME, values,
				DBDictionary.NAME + " = ?", new String[] { name });
	}

	// update all enabled in the Dictionary to false
	public void updateAllEnabledInDictionary() {
		ContentValues values = new ContentValues();
		values.put(DBDictionary.ENABLED, false);
		db.getWritableDatabase().update(DBDictionary.TABLE_NAME, values, null,
				null);
	}

	// update one enable in the Dictionary to true
	public void updateOneEnabledInDictionary(int fileId) {
		ContentValues values = new ContentValues();
		values.put(DBDictionary.ENABLED, true);
		String where = DBDictionary._ID + "=" + fileId;
		db.getWritableDatabase().update(DBDictionary.TABLE_NAME, values, where,
				null);
	}

	public int getCountCorrectOrAllAnswer(int wordId, boolean rightOrWrong) {
		int result = 0;
		String[] columns = new String[] { DBStatistic._ID, DBStatistic.WORD };
		StringBuilder selection = new StringBuilder();
		selection.append(DBStatistic.WORD).append(" = ").append(wordId);
		if (rightOrWrong)
			selection.append(" AND ").append(DBStatistic.RESULT).append("");
		Cursor c = db.getReadableDatabase().query(DBStatistic.TABLE_NAME,
				columns, selection.toString(), null, null, null, null);
		result = c.getCount();
		c.close();
		return result;
	}

	public void insertStatus(int wordId, long date, boolean result) {
		ContentValues cv = new ContentValues();
		cv.put(DBStatistic.WORD, wordId);
		cv.put(DBStatistic.DATE, date);
		cv.put(DBStatistic.RESULT, result);
		db.getWritableDatabase().insert(DBStatistic.TABLE_NAME, null, cv);
	}

	public String getFileNameDictionary(int dictionaryId) {
		String[] columns = new String[] { DBDictionary.FILE };
		String where = DBHelper.DBDictionary._ID + "=" + dictionaryId;
		Cursor cursor = db.getReadableDatabase().query(
				DBHelper.DBDictionary.TABLE_NAME, columns, where, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return "";
		}
		cursor.moveToFirst();
		String fileName = cursor.getString(cursor
				.getColumnIndex(DBDictionary.FILE));
		cursor.close();
		return fileName;
	}

	/*
	 * Checking all dictionary from sdcard and assets If dictionary not in db,
	 * add dictionary
	 */
	public void checkingAllDictionary() {
		String[] fileListInDirectory = new String[COUNT_DICTIONARY_IN_ASSETS];
		fileListInDirectory[0] = ASSETS_ENGLISH_RUSSIAN;
		fileListInDirectory[1] = ASSETS_RUSSIAN_ENGLISH;
	}

	public void insertAssets() {
		String[] assets = new String[COUNT_DICTIONARY_IN_ASSETS];
		assets[0] = ASSETS_ENGLISH_RUSSIAN;
		assets[1] = ASSETS_RUSSIAN_ENGLISH;
		String[] parts = context.getResources().getStringArray(
				R.array.parts_of_speech);
		for (String oneFile : assets) {
			String fileName = oneFile.substring(0, oneFile.indexOf("."));
			insertFile(oneFile, fileName, DICTIONARY_TYPE.ASSET);
			insertTypes(parts, getDictionaryIdByName(fileName));
		}
	}

	public void insertTypes(String[] parts, int dictionaryId) {
		for (int i = 0; i < parts.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(DBTypes.TYPE_NAME, parts[i]);
			cv.put(DBTypes.DICTIONARY_ID, dictionaryId);
			db.getWritableDatabase().insert(DBTypes.TABLE_NAME, null, cv);
		}
	}

	public ArrayList<String> getTypes() {
		ArrayList<String> result = new ArrayList<String>();
		int dictionaryId = getDictionary(context).getDictionaryId();
		String[] columns = new String[] { DBTypes.TYPE_NAME };
		String where = DBTypes.DICTIONARY_ID + "=" + dictionaryId;
		Cursor c = db.getReadableDatabase().query(DBTypes.TABLE_NAME, columns,
				where, null, null, null, null);
		if (c.moveToFirst()) {
			do {
				result.add(c.getString(0));
			} while (c.moveToNext());
		} else {
			result = null;
		}
		return result;
	}

	public ArrayList<String> getTypes(int dictionaryId) {
		ArrayList<String> result = new ArrayList<String>();
		String[] columns = new String[] { DBTypes.TYPE_NAME };
		String where = DBTypes.DICTIONARY_ID + "=" + dictionaryId;
		Cursor c = db.getReadableDatabase().query(DBTypes.TABLE_NAME, columns,
				where, null, null, null, null);
		if (c.moveToFirst()) {
			do {
				result.add(c.getString(0));
			} while (c.moveToNext());
		} else {
			result = null;
		}
		return result;
	}

	public int getCountOfWordsFromDate(Long fromDate) {
		String from = "SELECT words._id FROM words INNER JOIN  schedule ON schedule.word_id = words._id AND ";
		StringBuilder selection_day = new StringBuilder();
		selection_day.append("( ").append(DBSchedule.DATE).append(" < ")
				.append(fromDate + MILLSINDAY).append(")");

		selection_day.append(" AND (").append(DBSchedule.DATE).append(" + ")
				.append(MILLSINDAY).append(" > ").append(fromDate).append(") ");

		String dict_enabled = " INNER JOIN dictionary ON dictionary._id = words.dictionary_id AND (dictionary.selected = 1) GROUP BY words._id";
		String query = from + selection_day.toString() + dict_enabled;
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		int count = cursor.getCount();
		// Log.d("tag", "getCountOfWordsFromDate " + query + " " + count);
		return count;
	}

	public boolean hasAnyWords(Long fromDate, Long toDate) {
		String from = "SELECT words._id FROM words INNER JOIN  schedule ON schedule.word_id = words._id ";
		StringBuilder selection = new StringBuilder();
		selection.append(" AND (").append(DBSchedule.DATE).append(" >=")
				.append(fromDate);
		if (toDate != null) {
			selection.append(" AND ").append(DBSchedule.DATE).append(" < ")
					.append(toDate);
		}
		selection.append(")");
		String dict_enabled = " INNER JOIN dictionary ON dictionary._id = words.dictionary_id AND (dictionary.selected = 1) ";
		String query = from + selection.toString() + dict_enabled;
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		// Log.d("tag", "query hasAnyWords " + query + " " + cursor.getCount());
		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Cursor getAllWordsIdAndName(Long fromDate, Long toDate) {
		String from = "SELECT words._id, words.dictionary_id, words.word FROM words INNER JOIN  schedule ON schedule.word_id = words._id ";
		StringBuilder selection = new StringBuilder();
		selection.append(" AND (").append(DBSchedule.DATE).append(" >=")
				.append(fromDate);
		if (toDate != null) {
			selection.append(" AND ").append(DBSchedule.DATE).append(" < ")
					.append(toDate);
		}
		selection.append(")");
		String where = "WHERE (words.dictionary_id = "
				+ SelectedDictionary.getDictionaryID() + ") GROUP BY words._id";
		String dict_enabled = " INNER JOIN dictionary ON dictionary._id = words.dictionary_id AND (dictionary.selected = 1) ";
		String query = from + selection.toString() + dict_enabled + where;
		// Log.d("tag", "query " + query);
		return db.getReadableDatabase().rawQuery(query, null);
	}

	public ArrayList<Integer> getDayWordsId(Long fromDate, Long toDate) {
		ArrayList<Integer> wordsId = new ArrayList<Integer>();
		StringBuilder selection = new StringBuilder();
		selection.append("( ").append(DBSchedule.DATE).append(" < ")
				.append(fromDate + MILLSINDAY).append(")");
		selection.append(" AND (").append(DBSchedule.DATE).append(" + ")
				.append(MILLSINDAY).append(" > ").append(fromDate).append(") ");
		if (toDate != null) {
			selection.append(" OR ( ").append(DBSchedule.DATE).append(" >= ")
					.append(fromDate);
			selection.append(" AND ").append(DBSchedule.DATE).append(" < ")
					.append(toDate).append(")");
		}
		Cursor wordsIdCursor = db.getReadableDatabase().query(
				DBSchedule.TABLE_NAME, new String[] { DBSchedule.WORD },
				selection.toString(), null, null, null, null);
		if (wordsIdCursor.moveToFirst()) {
			do {
				wordsId.add(wordsIdCursor.getInt(0));
			} while (wordsIdCursor.moveToNext());
		}
		return wordsId;
	}

	public static String getDictionaryNameById(int dictionaryId) {
		Cursor cursor = db.getReadableDatabase()
				.query(DBDictionary.TABLE_NAME,
						new String[] { DBDictionary.FILE },
						DBDictionary._ID + " = " + dictionaryId, null, null,
						null, null);
		String dictionaryName = "";
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			dictionaryName = cursor.getString(cursor
					.getColumnIndex(DBDictionary.FILE));
		}
		cursor.close();
		return dictionaryName;
	}

	public static String getDictionaryTypeById(int dictionaryId) {
		Cursor cursor = db.getReadableDatabase()
				.query(DBDictionary.TABLE_NAME,
						new String[] { DBDictionary.DICT_TYPE },
						DBDictionary._ID + " = " + dictionaryId, null, null,
						null, null);
		String dictionaryType = "";
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			dictionaryType = cursor.getString(cursor
					.getColumnIndex(DBDictionary.DICT_TYPE));
		}
		cursor.close();
		return dictionaryType;
	}

	public void close() {
		if (db != null)
			db.close();
	}

	public boolean dictionaryIdEnabled(int dictionaryId) {
		StringBuilder selection = new StringBuilder();
		selection.append(DBDictionary._ID).append(" = ").append(dictionaryId);
		selection.append(" AND ").append(DBDictionary.ENABLED).append(" = 1");
		Cursor select = db.getReadableDatabase().query(DBDictionary.TABLE_NAME,
				new String[] { DBDictionary._ID }, selection.toString(), null,
				null, null, null);
		int countEnabled = select.getCount();
		select.close();
		if (countEnabled == 1)
			return true;
		return false;

	}

	public void setCheked(int dictId, boolean checked) {
		ContentValues values = new ContentValues();
		values.put(DBDictionary.SELECTED, checked);
		String whereClause = DBDictionary._ID + " = " + dictId;
		db.getWritableDatabase().update(DBDictionary.TABLE_NAME, values,
				whereClause, null);
	}

	public void updateDate(int wordId, double newDate) {
		ContentValues values = new ContentValues();
		values.put(DBSchedule.DATE, newDate);
		String whereClause = DBSchedule._ID + " = " + wordId;
		db.getWritableDatabase().update(DBSchedule.TABLE_NAME, values,
				whereClause, null);
	}

	public int getDictionaryIdByName(String dictionaryName) {
		Cursor cursor = db.getReadableDatabase().query(DBDictionary.TABLE_NAME,
				new String[] { DBDictionary._ID },
				DBDictionary.NAME + " = '" + dictionaryName + "'", null, null,
				null, null);
		cursor.moveToFirst();
		int returnInt = cursor.getInt(0);
		cursor.close();
		return returnInt;
	}

	public String getDictionaryFileByName(String dictionaryName) {
		Cursor cursor = db.getReadableDatabase().query(DBDictionary.TABLE_NAME,
				new String[] { DBDictionary.FILE },
				DBDictionary.NAME + " = '" + dictionaryName + "'", null, null,
				null, null);
		cursor.moveToFirst();
		String returnString = cursor.getString(0);
		cursor.close();
		return returnString;
	}

	public void addSchedule(int wordId, long startDate) {
		Log.d("tag", "addSchedule " + wordId);
		ContentValues values = new ContentValues();
		values.put(DBSchedule.WORD, wordId);
		values.put(DBSchedule.DATE, startDate);
		db.getWritableDatabase().insert(DBSchedule.TABLE_NAME, null, values);
	}

	public void updateShedule(int wordId, long startDate) {
		ContentValues values = new ContentValues();
		values.put(DBSchedule.DATE, startDate);
		db.getWritableDatabase().update(DBSchedule.TABLE_NAME, values,
				DBSchedule.WORD + "=" + wordId, null);
	}

}
